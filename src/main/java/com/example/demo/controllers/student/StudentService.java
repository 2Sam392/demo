package com.example.demo.controllers.student;

import com.example.demo.Config.JwtService;
import com.example.demo.controllers.Course.Account;
import com.example.demo.controllers.Course.CourseResponse;
import com.example.demo.controllers.Course.StudentCourseRequest;
import com.example.demo.controllers.Course.StudentCourseResponse;
import com.example.demo.controllers.auth.FinanceResponse;
import com.example.demo.models.CourseModel;
import com.example.demo.models.StudentCourseModel;
import com.example.demo.models.StudentsModel;
import com.example.demo.models.UserModel;
import com.example.demo.repository.StudentCourseRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final StudentCourseRepository studentCourseRepository;

    @Value("${financeMicroserviceAPI}")
    private String financeURL;

    public StudentsModel createStudent(UserModel userModel) {

        if (studentRepository.existsById(userModel.getId())) {
            throw new RuntimeException("Student already exists");
        }

        Faker faker = new Faker();

        StudentsModel studentsModel = new StudentsModel();
        studentsModel.setEmail(userModel.getEmail());
        studentsModel.setCreatedAt(LocalDateTime.now());
        studentsModel.setUpdatedAt(LocalDateTime.now());
        studentsModel.setStudentID("c" + faker.regexify("[a-zA-Z0-9]{7}"));

        return studentRepository.save(studentsModel);

    }

    public StudentsModel updateStudent(int id, String authToken, StudentRequest studentRequest) {
        // Validate token and retrieve authentication details
        String username = jwtService.extractUsername(authToken.substring(7));

        Optional<UserModel> user = userRepository.findByUsername(username);
        StudentsModel studentsModel = studentRepository.findById(id);
        int userid = user.get().getId();
        int student_id = studentsModel.getId();

        // Check if user is authenticated
        if (username != null && user.isPresent() && studentsModel != null && studentsModel.getId() == user.get().getId()) {
            studentsModel.setFirstName(studentRequest.getFirstname());
            studentsModel.setLastName(studentRequest.getLastname());
            studentsModel.setUpdatedAt(LocalDateTime.now());
            return studentRepository.save(studentsModel);
        } else {
            // Unauthorized or resource not found
            throw new RuntimeException("Unauthorized or resource not found");
        }
    }

    public StudentsModel viewStudentByID(int id, String authToken) {
        // Validate token and retrieve authentication details
        String username = jwtService.extractUsername(authToken.substring(7));

        Optional<UserModel> user = userRepository.findByUsername(username);
        StudentsModel studentsModel = studentRepository.findById(id);

        // Check if user is authenticated
        if (username != null && user.isPresent() && studentsModel != null && studentsModel.getId() == user.get().getId()) {
            StudentsModel student = studentsModel;
            student.getId();
            student.getStudentID();
            student.getLastName();
            student.getFirstName();
            return student;
        } else {
            // Unauthorized or resource not found
            throw new RuntimeException("Unauthorized or resource not found");
        }
    }

    public List<CourseEnrolmentResponse> viewEnrolledCourses(int Id, String authToken) {
        String username = jwtService.extractUsername(authToken.substring(7));

        Optional<UserModel> user = userRepository.findByUsername(username);
        int userId = user.get().getId();

        if (username == null || !user.isPresent() || userId != Id) {
            throw new RuntimeException("Unauthorized or resource not found");
        }

        StudentsModel student = studentRepository.findById(Id);

        List<StudentCourseModel> enrollments = studentCourseRepository.findByStudent(student);

        return enrollments.stream().map(enrollment -> {
            CourseEnrolmentResponse response = new CourseEnrolmentResponse();
            response.setCourseName(enrollment.getCourse().getCourseName());
            response.setCourseDescription(enrollment.getCourse().getCourseDescription());
            response.setCourseFee(enrollment.getCourse().getCourseFee());
            response.setReference(enrollment.getReference());
            return response;
        }).collect(Collectors.toList());

    }

    public GraduationResponse checkGraduateStudent(int Id, String authToken) {
        String username = jwtService.extractUsername(authToken.substring(7));

        Optional<UserModel> user = userRepository.findByUsername(username);
        int userId = user.get().getId();

        if (username == null || !user.isPresent() || userId != Id) {
            throw new RuntimeException("Unauthorized or resource not found");
        }

        StudentsModel student = studentRepository.findById(Id);

        RestTemplate restTemplate = new RestTemplate();


        FinanceResponse financeResponse =
                restTemplate.getForObject(financeURL + "/accounts/student/" + student.getStudentID(), FinanceResponse.class);

        if (financeResponse != null && financeResponse.isHasOutstandingBalance()) {
            return new GraduationResponse(false, "You have an outstanding balance. Please clear your balance to " +
                    "graduate.");
        } else {
            return new GraduationResponse(true, "You are eligible to graduate.");
        }

    }

}