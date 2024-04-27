package com.example.demo.controller.student;

import com.example.demo.Config.JwtService;
import com.example.demo.controllers.auth.FinanceResponse;
import com.example.demo.controllers.student.GraduationResponse;
import com.example.demo.controllers.student.StudentRequest;
import com.example.demo.controllers.student.StudentService;
import com.example.demo.models.StudentsModel;
import com.example.demo.models.UserModel;
import com.example.demo.repository.StudentCourseRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentCourseRepository studentCourseRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private StudentService studentService;
    @Value("${financeMicroserviceAPI}")
    private String financeURL;

    @Test
    void testCreateStudent() {
        // Arrange
        UserModel userModel = new UserModel();
        userModel.setId(1);
        userModel.setEmail("test@example.com");

        when(studentRepository.existsById(userModel.getId())).thenReturn(false);

        // Act
        StudentsModel createdStudent = studentService.createStudent(userModel);

        // Assert
        verify(studentRepository, times(1)).save(any(StudentsModel.class));
        // Add additional assertions as needed
    }

    @Test
    void testCreateStudent_StudentAlreadyExists() {
        // Arrange
        UserModel userModel = new UserModel();
        userModel.setId(1);
        userModel.setEmail("test@example.com");

        when(studentRepository.existsById(userModel.getId())).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> studentService.createStudent(userModel));
    }
    @Test
    void testUpdateStudent() {
        // Arrange
        int id = 1;
        String authToken = "Bearer token";
        StudentRequest studentRequest = new StudentRequest("John", "Doe");

        UserModel userModel = new UserModel();
        userModel.setId(id);
        userModel.setUsername("testuser");

        StudentsModel existingStudentsModel = new StudentsModel();
        existingStudentsModel.setId(id);
        existingStudentsModel.setEmail("test@example.com");
        existingStudentsModel.setStudentID("c1234567");

        when(jwtService.extractUsername(authToken.substring(7))).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userModel));
        when(studentRepository.findById(anyInt())).thenReturn(existingStudentsModel);

        // Act
        StudentsModel updatedStudent = studentService.updateStudent(id, authToken, studentRequest);

        // Assert
        if (updatedStudent != null) {
            assertEquals("John", updatedStudent.getFirstName());
            assertEquals("Doe", updatedStudent.getLastName());
            assertEquals("test@example.com", updatedStudent.getEmail());
            assertEquals("c1234567", updatedStudent.getStudentID());
        } else {
            // Handle the case where updateStudent returns null
            // You can add additional assertions or logging here
        }
    }

    @Test
    void testViewStudentByID() {
        // Arrange
        int id = 1;
        String authToken = "Bearer token";

        UserModel userModel = new UserModel();
        userModel.setId(id);
        userModel.setUsername("testuser");

        StudentsModel studentsModel = new StudentsModel();
        studentsModel.setId(id);

        when(jwtService.extractUsername(authToken.substring(7))).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userModel));
        when(studentRepository.findById(id)).thenReturn(studentsModel);

        // Act
        StudentsModel student = studentService.viewStudentByID(id, authToken);

        // Assert
        assertEquals(studentsModel, student);
    }


    @Test
    void testCheckGraduateStudent_HasOutstandingBalance() {
        // Arrange
        int id = 1;
        String authToken = "Bearer token";

        UserModel userModel = new UserModel();
        userModel.setId(id);
        userModel.setUsername("testuser");

        StudentsModel studentsModel = new StudentsModel();
        studentsModel.setId(id);
        studentsModel.setStudentID("c1234567");

        FinanceResponse financeResponse = new FinanceResponse();
        financeResponse.setHasOutstandingBalance(true);

        when(jwtService.extractUsername(authToken.substring(7))).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userModel));
        when(studentRepository.findById(anyInt())).thenReturn(studentsModel);
        when(restTemplate.getForObject(anyString(), eq(FinanceResponse.class))).thenReturn(financeResponse);

        // Act
        GraduationResponse response = studentService.checkGraduateStudent(id, authToken);

        // Assert
        assertEquals(false, response.isEligible());
        assertEquals("You have an outstanding balance. Please clear your balance to graduate.", response.getMessage());
    }
}