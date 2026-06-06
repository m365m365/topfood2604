package com.example.topfood2604.repository;

import com.example.topfood2604.entity.MemberCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberCartItemRepository
        extends JpaRepository<MemberCartItem, Long> {

    List<MemberCartItem> findByMemberId(Long memberId);

    Optional<MemberCartItem> findByMemberIdAndProductId(
            Long memberId,
            Long productId
    );

    @Transactional
    @Modifying
    void deleteByMemberId(Long memberId);
}