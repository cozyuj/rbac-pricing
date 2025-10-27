package com.example.demo.service;

import com.example.demo.domain.*;
import com.example.demo.dto.ProjectReq;
import com.example.demo.dto.ProjectRes;
import com.example.demo.exception.ApiException;
import com.example.demo.exception.ErrorHandling;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ProjectRes createProject(ProjectReq req) {
        log.info("createProject 호출, 요청 데이터: {}", req);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            log.error("Authentication 정보가 없습니다.");
            throw new ApiException(ErrorHandling.FORBIDDEN_EXCEPTION);
        }
        if (!(authentication.getPrincipal() instanceof CustomUserDetails)) {
            log.error("Principal 타입이 CustomUserDetails가 아님: {}", authentication.getPrincipal().getClass());
            throw new ApiException(ErrorHandling.FORBIDDEN_EXCEPTION);
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        String role = userDetails.getAuthorities().iterator().next().getAuthority(); // ROLE_A 등

        log.info("로그인한 사용자 ID: {}, Role: {}", userId, role);

        User createdBy = userRepository.findById(userId)
                .orElseThrow(() -> {
                            log.error("사용자({})를 찾을 수 없음", userId);
                            return new ApiException(ErrorHandling.NOT_FOUND_EXCEPTION);
                        });

        // 요청에서 지정한 프로젝트 소유자
        User owner = userRepository.findById(req.getOwnerId())
                .orElseThrow(() -> {
                    log.error("프로젝트 소유자({})를 찾을 수 없음", req.getOwnerId());
                    return new ApiException(ErrorHandling.NOT_FOUND_EXCEPTION);
                });

        // Plan 기반 프로젝트 개수 제한
        long projectCount = projectRepository.countByOwnerId(owner.getId());
        if ((owner.getPlan() == Plan.BASIC && projectCount >= 1) ||
                (owner.getPlan() == Plan.PRO && projectCount >= 5)) {
            log.info("Owner {}의 현재 프로젝트 수: {}", owner.getUsername(), projectCount);
            throw new ApiException(ErrorHandling.TOO_MANY_REQUEST_EXCEPTION);
        }

        // Role 체크 (권한 없는 사용자는 프로젝트 생성 불가)
        if (!List.of(Role.A, Role.B, Role.C, Role.D).contains(createdBy.getRole())) {
            log.warn("권한 없는 사용자가 프로젝트 생성 시도: {}", createdBy.getRole());
            throw new ApiException(ErrorHandling.FORBIDDEN_EXCEPTION);
        }

        // 프로젝트 이름 중복 체크
        if (projectRepository.existsByName(req.getName())) {
            log.warn("이미 존재하는 프로젝트 이름: {}", req.getName());
            throw new ApiException(ErrorHandling.DUPLICATE_REQUEST);
        }

        // 프로젝트 생성
        Project project = new Project();
        project.setName(req.getName());
        project.setDeadline(req.getDeadline());
        project.setOwner(owner);
        project.setCreatedBy(createdBy);

        Project saved = projectRepository.save(project);

        log.info("프로젝트 생성 완료, ID: {}", saved.getId());

        // 응답 생성
        return ProjectRes.builder()
                .id(saved.getId())
                .name(saved.getName())
                .deadline(saved.getDeadline())
                .ownerName(saved.getOwner().getUsername())
                .createdByName(saved.getCreatedBy().getUsername())
                .memberNames(saved.getMembers().stream().map(User::getUsername).toList())
                .ongoingTaskCount(saved.getTasks() == null ? 0 :
                        (int) saved.getTasks().stream()
                                .filter(t -> t.getStatus() != TaskStatus.DONE)
                                .count())
                .status(saved.getStatus().name())
                .createdAt(saved.getCreateAt().toLocalDateTime())
                .build();
    }

    @PreAuthorize("hasAnyRole('A', 'B')")
    public ProjectRes updateProject(Long projectId,  ProjectReq req) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(ErrorHandling.NOT_FOUND_EXCEPTION));

        if (req.getDeadline() != null) project.setDeadline(req.getDeadline());
        if (req.getName() != null) project.setName(req.getName());

        if (req.getStatus() != null) project.setStatus(ProjectStatus.valueOf(req.getStatus()));

        if (req.getOwnerId() != null) {
            User newOwner = userRepository.findById(req.getOwnerId())
                    .orElseThrow(() -> new ApiException(ErrorHandling.NOT_FOUND_EXCEPTION));
            project.setOwner(newOwner);
        }

        projectRepository.save(project);

        long ongoingCount = taskRepository.countOngoingTasks(projectId, TaskStatus.DONE);
        return toProjectRes(project, ongoingCount);
    }


    @PreAuthorize("hasAnyRole('A', 'B', 'C')")
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ApiException(ErrorHandling.NOT_FOUND_EXCEPTION));

        projectRepository.delete(project);
    }


    public ProjectRes toProjectRes(Project project) {
        long ongoingCount = project.getTasks().stream()
                .filter(t -> t.getStatus() == TaskStatus.ONGOING)
                .count();
        return toProjectRes(project, ongoingCount);
    }

    public ProjectRes toProjectRes(Project project, long ongoingCount) {
        return ProjectRes.builder()
                .id(project.getId())
                .name(project.getName())
                .deadline(project.getDeadline())
                .ownerName(project.getOwner().getUsername())
                .createdByName(project.getCreatedBy().getUsername())
                .memberNames(project.getMembers().stream().map(User::getUsername).toList())
                .ongoingTaskCount((int) ongoingCount)
                .status(project.getStatus().name())
                .createdAt(project.getCreateAt().toLocalDateTime())
                .build();
    }

    @PreAuthorize("hasAnyRole('A', 'D')")
    @Transactional(readOnly = true)
    public ProjectRes getProjectDetail(Long id) {
        log.info("===== getProjectDetail 시작 - 프로젝트 ID: {} =====", id);

        // 1. 인증 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails)) {
            log.error("인증 정보가 없거나 CustomUserDetails가 아닙니다. Principal: {}",
                    auth != null ? auth.getPrincipal() : "null");
            throw new ApiException(ErrorHandling.FORBIDDEN_EXCEPTION);
        }

        CustomUserDetails loginUser = (CustomUserDetails) auth.getPrincipal();
        Long loginUserId = loginUser.getId();
        Role role = loginUser.getRole();

        log.info("프로젝트 상세조회 요청 - 사용자 ID: {}, Role: {}, 프로젝트 ID: {}",
                loginUserId, role, id);

        // 2. 프로젝트 조회
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("프로젝트를 찾을 수 없습니다. ID: {}", id);
                    return new ApiException(ErrorHandling.NOT_FOUND_EXCEPTION);
                });

        // 3. 권한 체크: Role A 또는 D이거나, 프로젝트 소속이면 허용
        boolean isProjectMember = project.getOwner().getId().equals(loginUserId) ||
                project.getMembers().stream()
                        .anyMatch(member -> member.getId().equals(loginUserId));

        if (!(role == Role.A || role == Role.D || isProjectMember)) {
            log.warn("프로젝트 조회 권한 없음 - 사용자 ID: {}, Role: {}, 프로젝트 ID: {}",
                    loginUserId, role, id);
            throw new ApiException(ErrorHandling.FORBIDDEN_EXCEPTION);
        }

        // 4. 태스크 카운트 계산
        int totalTaskCount = project.getTasks() != null ? project.getTasks().size() : 0;
        long ongoingTaskCount = project.getTasks() != null ?
                project.getTasks().stream()
                        .filter(task -> task.getStatus() != TaskStatus.DONE)
                        .count() : 0;

        // 5. 응답 생성
        ProjectRes res = ProjectRes.builder()
                .id(project.getId())
                .name(project.getName())
                .deadline(project.getDeadline())
                .status(project.getStatus().name())
                .ownerName(project.getOwner().getUsername())
                .createdByName(project.getCreatedBy().getUsername())
                .memberNames(project.getMembers().stream()
                        .map(User::getUsername)
                        .toList())
                .ongoingTaskCount((int) ongoingTaskCount)  // 중복 제거
                .createdAt(project.getCreateAt().toLocalDateTime())
                .build();

        log.info("프로젝트 상세조회 완료 - 프로젝트 ID: {}, 이름: {}, 진행중 태스크: {}/{}",
                res.getId(), res.getName(), ongoingTaskCount, totalTaskCount);

        return res;
    }

}
