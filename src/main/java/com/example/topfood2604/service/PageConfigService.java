package com.example.topfood2604.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageConfigService {

    public List<String> getSubMenu(String page) {

        switch (page) {

            case "search":
                return List.of(
                        "依地區搜尋餐廳",
                        "依美食類型搜尋",
                        "依價格範圍搜尋",
                        "AI 智慧推薦餐廳"
                );

            case "business":
                return List.of(
                        "餐廳優惠活動",
                        "美食展覽資訊",
                        "合作商家活動",
                        "品牌聯名推薦"
                );

            case "forum":
                return List.of(
                        "美食心得分享",
                        "新店討論",
                        "排隊名店交流",
                        "地雷店避雷"
                );
            case "member-center":
                return List.of(
                        "我的推薦餐廳",
                        "我的按讚紀錄",
                        "會員等級",
                        "每日使用次數"
                );

            case "about":
                return List.of(
                        "平台介紹",
                        "功能特色",
                        "開發理念",
                        "聯絡我們"
                );
            default:
                return List.of();
        }
    }
}
