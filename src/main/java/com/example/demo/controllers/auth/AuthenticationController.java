package com.example.demo.controllers.auth;

import com.example.demo.Config.ResponseDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<ResponseDefinition<AuthenticationResponse>> register(
            @RequestBody RegisterRequest request
    ) {
       ResponseDefinition<AuthenticationResponse> response = service.register(request);
       if (response.isSuccess()) {
           return ResponseEntity.ok(response);
       } else {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                   .body(response);
       }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ResponseDefinition<AuthenticationResponse>> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        ResponseDefinition<AuthenticationResponse> response = service.authenticate(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }
}
