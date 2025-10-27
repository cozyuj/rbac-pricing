package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectReq {
    private String name;
    private LocalDateTime deadline;
    private Long ownerId;
    private Long createdById;

    @Builder
    public ProjectReq(String name, LocalDateTime deadline, Long ownerId, Long createdById) {
        this.name = name;
        this.deadline = deadline;
        this.ownerId = ownerId;
        this.createdById = createdById;
    }
}
