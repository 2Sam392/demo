package com.example.demo.controllers.Course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class CourseResponse {
    private int Id;
    private String CourseName;
    private String CourseDescription;
    private double CourseFee;
    private String Reference;
    private String Message;

    public CourseResponse(int id, String courseName, String courseDescription, double courseFee, String reference) {

    }
}
