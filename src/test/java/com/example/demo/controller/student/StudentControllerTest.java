package com.example.demo.controller.student;

import com.example.demo.Config.ResponseDefinition;
import com.example.demo.controllers.student.CourseEnrolmentResponse;
import com.example.demo.controllers.student.StudentController;
import com.example.demo.controllers.student.StudentService;
import com.example.demo.models.StudentsModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void testGetStudent() {
        int studentId = 1;
        String authToken = "Bearer token";
        StudentsModel expectedStudent = new StudentsModel();
        // Set up the expected student object

        ResponseDefinition<StudentsModel> expectedResponse = ResponseDefinition.<StudentsModel>builder()
                .success(true)
                .errorMessage(null)
                .data(expectedStudent)
                .build();

        when(studentService.viewStudentByID(studentId, authToken)).thenReturn(expectedResponse);

        ResponseEntity<ResponseDefinition<StudentsModel>> response = studentController.getStudent(studentId, authToken);
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testGetCourses() {
        int studentId = 1;
        String authToken = "Bearer token";
        List<CourseEnrolmentResponse> expectedCourses = Arrays.asList(
                new CourseEnrolmentResponse("Course 1", "Description 1", 100.0, "REF001"),
                new CourseEnrolmentResponse("Course 2", "Description 2", 200.0, "REF002")
        );

        ResponseDefinition<List<CourseEnrolmentResponse>> expectedResponse = ResponseDefinition.<List<CourseEnrolmentResponse>>builder()
                .success(true)
                .errorMessage(null)
                .data(expectedCourses)
                .build();

        when(studentService.viewEnrolledCourses(studentId, authToken)).thenReturn(expectedResponse);

        ResponseEntity<ResponseDefinition<List<CourseEnrolmentResponse>>> response = studentController.getCourses(studentId, authToken);
        assertEquals(expectedResponse, response.getBody());
    }


}