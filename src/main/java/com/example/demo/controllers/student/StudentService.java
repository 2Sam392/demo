package com.example.demo.controllers.student;

import com.example.demo.Config.JwtService;
import com.example.demo.models.StudentsModel;
import com.example.demo.models.UserModel;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public StudentsModel createStudent(UserModel userModel) {

        if (studentRepository.existsById(userModel.getId())) {
            throw new RuntimeException("Student already exists");
        }

        Faker faker = new Faker();

        StudentsModel studentsModel = new StudentsModel();
        studentsModel.setEmail(userModel.getEmail());
        studentsModel.setCreatedAt(LocalDateTime.now());
        studentsModel.setUpdatedAt(LocalDateTime.now());
        studentsModel.setStudentID("c"+ faker.regexify("[a-zA-Z0-9]{7}"));

        return studentRepository.save(studentsModel);

    }

    public StudentsModel updateStudent(int id, String authToken, StudentRequest studentRequest){
        // Validate token and retrieve authentication details
        String username = jwtService.extractUsername(authToken.substring(7));

        Optional<UserModel> user = userRepository.findByUsername(username);
        StudentsModel studentsModel = studentRepository.findById(id);
        int userid = user.get().getId();
        int student_id = studentsModel.getId();

        // Check if user is authenticated
        if (username != null && user.isPresent() && studentsModel!=null && studentsModel.getId() == user.get().getId()) {
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
        if (username != null && user.isPresent() && studentsModel!=null && studentsModel.getId() == user.get().getId()) {
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
}
