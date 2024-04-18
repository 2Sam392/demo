package com.example.demo.controllers.student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseService {
    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentPortal";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";



    // Method to retrieve all available courses
    public void viewAllCourses() {
        try {
            // Establishing database connection
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            // SQL query to retrieve all courses
            String sql = "SELECT * FROM courses";

            // Creating PreparedStatement object
            PreparedStatement statement = connection.prepareStatement(sql);

            // Executing the query
            ResultSet resultSet = statement.executeQuery();

            // Displaying the results
            System.out.println("Available Courses:");
            while (resultSet.next()) {
                int courseId = resultSet.getInt("id");
                String courseName = resultSet.getString("name");
                String courseDescription = resultSet.getString("description");
                // You can add more fields as per your database schema
                System.out.println("Course ID: " + courseId + ", Name: " + courseName + ", Description: " + courseDescription);
            }

            // Closing resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Creating an instance of CourseService and calling the method to view all courses
        CourseService courseService = new CourseService();
        courseService.viewAllCourses();
    }
}

