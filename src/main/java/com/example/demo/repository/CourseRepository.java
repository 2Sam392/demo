package com.example.demo.repository;

import com.example.demo.models.CourseModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<CourseModel, Integer> {

    CourseModel findById(int Id);
    CourseModel findByCourseName(String CourseName);


}
