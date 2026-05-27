package com.example.topfood2604.config;

import com.example.topfood2604.entity.Member;
import com.example.topfood2604.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class NavbarControllerAdvice {

    private final MemberRepository memberRepository;

    public NavbarControllerAdvice(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @ModelAttribute
    public void addLoginInfo(Model model, Authentication authentication) {

        boolean loggedIn =
                authentication != null &&
                        authentication.isAuthenticated() &&
                        !"anonymousUser".equals(authentication.getPrincipal());

        model.addAttribute("loggedIn", loggedIn);

        if (!loggedIn) {
            return;
        }

        String username = authentication.getName();

        String displayName = username;
        if (displayName != null && displayName.contains("@")) {
            displayName = displayName.substring(0, displayName.indexOf("@"));
        }

        model.addAttribute("loginUsername", username);
        model.addAttribute("loginDisplayName", displayName);

        String role = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");

        model.addAttribute("loginRole", role);

        Member member = memberRepository
                .findByUsername(username)
                .orElse(null);

        if (member != null) {
            model.addAttribute("loginMemberId", member.getId());
        }
    }
}