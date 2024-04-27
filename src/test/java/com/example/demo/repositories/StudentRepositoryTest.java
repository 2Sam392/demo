package com.example.demo.repositories;

import com.example.demo.controllers.student.StudentService;
import com.example.demo.models.StudentsModel;
import com.example.demo.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentRepositoryTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void testFindById() {
        // Arrange
        int id = 1;
        StudentsModel expectedStudent = StudentsModel.builder()
                .id(id)
                .email("test@example.com")
                .studentID("c1234567")
                .firstName("John")
                .lastName("Doe")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        when(studentRepository.findById(id)).thenReturn(expectedStudent);

        // Act
        StudentsModel actualStudentOptional = studentRepository.findById(id);

        // Assert
        assertEquals(expectedStudent, actualStudentOptional);
    }
}

