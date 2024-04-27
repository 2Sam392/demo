package com.example.demo.repositories;

import com.example.demo.models.StudentsModel;
import com.example.demo.repository.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ImportAutoConfiguration
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    private StudentsModel student;

    @BeforeEach
    void setUp() {
        student = StudentsModel.builder()
                .id(1)
                .email("john.doe@example.com")
                .studentID("c1234567")
                .firstName("John")
                .lastName("Doe")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldSaveStudent() {
        // Save student
        StudentsModel savedStudent = studentRepository.save(student);
        // Assert that saved student is not null
        assertThat(savedStudent).isNotNull();
    }

}