package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectReq {
    private String name;
    private LocalDateTime deadline;
    private String status;
    private Long ownerId;
    private Long createdById;

    @Builder
    public ProjectReq(String name, LocalDateTime deadline, String status, Long ownerId) {
        this.name = name;
        this.deadline = deadline;
        this.status = status;
        this.ownerId = ownerId;
    }
}
