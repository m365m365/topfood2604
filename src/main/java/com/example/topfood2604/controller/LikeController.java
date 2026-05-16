package com.example.topfood2604.controller;

import com.example.topfood2604.entity.Member;
import com.example.topfood2604.repository.MemberRepository;
import com.example.topfood2604.service.LikeService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/recommend")
public class LikeController {

    private final LikeService likeService;
    private final MemberRepository memberRepository;

    public LikeController(
            LikeService likeService,
            MemberRepository memberRepository
    ) {
        this.likeService = likeService;
        this.memberRepository = memberRepository;
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> like(
            @PathVariable Long id,
            Authentication authentication
    ) {

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {

            return ResponseEntity.status(401).body(
                    Map.of(
                            "success", false,
                            "message", "請先登入會員才能按讚"
                    )
            );
        }

        Member member = memberRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        try {
            int newLikeCount = likeService.addLike(
                    member.getId(),
                    id
            );

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message", "按讚成功",
                            "likeCount", newLikeCount
                    )
            );

        } catch (RuntimeException e) {

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message", e.getMessage()
                    )
            );
        }
    }
}