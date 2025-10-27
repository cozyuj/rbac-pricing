package com.example.demo.controller;

import com.example.demo.dto.ProjectReq;
import com.example.demo.dto.ProjectRes;
import com.example.demo.exception.ApiException;
import com.example.demo.exception.ErrorHandling;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "프로젝트 API", description = "프로젝트 관련 Controller")
@RestController
@RequestMapping("/api/v1/projects")
@Slf4j
public class ProjectContoroller {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "프로젝트 생성하기", description = "프로젝트 생성 (중복 불가) \n" +
            "프로젝트 이름 중복 체크")
    @PostMapping("/new")
    public ResponseEntity<?> createProject(@RequestBody ProjectReq project) {
        try {
            ProjectRes created = projectService.createProject(project);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            throw new ApiException(ErrorHandling.BADREQUEST_EXCEPTION);
        }
    }

    @Operation(summary = "프로젝트 상세조회", description = "프로젝트 상세 \n" +
            " 진행 중 태스크 개수")
    @GetMapping("/{id}")
    public ResponseEntity<?> getProject(@PathVariable Long id) {
        log.info("===== Controller 진입! 프로젝트 ID: {} =====", id);
        ProjectRes res = projectService.getProjectDetail(id);
        log.info("===== Controller 응답 반환 =====");
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "프로젝트 수정", description = "마감일, 상태, 담당자 변경 가능")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id,
                                           @RequestBody ProjectReq project) {
        try {
            ProjectRes updated = projectService.updateProject(id, project);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            throw new ApiException(ErrorHandling.FORBIDDEN_EXCEPTION);
        }
    }

    @Operation(summary = "프로젝트 삭제", description = "프로젝트 삭제 권한 없을시 삭제 불가")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        try {
            projectService.deleteProject(id);
            return ResponseEntity.ok(Map.of("message", "Project deleted"));
        } catch (RuntimeException e) {
            throw new ApiException(ErrorHandling.FORBIDDEN_EXCEPTION);
        }
    }


}
