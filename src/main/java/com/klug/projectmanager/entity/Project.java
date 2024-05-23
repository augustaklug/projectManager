package com.klug.projectmanager.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToMany
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<User> teamMembers;
}

