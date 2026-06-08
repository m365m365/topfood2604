package com.example.topfood2604.controller;

import com.example.topfood2604.dto.CartItemDto;
import com.example.topfood2604.entity.Member;
import com.example.topfood2604.entity.MemberCartItem;
import com.example.topfood2604.repository.MemberRepository;
import com.example.topfood2604.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartApiController {

    private final CartService cartService;
    private final MemberRepository memberRepository;

    public CartApiController(
            CartService cartService,
            MemberRepository memberRepository
    ) {
        this.cartService = cartService;
        this.memberRepository = memberRepository;
    }

    private Long getLoginMemberId(Authentication authentication) {

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("尚未登入");
        }

        String username = authentication.getName();

        Member member =
                memberRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("找不到會員"));

        return member.getId();
    }

    @PostMapping("/add")
    public Map<String, Object> addCart(
            @RequestBody Map<String, Object> request,
            Authentication authentication
    ) {
        Long memberId = getLoginMemberId(authentication);

        Long productId =
                Long.valueOf(request.get("productId").toString());

        Integer quantity =
                Integer.valueOf(request.get("quantity").toString());

        cartService.addCartItem(memberId, productId, quantity);

        return Map.of(
                "success", true,
                "message", "加入成功"
        );
    }

    @GetMapping
    public List<MemberCartItem> getCart(Authentication authentication) {

        Long memberId = getLoginMemberId(authentication);

        return cartService.getCartItems(memberId);
    }

    @GetMapping("/count")
    public Map<String, Object> getCartCount(Authentication authentication) {

        Long memberId = getLoginMemberId(authentication);

        int count = cartService.getCartCount(memberId);

        return Map.of(
                "count", count
        );
    }

    @PostMapping("/update")
    public String updateCart(
            @RequestBody Map<String, Object> request,
            Authentication authentication
    ) {
        Long memberId = getLoginMemberId(authentication);

        Long productId =
                Long.valueOf(request.get("productId").toString());

        Integer quantity =
                Integer.valueOf(request.get("quantity").toString());

        cartService.updateQuantity(memberId, productId, quantity);

        return "success";
    }

    @PostMapping("/delete")
    public String deleteCart(
            @RequestBody Map<String, Object> request,
            Authentication authentication
    ) {
        Long memberId = getLoginMemberId(authentication);

        Long productId =
                Long.valueOf(request.get("productId").toString());

        cartService.deleteItem(memberId, productId);

        return "success";
    }

    @PostMapping("/clear")
    public String clearCart(Authentication authentication) {

        Long memberId = getLoginMemberId(authentication);

        cartService.clearCart(memberId);

        return "success";
    }

    @GetMapping("/detail")
    public List<CartItemDto> getCartDetail(Authentication authentication) {

        Long memberId = getLoginMemberId(authentication);

        return cartService.getCartItemDtos(memberId);
    }
}