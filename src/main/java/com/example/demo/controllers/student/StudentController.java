package com.example.demo.controllers.student;

import com.example.demo.controllers.Course.CourseResponse;
import com.example.demo.models.StudentsModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor

public class StudentController {
    private final StudentService studentService;

    @PostMapping("/update/{id}")
    public ResponseEntity<StudentsModel> updateStudent(@PathVariable("id") int id,  @RequestHeader("Authorization") String authToken,@RequestBody StudentRequest studentRequest) {
        return ResponseEntity.ok(studentService.updateStudent(id,authToken,studentRequest));
    }

    @GetMapping("/viewprofile/{id}")
    public ResponseEntity<StudentsModel> getStudent(@PathVariable("id") int id,  @RequestHeader("Authorization") String authToken) {
        return ResponseEntity.ok(studentService.viewStudentByID(id, authToken));
    }

    @GetMapping("/getcourses/{id}")
    public ResponseEntity<List<CourseEnrolmentResponse>> getCourses(@PathVariable int id,
                                                           @RequestHeader("Authorization") String authToken) {
        return ResponseEntity.ok(studentService.viewEnrolledCourses(id,authToken));
    }

    @GetMapping("/graduation/{id}")
    public ResponseEntity<GraduationResponse> graduate(@PathVariable int id, @RequestHeader("Authorization") String authToken) {
        return ResponseEntity.ok(studentService.checkGraduateStudent(id,authToken));
    }

}
