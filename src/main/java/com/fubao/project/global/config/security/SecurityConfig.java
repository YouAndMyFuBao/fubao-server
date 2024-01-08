package com.fubao.project.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fubao.project.global.config.security.jwt.JwtAuthenticationCheckFilter;
import com.fubao.project.global.config.security.jwt.JwtAuthenticationEntryPoint;
import com.fubao.project.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.authentication.AuthenticationManagerFactoryBean;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //    @Value("${security.permitted-urls}")
//    private final List<String> PERMITTED_URLS;
    private final String[] GET_AUTHENTICATED_URLS = {
            "/api/posts/my"
    };
    private final String[] GET_PERMITTED_URLS = {
            "/api/swagger-ui/**", "/api/swagger-resources/**", "/api/v3/api-docs/**",
            "/api/posts","/api/posts/*", "/api/posts/*/download", "/api/posts/fubao/love","/api/auth/kakao/code"
    };
    private final String[] POST_PERMITTED_URLS = {
            "/api/auth/kakao", "/api/auth/refresh", "/api/auth/logout",
            "/api/posts/fubao/love",
            "/api/test/success", "/api/test/fail"
    };
    @Value("${security.cors-urls}")
    private final List<String> CORS_URLS;

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http, JwtAuthenticationCheckFilter jwtAuthenticationCheckFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) throws Exception {
        http
                .cors(httpSecurityCorsConfigurer -> corsConfigurationSource())//CORS 허용 정책(Front , Back 사이에 도메인이 달라지는 경우)
                .csrf(AbstractHttpConfigurer::disable) //서버에 인증정보를 저장하지 않기 때문에 굳이 불필요한 csrf 코드들을 작성할 필요가 없다.
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //스프링시큐리티가 생성하지도않고 기존것을 사용하지도 않음 ->JWT 같은토큰방식을 쓸때 사용하는 설정
                .formLogin(AbstractHttpConfigurer::disable)// 서버에서  View를 배포하지 안으므로 disable
                .httpBasic(AbstractHttpConfigurer::disable) // JWT 인증 방식을 사용하기에 httpBasic을 이용한 인증방식 사용X
                .addFilterAfter(jwtAuthenticationCheckFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(config -> config
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) //인증 중 예외 발생 시 jwtAuthenticationEntryPoint 호출
                )
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.GET, GET_AUTHENTICATED_URLS).authenticated()
                        .requestMatchers(HttpMethod.GET, GET_PERMITTED_URLS).permitAll()
                        .requestMatchers(HttpMethod.POST, POST_PERMITTED_URLS).permitAll()
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    // CORS 허용 적용
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(CORS_URLS);
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(ApplicationContext context) throws Exception {
        AuthenticationManagerFactoryBean authenticationManagerFactoryBean = new AuthenticationManagerFactoryBean();
        authenticationManagerFactoryBean.setBeanFactory(context);
        return authenticationManagerFactoryBean.getObject();
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        return new JwtAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    public JwtAuthenticationCheckFilter jwtAuthenticationCheckFilter(JwtTokenProvider jwtTokenProvider) {
        return new JwtAuthenticationCheckFilter(jwtTokenProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
