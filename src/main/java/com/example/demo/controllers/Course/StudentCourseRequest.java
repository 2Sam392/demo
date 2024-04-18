package com.example.demo.controllers.Course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourseRequest {
    private double amount;
    private LocalDate dueDate= LocalDate.now();
    private String type ="TUITION_FEES";
    private Account account;


}
