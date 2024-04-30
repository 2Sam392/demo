package com.example.demo.controller.student;
import com.example.demo.Config.JwtService;
import com.example.demo.Config.ResponseDefinition;
import com.example.demo.Config.SecurityConfiguration;
import com.example.demo.controllers.student.*;
import com.example.demo.models.StudentsModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(StudentController.class)

public class StudentControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtService jwtService;

    @MockBean
    private StudentService studentService;

    @Test
    public void testUpdateStudent() throws Exception {
        StudentsModel student = new StudentsModel();
        student.setId(1);
        student.setFirstName("John");

        ResponseDefinition<StudentsModel> response = new ResponseDefinition<>(true, "Success", student);

        when(studentService.updateStudent(eq(1), any(), any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/student/update/1")
                        .header("Authorization", "dummy-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StudentRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("John"));
    }

    @Test
    public void testViewStudentByID() throws Exception {
        StudentsModel student = new StudentsModel();
        student.setId(1);
        student.setFirstName("John Doe");

        ResponseDefinition<StudentsModel> response = new ResponseDefinition<>(true, "Success", student);

        when(studentService.viewStudentByID(eq(1), any())).thenReturn(response);

        mockMvc.perform(get("/api/v1/student/viewprofile/1")
                        .header("Authorization", "dummy-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("John Doe"));
    }

    @Test
    public void testViewEnrolledCourses() throws Exception {
        List<CourseEnrolmentResponse> courses = new ArrayList<>();
        courses.add(new CourseEnrolmentResponse());

        ResponseDefinition<List<CourseEnrolmentResponse>> response = new ResponseDefinition<>(true, "Success", courses);

        when(studentService.viewEnrolledCourses(eq(1), any())).thenReturn(response);

        mockMvc.perform(get("/api/v1/student/getcourses/1")
                        .header("Authorization", "dummy-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(1));
    }
    @Test
    void testCheckGraduateStudent_HasOutstandingBalance() throws Exception {
        int id = 1;
        String authToken = "Bearer token";

        GraduationResponse graduationResponse = new GraduationResponse(false, "You have an outstanding balance. Please clear your balance to graduate.");

        given(studentService.checkGraduateStudent(anyInt(), anyString())).willReturn(graduationResponse);

        mockMvc.perform(get("/api/v1/student/graduation/{id}", id)
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eligible").value(false))
                .andExpect(jsonPath("$.message").value("You have an outstanding balance. Please clear your balance to graduate."));
    }
}