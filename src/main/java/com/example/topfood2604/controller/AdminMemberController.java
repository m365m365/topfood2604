package com.example.topfood2604.controller;

import com.example.topfood2604.entity.Member;
import com.example.topfood2604.entity.MemberTier;
import com.example.topfood2604.repository.MemberRepository;
import com.example.topfood2604.repository.MemberTierRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/members")
public class AdminMemberController {

    private final MemberRepository memberRepository;
    private final MemberTierRepository memberTierRepository;

    public AdminMemberController(MemberRepository memberRepository,
                                 MemberTierRepository memberTierRepository) {
        this.memberRepository = memberRepository;
        this.memberTierRepository = memberTierRepository;
    }

    @GetMapping
    public String membersPage(Model model) {

        List<Member> members = memberRepository.findAll();
        List<MemberTier> tiers = memberTierRepository.findAll();

        model.addAttribute("members", members);
        model.addAttribute("tiers", tiers);

        return "admin/members";
    }

    @PostMapping("/{memberId}/tier")
    public String updateMemberTier(@PathVariable Long memberId,
                                   @RequestParam Long tierId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("找不到會員"));

        MemberTier tier = memberTierRepository.findById(tierId)
                .orElseThrow(() -> new RuntimeException("找不到會員等級"));

        member.setTier(tier);

        memberRepository.save(member);

        return "redirect:/admin/members";
    }
}