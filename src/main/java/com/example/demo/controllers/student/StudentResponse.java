package com.example.demo.controllers.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {
    private int id;
    private String studentID;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isAccountUpdated;
}
