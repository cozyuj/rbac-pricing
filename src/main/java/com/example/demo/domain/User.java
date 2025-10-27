package com.example.demo.domain;

import com.example.demo.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // A/B/C/D

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Plan plan; // BASIC/PRO

    // 유저가 생성한 프로젝트 (OneToMany 양방향)
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects = new ArrayList<>();

    @ManyToMany(mappedBy = "members")
    private List<Project> joinedProjects = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy")
    private List<Project> createdProjects = new ArrayList<>();
}
