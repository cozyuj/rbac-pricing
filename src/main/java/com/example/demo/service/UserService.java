package com.example.demo.service;

import com.example.demo.domain.CustomUserDetails;
import com.example.demo.domain.Plan;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.dto.UserReq;
import com.example.demo.dto.UserRes;
import com.example.demo.exception.ApiException;
import com.example.demo.exception.ErrorHandling;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('A')")  // Role A만 접근 가능
    public void changeUserRoleAndPlan(Long targetUserId, Role newRole, Plan newPlan) {
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        CustomUserDetails loginUser = (CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        log.info("로그인 사용자: {} {}", loginUser.getUsername(), loginUser.getAuthorities());

        log.info("target User: {}", targetUser.toString());
        if (targetUser.getRole() == Role.A) {
            throw new ApiException(ErrorHandling.ACCESS_DENIED_EXCEPTION);
        }

        // 변경 적용
        targetUser.setRole(newRole);
        targetUser.setPlan(newPlan);

        userRepository.save(targetUser);
    }

    public UserRes createUser(UserReq req) {
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new ApiException(ErrorHandling.DUPLICATE_REQUEST);
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(req.getRole());
        user.setPlan(req.getPlan());

        userRepository.save(user);
        return toRes(user);
    }

    public UserRes getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorHandling.NOT_FOUND_EXCEPTION));
        return toRes(user);
    }

    public List<UserRes> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toRes)
                .collect(Collectors.toList());
    }

    public UserRes updateUser(UserReq req) {
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new ApiException(ErrorHandling.DUPLICATE_REQUEST);
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(req.getRole());
        user.setPlan(req.getPlan());

        User updated = userRepository.save(user);

        return toRes(updated);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ApiException(ErrorHandling.NOT_FOUND_EXCEPTION);
        }
        userRepository.deleteById(id);
    }

    private UserRes toRes(User user) {
        return UserRes.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .plan(user.getPlan())
                .createdAt(user.getCreateAt().toLocalDateTime())
                .build();
    }
}
