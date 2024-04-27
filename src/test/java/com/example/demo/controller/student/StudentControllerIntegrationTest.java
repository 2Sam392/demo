package com.example.demo.controller.student;

import com.example.demo.controllers.student.StudentController;
import com.example.demo.controllers.student.StudentService;
import com.example.demo.controllers.student.StudentRequest;
import com.example.demo.controllers.student.GraduationResponse;
import com.example.demo.controllers.student.CourseEnrolmentResponse;
import com.example.demo.Config.JwtService;
import com.example.demo.models.StudentsModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringJUnitConfig
@WebMvcTest(StudentController.class)
public class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private JwtService jwtService; // Mock JwtService bean

    @BeforeEach
    void setUp() {
        // Mocking behavior for StudentService methods
        Mockito.when(studentService.viewStudentByID(Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(createTestStudent());
        Mockito.when(studentService.checkGraduateStudent(Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(new GraduationResponse(true, "You are eligible to graduate."));
        Mockito.when(studentService.viewEnrolledCourses(Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(createTestEnrolledCourses());
    }

    @Test
    public void testGetStudent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/student/viewprofile/1")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void testCheckGraduateStudent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/student/graduation/1")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.eligible").value(true));
    }

    @Test
    public void testGetEnrolledCourses() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/student/getcourses/1")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].courseName").value("Test Course"));
    }

    private StudentsModel createTestStudent() {
        StudentsModel student = new StudentsModel();
        student.setId(1);
        student.setEmail("test@example.com");
        student.setStudentID("1234567");
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());
        return student;
    }

    private List<CourseEnrolmentResponse> createTestEnrolledCourses() {
        List<CourseEnrolmentResponse> courses = new ArrayList<>();
        CourseEnrolmentResponse course = new CourseEnrolmentResponse();
        course.setCourseName("Test Course");
        course.setCourseDescription("This is a test course");
        course.setCourseFee(100);
        course.setReference("REF123");
        courses.add(course);
        return courses;
    }
}
