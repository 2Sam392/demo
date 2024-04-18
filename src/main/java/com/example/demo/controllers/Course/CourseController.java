package com.example.demo.controllers.Course;
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
            @PathVariable("id") int id,
            @RequestHeader("Authorization") String authToken,
            @RequestBody CourseRequest courseRequest) {
        return ResponseEntity.ok(courseService.enrolStudent(id,authToken,courseRequest));
    }

}
