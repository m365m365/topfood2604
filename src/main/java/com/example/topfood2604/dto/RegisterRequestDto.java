package com.example.topfood2604.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDto {

    @NotBlank(message = "Email 不可空白")
    @Email(message = "Email 格式不正確")
    private String email;

    @NotBlank(message = "密碼不可空白")
    @Size(min = 6, message = "密碼長度至少 6 碼")
    private String password;

    @NotBlank(message = "確認密碼不可空白")
    private String confirmPassword;

    private String name;
}