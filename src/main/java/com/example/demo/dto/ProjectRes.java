package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectRes {
    private Long id;
    private String name;
    private LocalDateTime deadline;
    private String ownerName;
    private String createdByName;
    private List<String> memberNames;
    private int ongoingTaskCount;
    private String status;
    private LocalDateTime createdAt;

    @Builder
    public ProjectRes(Long id, String name, LocalDateTime deadline, String ownerName, String createdByName, List<String> memberNames, int ongoingTaskCount, String status, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.deadline = deadline;
        this.ownerName = ownerName;
        this.createdByName = createdByName;
        this.memberNames = memberNames;
        this.ongoingTaskCount = ongoingTaskCount;
        this.status = status;
        this.createdAt = createdAt;
    }
}
