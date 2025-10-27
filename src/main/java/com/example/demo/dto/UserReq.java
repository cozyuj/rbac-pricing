package com.example.demo.dto;

import com.example.demo.domain.Plan;
import com.example.demo.domain.Role;
import lombok.Builder;
import lombok.Data;

@Data
public class UserReq {
    private String username;
    private String password;
    private Role role;
    private Plan plan;

    @Builder
    public UserReq(String username, String password, Role role, Plan plan) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.plan = plan;
    }
}
