package com.example.demo.controllers.student;

import com.example.demo.models.StudentsModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor

public class StudentController {
    private final StudentService studentService;

    @PostMapping("/update/{id}")
    public ResponseEntity<StudentsModel> updateStudent(@PathVariable("id") int id,  @RequestHeader("Authorization") String authToken,@RequestBody StudentRequest studentRequest) {
        return ResponseEntity.ok(studentService.updateStudent(id,authToken,studentRequest));
    }
}
