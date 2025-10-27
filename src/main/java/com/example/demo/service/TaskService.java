package com.example.demo.service;

import com.example.demo.domain.*;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskRes createTask(Long projectId, TaskReq req) {
        Project findProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        Task task = new Task();
        task.setName(req.getName());
        task.setProject(findProject);
        task.setStatus(TaskStatus.ONGOING);

        Task saved = taskRepository.save(task);

        return TaskRes.builder()
                .id(saved.getId())
                .name(saved.getName())
                .status(saved.getStatus())
                .build();
    }

    public List<TaskRes> getTasks(Long projectId) {
        return taskRepository.findByProjectId(projectId).stream()
                .map(task -> TaskRes.builder()
                        .id(task.getId())
                        .name(task.getName())
                        .status(task.getStatus())
                        .build())
                .toList();
    }
}
