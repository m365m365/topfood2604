package com.example.topfood2604.security;

import com.example.topfood2604.entity.Member;
import com.example.topfood2604.repository.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("找不到會員：" + username));

        boolean enabled = "ACTIVE".equalsIgnoreCase(member.getState());

        return new User(
                member.getUsername(),
                member.getPassword(),
                enabled,
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority(member.getRole()))
        );
    }
}