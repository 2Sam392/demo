package com.example.demo.controller.student;

import com.example.demo.Config.JwtService;
import com.example.demo.Config.ResponseDefinition;
import com.example.demo.controllers.auth.FinanceResponse;
import com.example.demo.controllers.student.GraduationResponse;
import com.example.demo.controllers.student.StudentRequest;
import com.example.demo.controllers.student.StudentService;
import com.example.demo.models.StudentsModel;
import com.example.demo.models.UserModel;
import com.example.demo.repository.StudentCourseRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

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
        UserModel userModel = new UserModel();
        userModel.setId(1);
        userModel.setEmail("test@example.com");

        when(studentRepository.existsById(userModel.getId())).thenReturn(false);
        ResponseDefinition<StudentsModel> createdStudent = studentService.createStudent(userModel);

        verify(studentRepository, times(1)).save(any(StudentsModel.class));
    }


    @Test
    void testCreateStudent_StudentAlreadyExists() {
        UserModel userModel = new UserModel();
        userModel.setId(1);
        userModel.setEmail("test@example.com");

        StudentsModel existingStudent = new StudentsModel();
        existingStudent.setId(userModel.getId());

        when(studentRepository.existsById(userModel.getId())).thenReturn(true);
        ResponseDefinition<StudentsModel> response = studentService.createStudent(userModel);
        assertFalse(response.isSuccess());
        assertEquals("An error occurred", response.getErrorMessage());
        assertNull(response.getData());
    }

    @Test
    void testUpdateStudent() {
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
        ResponseDefinition<StudentsModel> updatedStudent = studentService.updateStudent(id, authToken, studentRequest);

        if (updatedStudent != null) {
            assertEquals("John", updatedStudent.getData().getFirstName());
            assertEquals("Doe", updatedStudent.getData().getLastName());
            assertEquals("test@example.com", updatedStudent.getData().getEmail());
            assertEquals("c1234567", updatedStudent.getData().getStudentID());
        }
    }

    @Test
    void testViewStudentByID() {
        int id = 1;
        String authToken = "Bearer token";

        UserModel userModel = new UserModel();
        userModel.setId(id);
        userModel.setUsername("testuser");

        StudentsModel studentsModel = new StudentsModel();
        studentsModel.setId(id);

        when(jwtService.extractUsername(authToken.substring(7))).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userModel));


        ResponseDefinition<StudentsModel> expectedResponse = ResponseDefinition.<StudentsModel>builder()
                .success(true)
                .errorMessage(null)
                .data(studentsModel)
                .build();

        when(studentRepository.findById(id)).thenReturn(ResponseDefinition.<StudentsModel>builder()
                .success(true)
                .errorMessage(null)
                .data(studentsModel)
                .build().getData());
        ResponseDefinition<StudentsModel> student = studentService.viewStudentByID(id, authToken);
        assertEquals(expectedResponse, student);
    }


    @Test
    void testCheckGraduateStudent_HasOutstandingBalance() throws Exception {
        int id = 1;
        String authToken = "Bearer token";
        String username = "testuser";
        String financeURL = "http://localhost:8081";

        UserModel userModel = new UserModel();
        userModel.setId(id);
        userModel.setUsername(username);

        StudentsModel studentsModel = new StudentsModel();
        studentsModel.setId(id);
        studentsModel.setStudentID("c1234567");

        when(jwtService.extractUsername(authToken.substring(7))).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userModel));
        when(studentRepository.findById(anyInt())).thenReturn(studentsModel);
        FinanceResponse financeResponse = FinanceResponse.builder()
                .id(1L)
                .studentId(studentsModel.getStudentID())
                .hasOutstandingBalance(true)
                .build();
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockServer.expect(requestTo(financeURL + "/accounts/student/" + studentsModel.getStudentID()))
                .andRespond(withSuccess(new ObjectMapper().writeValueAsString(financeResponse), MediaType.APPLICATION_JSON));
        ReflectionTestUtils.setField(studentService, "financeURL", financeURL);
        GraduationResponse response = studentService.checkGraduateStudent(id, authToken);
        assertFalse(response.isEligible());
        assertEquals("You have an outstanding balance. Please clear your balance to graduate.", response.getMessage());
    }

    @Test
    void testCheckGraduateStudent_Unauthorized() {
        int studentId = 1;
        String token = "invalidToken";
        String username = "testuser";

        when(jwtService.extractUsername(token.substring(7))).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        GraduationResponse response = studentService.checkGraduateStudent(studentId, token);
        assertFalse(response.isEligible());
        assertEquals("An error occurred", response.getMessage());
    }
}