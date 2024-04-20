package com.example.demo.controllers.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class CourseEnrolmentResponse {
    private String CourseName;
    private String CourseDescription;
    private double CourseFee;
    private String Reference;
}
