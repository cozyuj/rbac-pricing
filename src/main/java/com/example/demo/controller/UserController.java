package com.example.demo.controller;

import com.example.demo.domain.Plan;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.dto.UserReq;
import com.example.demo.dto.UserRes;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/new")
    public ResponseEntity<?> createUser(@RequestBody UserReq req) {
        UserRes res = userService.createUser(req);
        URI location = URI.create("/api/v1/users/" + res.getId());
        return ResponseEntity.created(location).body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserReq req) {
        return ResponseEntity.ok(userService.updateUser(req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<?> updateUserRoleAndPlan(
            @PathVariable Long id,
            @RequestParam Role role,
            @RequestParam Plan plan) {

        userService.changeUserRoleAndPlan(id, role, plan);
        return ResponseEntity.ok("사용자 권한 및 요금제가 변경되었습니다.");
    }
}
