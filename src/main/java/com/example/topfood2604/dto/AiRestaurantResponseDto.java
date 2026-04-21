package com.example.topfood2604.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiRestaurantResponseDto {

    private String name;
    private String url;
    private String photoUrl;
    private String address;
}