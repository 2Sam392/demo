package com.example.demo.controllers.auth;
import com.example.demo.Config.JwtService;
import com.example.demo.controllers.student.StudentService;
import com.example.demo.models.Role;
import com.example.demo.models.UserModel;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordencoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final StudentRepository studentRepository;
    private final StudentService studentService;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = UserModel.builder()
                .username(request.getUsername())
                .role(Role.STUDENT)
                .email(request.getEmail())
                .password(passwordencoder.encode(request.getPassword()))
                .lastLogin(java.time.LocalDateTime.now())
                .createdAt(java.time.LocalDateTime.now())
                .build();

       userRepository.save(user);

       studentRepository.save(studentService.createStudent(user));

        var jwtToken =jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .username(user.getUsername())
                .id(user.getId())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        UserModel user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        var jwtToken =jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .username(user.getUsername())
                .id(user.getId())
                .build();
    }
}
