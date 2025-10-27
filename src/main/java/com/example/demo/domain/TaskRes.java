package com.example.demo.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskRes {
    private Long id;
    private String name;
    private TaskStatus status;
}