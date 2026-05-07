package com.example.topfood2604.config;

import com.example.topfood2604.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(customUserDetailsService);

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // 公開頁面
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/navbar.css",
                                "/favicon.ico"
                        ).permitAll()

                        // 公開 API：首頁與 AI 搜尋不需要登入
                        .requestMatchers(
                                "/api/homepage",
                                "/api/restaurants/**",
                                "/api/AiRestaurantApi/search",
                                "/api/member/check-login",
                                "/api/recommended-restaurants"
                        ).permitAll()

                        // 會員功能：必須登入
                        .requestMatchers(
                                "/recommend",
                                "/my-recommend",
                                "/recommend-detail"
                        ).authenticated()

                        // 後台：只有 ADMIN
                        .requestMatchers("/admin/**")
                        .hasAuthority("ROLE_ADMIN")

                        // 其他全部都要登入
                        .anyRequest()
                        .authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/index.html", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/index.html")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration.getAuthenticationManager();
    }
}