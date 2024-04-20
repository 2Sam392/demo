package com.example.demo.controllers.Course;

import com.example.demo.Config.JwtService;
import com.example.demo.models.CourseModel;
import com.example.demo.models.StudentCourseModel;
import com.example.demo.models.StudentsModel;
import com.example.demo.models.UserModel;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.StudentCourseRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final StudentCourseRepository studentCourseRepository;

    @Value("${financeMicroserviceAPI}")
    private String financeURL;


    // Method to retrieve all available courses
    public List<CourseResponse> viewAllCourses(String authToken) {
        String username = jwtService.extractUsername(authToken.substring(7));

        Optional<UserModel> user = userRepository.findByUsername(username);

        int userid = user.get().getId();


        // Check if user is authenticated
        if (username != null && user.isPresent()) {
            List<CourseModel> courseModels = courseRepository.findAll();
            List<CourseResponse> courseResponses = courseModels.stream()
                    .map(this::CourseModelToResponse)
                    .toList();
            return courseResponses;

        } else {
            // Unauthorized or resource not found
            throw new RuntimeException("Unauthorized or resource not found");
        }
    }

    public CourseResponse enrolStudent(String name, int studentId, String authToken) {
        String username = jwtService.extractUsername(authToken.substring(7));
        Optional<UserModel> user = userRepository.findByUsername(username);
        int userId = user.get().getId();

        if (username == null || !user.isPresent() || userId != studentId) {
            throw new RuntimeException("Unauthorized or resource not found");
        }

        // Fetch the course from the database using the courseId
        CourseModel courseModel = courseRepository.findByCourseName(name);

        if (courseModel == null) {
            return CourseResponse.builder()
                    .Message("Course not found")
                    .build();
        }

        StudentsModel studentModel = studentRepository.findById(studentId);
        if (studentModel == null) {
            return CourseResponse.builder()
                    .Message("Student record is not found")
                    .build();
        }


        if (studentCourseRepository.existsByCourseAndStudent(courseModel, studentModel)) {
            return CourseResponse.builder()
                    .Message("You are already enrolled in this course")
                    .build();
        }

        RestTemplate restTemplate = new RestTemplate();
        StudentCourseRequest studentCourseRequest = new StudentCourseRequest();
        studentCourseRequest.setAmount(courseModel.getCourseFee());

        Account account = new Account();
        account.setStudentId(studentModel.getStudentID());
        studentCourseRequest.setAccount(account);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<StudentCourseRequest> entity = new HttpEntity<>(studentCourseRequest, headers);

        StudentCourseResponse studentCourseResponse = restTemplate.postForObject(financeURL + "/invoices/", entity, StudentCourseResponse.class);

        StudentCourseModel enrollStudent = StudentCourseModel.builder()
                .student(studentModel)
                .course(courseModel)
                .Reference(studentCourseResponse.getReference())
                .createdAt(LocalDateTime.now())
                .build();


        studentCourseRepository.save(enrollStudent);

        return CourseResponse.builder()
                .CourseDescription(courseModel.getCourseDescription())
                .CourseName(courseModel.getCourseName())
                .CourseFee(courseModel.getCourseFee())
                .Reference(enrollStudent.getReference())
                .Message(null)
                .Id(enrollStudent.getId())
                .build();
    }
    private CourseResponse CourseModelToResponse(CourseModel courseModel) {
        return new CourseResponse(
                courseModel.getId(),
                courseModel.getCourseName(),
                courseModel.getCourseDescription(),
                courseModel.getCourseFee(),
                "null",
                null
        );
    }

}

