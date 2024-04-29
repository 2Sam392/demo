package com.example.demo.controllers.auth;

import com.example.demo.Config.JwtService;
import com.example.demo.Config.ResponseDefinition;
import com.example.demo.controllers.student.StudentService;
import com.example.demo.models.Role;
import com.example.demo.models.StudentsModel;
import com.example.demo.models.UserModel;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor

public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordencoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final StudentRepository studentRepository;
    private final StudentService studentService;


    @Value("${financeMicroserviceAPI}")
    private String financeURL;

    @Value("${libraryMicroserviceAPI}")
    private String libraryURL;

    public ResponseDefinition<AuthenticationResponse> register(RegisterRequest request) {
        try {
            var user = UserModel.builder()
                    .username(request.getUsername())
                    .role(Role.STUDENT)
                    .email(request.getEmail())
                    .password(passwordencoder.encode(request.getPassword()))
                    .lastLogin(java.time.LocalDateTime.now())
                    .createdAt(java.time.LocalDateTime.now())
                    .build();

            userRepository.save(user);
            studentService.createStudent(user);

            //create a student account in the library and finance microservices
            RestTemplate restTemplate = new RestTemplate();

            StudentsModel studentsModel = studentRepository.findById(user.getId());
            String studentID = studentsModel.getStudentID();
            MicroserviceRequest microserviceRequest = new MicroserviceRequest(studentID);

            HttpEntity<MicroserviceRequest> entity = new HttpEntity<>(microserviceRequest);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ;

            restTemplate.postForObject(financeURL + "/accounts/", entity, FinanceResponse.class);
            restTemplate.postForObject(libraryURL + "/api/register", entity, LibraryResponse.class);


            var jwtToken = jwtService.generateToken(user);
            AuthenticationResponse res = AuthenticationResponse.builder()
                    .token(jwtToken)
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .id(user.getId())
                    .build();

            return new ResponseDefinition<>(true, "User registered successfully", res);
        } catch (Exception e) {
            return new ResponseDefinition<>(false, "User registration failed", null);
        }

    }

    public ResponseDefinition<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            UserModel user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow();

            var jwtToken = jwtService.generateToken(user);
            AuthenticationResponse res = AuthenticationResponse.builder()
                    .token(jwtToken)
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .id(user.getId())
                    .build();

            return new ResponseDefinition<>(true, "Authentication successful", res);
        } catch (Exception e) {
            return new ResponseDefinition<>(false, "Authentication failed", null);
        }
    }
}
