package com.example.topfood2604.controller;

import com.example.topfood2604.entity.GiftProduct;
import com.example.topfood2604.repository.GiftProductRepository;
import com.example.topfood2604.service.S3ImageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final GiftProductRepository giftProductRepository;
    private final S3ImageService s3ImageService;

    public AdminProductController(
            GiftProductRepository giftProductRepository,
            S3ImageService s3ImageService
    ) {
        this.giftProductRepository = giftProductRepository;
        this.s3ImageService = s3ImageService;
    }

    @GetMapping
    public String list(Model model) {

        model.addAttribute(
                "products",
                giftProductRepository.findAllByOrderByCreatedAtDesc()
        );

        return "admin/products";
    }

    @GetMapping("/new")
    public String newProduct(Model model) {

        model.addAttribute("product", new GiftProduct());
        model.addAttribute("mode", "create");

        return "admin/product-form";
    }

    @PostMapping("/create")
    public String create(
            @ModelAttribute GiftProduct product,
            @RequestParam("imageFile") MultipartFile imageFile
    ) throws Exception {

        product.setStatus("ON");

        GiftProduct savedProduct =
                giftProductRepository.save(product);

        if (!imageFile.isEmpty()) {

            S3ImageService.ImageResult imageResult =
                    s3ImageService.uploadProductImage(
                            savedProduct.getId(),
                            imageFile
                    );

            savedProduct.setImageUrl(imageResult.imageUrl());

            giftProductRepository.save(savedProduct);
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            Model model
    ) {

        GiftProduct product =
                giftProductRepository.findById(id)
                        .orElseThrow();

        model.addAttribute("product", product);
        model.addAttribute("mode", "edit");

        return "admin/product-form";
    }

    @PostMapping("/update/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute GiftProduct form,
            @RequestParam("imageFile") MultipartFile imageFile
    ) throws Exception {

        GiftProduct product =
                giftProductRepository.findById(id)
                        .orElseThrow();

        product.setName(form.getName());
        product.setDescription(form.getDescription());
        product.setPrice(form.getPrice());
        product.setStock(form.getStock());

        if (!imageFile.isEmpty()) {

            S3ImageService.ImageResult imageResult =
                    s3ImageService.uploadProductImage(
                            product.getId(),
                            imageFile
                    );

            product.setImageUrl(imageResult.imageUrl());
        }

        giftProductRepository.save(product);

        return "redirect:/admin/products";
    }

    @PostMapping("/on/{id}")
    public String on(@PathVariable Long id) {

        GiftProduct product =
                giftProductRepository.findById(id)
                        .orElseThrow();

        product.setStatus("ON");
        giftProductRepository.save(product);

        return "redirect:/admin/products";
    }

    @PostMapping("/off/{id}")
    public String off(@PathVariable Long id) {

        GiftProduct product =
                giftProductRepository.findById(id)
                        .orElseThrow();

        product.setStatus("OFF");
        giftProductRepository.save(product);

        return "redirect:/admin/products";
    }
}