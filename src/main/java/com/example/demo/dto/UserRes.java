package com.example.demo.dto;

import com.example.demo.domain.Plan;
import com.example.demo.domain.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRes {
    private Long id;
    private String username;
    private Role role;
    private Plan plan;
    private LocalDateTime createdAt;

    @Builder
    public UserRes(Long id, String username, Role role, Plan plan, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.plan = plan;
        this.createdAt = createdAt;
    }
}