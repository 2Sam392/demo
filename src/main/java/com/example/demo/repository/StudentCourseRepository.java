package com.example.demo.repository;

import com.example.demo.models.CourseModel;
import com.example.demo.models.StudentCourseModel;
import com.example.demo.models.StudentsModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentCourseRepository extends JpaRepository<StudentCourseModel,Integer> {


    StudentCourseModel findByCourseAndStudent(CourseModel courseModel, StudentsModel studentModel);
}
