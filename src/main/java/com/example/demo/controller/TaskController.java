package com.example.demo.controller;

import com.example.demo.domain.TaskReq;
import com.example.demo.domain.TaskRes;
import com.example.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects/{projectId}/tasks")
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskRes> createTask(
            @PathVariable Long projectId,
            @RequestBody TaskReq request) {
        return ResponseEntity.ok(taskService.createTask(projectId, request));
    }

    @GetMapping
    public ResponseEntity<?> getTasks(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getTasks(projectId));
    }
}
