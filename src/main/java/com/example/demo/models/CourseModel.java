package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "courses")

public class CourseModel {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int Id;
    @Column(nullable = false, unique = true, length = 25)
    private String CourseName;
    private String CourseDescription;
    private double CourseFee;
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "course")
    private List<StudentCourseModel> studentCourses = new ArrayList<>();

}
