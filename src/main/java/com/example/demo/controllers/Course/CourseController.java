package com.example.demo.controllers.Course;
import com.example.demo.Config.ResponseDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/courses")
    public ResponseEntity<ResponseDefinition<List<CourseResponse>>> getCourses(@RequestHeader("Authorization") String authToken) {
        ResponseDefinition<List<CourseResponse>> response = courseService.viewAllCourses(authToken);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    @PostMapping("/enrol/{id}")
    public ResponseEntity<ResponseDefinition<CourseResponse>> addEnrolment(
            @RequestBody CourseRequest courseRequest,
            @PathVariable int id,
            @RequestHeader("Authorization") String authToken) {
        ResponseDefinition<CourseResponse> response = courseService.enrolStudent(courseRequest.getCourseName(), id, authToken);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }


}
