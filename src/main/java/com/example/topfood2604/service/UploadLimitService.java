package com.example.topfood2604.service;

import com.example.topfood2604.entity.Member;
import com.example.topfood2604.entity.MemberUploadDailyUsage;
import com.example.topfood2604.repository.MemberRepository;
import com.example.topfood2604.repository.MemberUploadDailyUsageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class UploadLimitService {

    private final MemberRepository memberRepository;
    private final MemberUploadDailyUsageRepository usageRepository;

    public UploadLimitService(
            MemberRepository memberRepository,
            MemberUploadDailyUsageRepository usageRepository
    ) {
        this.memberRepository = memberRepository;
        this.usageRepository = usageRepository;
    }

    /**
     * 使用一次上傳機會
     * 回傳剩餘次數
     */
    @Transactional
    public int useOneUploadChance(Long memberId) {

        LocalDate today = LocalDate.now();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("找不到會員"));

        int dailyLimit = member.getTier().getDailyUploadLimit();

        MemberUploadDailyUsage usage = usageRepository
                .findByMemberIdAndUploadDate(memberId, today)
                .orElseGet(() -> {
                    MemberUploadDailyUsage newUsage =
                            new MemberUploadDailyUsage();

                    newUsage.setMember(member);
                    newUsage.setUploadDate(today);
                    newUsage.setUsedCount(0);

                    return newUsage;
                });

        if (usage.getUsedCount() >= dailyLimit) {
            throw new RuntimeException("今日上傳次數已用完");
        }

        usage.setUsedCount(usage.getUsedCount() + 1);

        usageRepository.save(usage);

        return dailyLimit - usage.getUsedCount();
    }

    /**
     * 查詢今天剩餘上傳次數
     */
    public int getTodayRemainingCount(Long memberId) {

        LocalDate today = LocalDate.now();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("找不到會員"));

        int dailyLimit = member.getTier().getDailyUploadLimit();

        int used = usageRepository
                .findByMemberIdAndUploadDate(memberId, today)
                .map(MemberUploadDailyUsage::getUsedCount)
                .orElse(0);

        return dailyLimit - used;
    }

    /**
     * 查詢會員每日上限
     */
    public int getDailyLimit(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("找不到會員"));

        return member.getTier().getDailyUploadLimit();
    }
}