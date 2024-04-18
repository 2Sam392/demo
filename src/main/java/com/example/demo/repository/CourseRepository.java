package com.example.demo.repository;

import com.example.demo.models.StudentCourseModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<StudentCourseModel, Integer> {
}
