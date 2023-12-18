package com.fubao.project.global.config.security.jwt;

import com.fubao.project.domain.entity.Member;
import com.fubao.project.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<Member> member = memberRepository.findById(UUID.fromString(username));
        return User.withUsername(username)
                .password(member.get().getId().toString())
                .authorities(AuthorityUtils.NO_AUTHORITIES)
                .build();
    }
}