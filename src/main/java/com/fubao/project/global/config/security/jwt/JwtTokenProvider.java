package com.fubao.project.global.config.security.jwt;

import com.fubao.project.domain.api.auth.dto.response.AuthTokens;
import com.fubao.project.global.common.exception.ResponseCode;
import com.fubao.project.global.util.RedisUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

import static io.jsonwebtoken.Jwts.builder;


@Slf4j
@Component
public class JwtTokenProvider {

    public static final String TOKEN_PREFIX = "Bearer ";

    private static final String TOKEN_TYPE = "token_type";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private final SecretKey secretKey;

    private final long accessTokenValidityInMinute;
    private final long refreshTokenValidityInDay;

    private final UserDetailsService userDetailsService;

    private final JwtParser jwtParser;
    private final RedisUtil redisUtil;

    public JwtTokenProvider(
            @Value("${security.access-token-minute}") long accessTokenMinute,
            @Value("${security.refresh-token-day}") long refreshTokenDay,
            @Value("${security.code}") String secret,
            UserDetailsService userDetailsService, RedisUtil redisUtil) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessTokenValidityInMinute = accessTokenMinute;
        this.refreshTokenValidityInDay = refreshTokenDay;
        this.userDetailsService = userDetailsService;
        this.redisUtil = redisUtil;
        this.jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
    }

    public AuthTokens createAccessToken(String id) {
        AuthTokens authTokens = AuthTokens.of(createToken(id), createRefreshToken(id));
        redisUtil.setStringData(authTokens.getRefreshToken(), authTokens.getAccessToken(), Duration.ofDays(refreshTokenValidityInDay));
        return authTokens;
    }

    private String createToken(String username) {
        LocalDateTime now = LocalDateTime.now();
        return builder()
                .setSubject(username)
                .claim(TOKEN_TYPE, ACCESS_TOKEN)
                .setIssuedAt(Timestamp.valueOf(now))
                .setExpiration(Timestamp.valueOf(now.plusMinutes(accessTokenValidityInMinute)))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private String createRefreshToken(String username) {
        LocalDateTime now = LocalDateTime.now();
        return builder()
                .setSubject(username)
                .claim(TOKEN_TYPE, REFRESH_TOKEN)
                .setIssuedAt(Timestamp.valueOf(now))
                .setExpiration(Timestamp.valueOf(now.plusDays(refreshTokenValidityInDay)))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 유효성 및 만료기간 검사
    public boolean validateToken(String token, HttpServletRequest request) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: ", e);
            request.setAttribute("exception", ResponseCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: ", e);
            request.setAttribute("exception", ResponseCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: ", e);
            request.setAttribute("exception", ResponseCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: ", e);
            request.setAttribute("exception", ResponseCode.INVALID_TOKEN);
        }
        return false;
    }

    // 토큰에서 인증 정보 추출
    public Authentication getAuthentication(String accessToken) {
        String usernameFromToken = jwtParser.parseClaimsJws(accessToken).getBody().getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(usernameFromToken);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String getUsernameFromRefreshToken(String refreshToken) {
        return jwtParser.parseClaimsJws(refreshToken).getBody().getSubject();
    }
}