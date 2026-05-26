package com.example.topfood2604.repository;

import com.example.topfood2604.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository
        extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Member> findByVerificationToken(String verificationToken);

    Optional<Member> findByResetPasswordToken(String resetPasswordToken);
}