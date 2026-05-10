package com.example.topfood2604.controller;

import com.example.topfood2604.entity.MemberUploadDailyUsage;
import com.example.topfood2604.repository.MemberUploadDailyUsageRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/upload-usage")
public class AdminUploadUsageController {

    private final MemberUploadDailyUsageRepository usageRepository;

    public AdminUploadUsageController(
            MemberUploadDailyUsageRepository usageRepository
    ) {
        this.usageRepository = usageRepository;
    }

    /**
     * 顯示會員每日上傳紀錄
     */
    @GetMapping
    public String list(
            @RequestParam(required = false) String date,
            Model model
    ) {
        List<MemberUploadDailyUsage> usages;

        if (date != null && !date.isBlank()) {
            LocalDate uploadDate = LocalDate.parse(date);
            usages = usageRepository.findByUploadDateOrderByIdDesc(uploadDate);
            model.addAttribute("selectedDate", date);
        } else {
            usages = usageRepository.findAllByOrderByUploadDateDescIdDesc();
            model.addAttribute("selectedDate", "");
        }

        model.addAttribute("usages", usages);

        return "admin/upload-usage";
    }

    /**
     * 重置某筆紀錄 usedCount = 0
     */
    @PostMapping("/{id}/reset")
    public String reset(@PathVariable Long id) {

        MemberUploadDailyUsage usage = usageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到上傳紀錄"));

        usage.setUsedCount(0);

        usageRepository.save(usage);

        return "redirect:/admin/upload-usage";
    }

    /**
     * 手動修改 usedCount
     */
    @PostMapping("/{id}/update")
    public String updateUsedCount(
            @PathVariable Long id,
            @RequestParam Integer usedCount
    ) {
        MemberUploadDailyUsage usage = usageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到上傳紀錄"));

        if (usedCount < 0) {
            usedCount = 0;
        }

        usage.setUsedCount(usedCount);

        usageRepository.save(usage);

        return "redirect:/admin/upload-usage";
    }

    /**
     * 刪除某筆紀錄
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {

        usageRepository.deleteById(id);

        return "redirect:/admin/upload-usage";
    }
}