package com.example.demo.repository;

import com.example.demo.models.StudentsModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository <StudentsModel,Integer> {
}
