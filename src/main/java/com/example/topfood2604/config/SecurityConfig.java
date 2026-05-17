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
        System.out.println("===== SecurityConfig Loaded =====");
        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // =========================
                        // 公開頁面
                        // =========================
                        .requestMatchers(
                                "/index.html",
                                "/register",
                                "/verify-email",
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/navbar.css",
                                "/favicon.ico",

                                // mock 測試頁
                                "/mock-member-wang.html",

                                // 會員排行榜頁
                                "/member-rankings/**",

                                // 推薦詳細頁公開
                                "/recommend-detail",
                                "/recommend/detail/**"
                        ).permitAll()

                        // =========================
                        // 公開 API
                        // =========================
                        .requestMatchers(
                                "/api/homepage",
                                "/api/restaurants/**",
                                "/api/AiRestaurantApi/search",

                                "/api/member/check-login",

                                "/api/recommended-restaurants/home"
                        ).permitAll()

                        // =========================
                        // 按讚 API：需要登入
                        // =========================
                        .requestMatchers(
                                "/api/recommend/*/like"
                        ).authenticated()

                        // =========================
                        // 會員功能：需要登入
                        // =========================
                        .requestMatchers(
                                "/recommend",
                                "/my-recommend"
                        ).authenticated()

                        // =========================
                        // 上傳推薦 API：需要登入
                        // =========================
                        .requestMatchers(
                                "/api/recommended-restaurants"
                        ).authenticated()

                        // =========================
                        // ADMIN
                        // =========================
                        .requestMatchers("/admin/**")
                        .hasAuthority("ROLE_ADMIN")

                        // =========================
                        // 其他全部允許
                        // =========================
                        .anyRequest()
                        .permitAll()
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