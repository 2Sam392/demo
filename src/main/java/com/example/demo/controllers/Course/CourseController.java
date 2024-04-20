package com.example.demo.controllers.Course;
import com.example.demo.models.CourseModel;
import com.example.demo.models.StudentCourseModel;
import com.example.demo.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor

public class CourseController {
    private final CourseService courseService;

    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponse>> getCourses(@RequestHeader("Authorization") String authToken) {
        return ResponseEntity.ok(courseService.viewAllCourses(authToken));
    }

    @PostMapping("/enrol/{id}")
    public ResponseEntity<CourseResponse> addEnrolment(
            @RequestBody CourseRequest courseRequest,
            @PathVariable int id,
            @RequestHeader("Authorization") String authToken) {
        return ResponseEntity.ok(courseService.enrolStudent(courseRequest.getCourseName(), id, authToken));
    }


}
