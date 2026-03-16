package com.example.demo.dto;

import lombok.Data;
import com.example.demo.model.Role;

@Data
public class RegisterRequest {

    private String name;
    private String email;
    private String password;
    private Role role;
}