package com.example.topfood2604.repository;

import com.example.topfood2604.entity.MemberTier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberTierRepository extends JpaRepository<MemberTier, Long> {

    Optional<MemberTier> findByCode(String code);
}