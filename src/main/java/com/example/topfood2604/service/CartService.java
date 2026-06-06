package com.example.topfood2604.service;

import com.example.topfood2604.dto.CartItemDto;
import com.example.topfood2604.entity.GiftProduct;
import com.example.topfood2604.entity.MemberCartItem;
import com.example.topfood2604.repository.GiftProductRepository;
import com.example.topfood2604.repository.MemberCartItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final MemberCartItemRepository memberCartItemRepository;
    private final GiftProductRepository giftProductRepository;

    public CartService(
            MemberCartItemRepository memberCartItemRepository,
            GiftProductRepository giftProductRepository
    ) {
        this.memberCartItemRepository = memberCartItemRepository;
        this.giftProductRepository = giftProductRepository;
    }

    public void addCartItem(Long memberId, Long productId, Integer quantity) {

        MemberCartItem cartItem =
                memberCartItemRepository
                        .findByMemberIdAndProductId(memberId, productId)
                        .orElse(null);

        if (cartItem == null) {

            cartItem = new MemberCartItem();

            cartItem.setMemberId(memberId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartItem.setCreatedAt(LocalDateTime.now());
            cartItem.setUpdatedAt(LocalDateTime.now());

        } else {

            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setUpdatedAt(LocalDateTime.now());
        }

        memberCartItemRepository.save(cartItem);
    }

    public List<MemberCartItem> getCartItems(Long memberId) {
        return memberCartItemRepository.findByMemberId(memberId);
    }

    public List<CartItemDto> getCartItemDtos(Long memberId) {

        List<MemberCartItem> cartItems =
                memberCartItemRepository.findByMemberId(memberId);

        List<CartItemDto> result = new ArrayList<>();

        for (MemberCartItem cartItem : cartItems) {

            GiftProduct product =
                    giftProductRepository
                            .findById(cartItem.getProductId())
                            .orElse(null);

            if (product == null) {
                continue;
            }

            CartItemDto dto = new CartItemDto();

            dto.setProductId(product.getId());
            dto.setProductName(product.getName());
            dto.setPrice(product.getPrice());
            dto.setImageUrl(product.getImageUrl());
            dto.setStock(product.getStock());
            dto.setQuantity(cartItem.getQuantity());
            dto.setSubtotal(product.getPrice() * cartItem.getQuantity());

            result.add(dto);
        }

        return result;
    }

    public void updateQuantity(Long memberId, Long productId, Integer quantity) {

        MemberCartItem cartItem =
                memberCartItemRepository
                        .findByMemberIdAndProductId(memberId, productId)
                        .orElse(null);

        if (cartItem == null) {
            return;
        }

        if (quantity <= 0) {
            memberCartItemRepository.delete(cartItem);
            return;
        }

        cartItem.setQuantity(quantity);
        cartItem.setUpdatedAt(LocalDateTime.now());

        memberCartItemRepository.save(cartItem);
    }

    public void deleteItem(Long memberId, Long productId) {

        MemberCartItem cartItem =
                memberCartItemRepository
                        .findByMemberIdAndProductId(memberId, productId)
                        .orElse(null);

        if (cartItem != null) {
            memberCartItemRepository.delete(cartItem);
        }
    }

    public void clearCart(Long memberId) {
        memberCartItemRepository.deleteByMemberId(memberId);
    }
}