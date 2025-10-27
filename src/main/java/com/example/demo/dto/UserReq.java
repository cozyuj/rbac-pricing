package com.example.demo.dto;

import com.example.demo.domain.Plan;
import com.example.demo.domain.Role;
import lombok.Builder;
import lombok.Data;

@Data
public class UserReq {
    private String username;
    private Role role;
    private Plan plan;

    @Builder
    public UserReq(String username, Role role, Plan plan) {
        this.username = username;
        this.role = role;
        this.plan = plan;
    }
}
