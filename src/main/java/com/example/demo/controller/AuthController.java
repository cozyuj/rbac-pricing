package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.dto.LoginReq;
import com.example.demo.dto.LoginRes;
import com.example.demo.exception.ApiException;
import com.example.demo.exception.ErrorHandling;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginRes> login(@RequestBody LoginReq req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new ApiException(ErrorHandling.NOT_FOUND_EXCEPTION));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new ApiException(ErrorHandling.ACCESS_DENIED_EXCEPTION);
        }

        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new LoginRes(token));
    }
}
