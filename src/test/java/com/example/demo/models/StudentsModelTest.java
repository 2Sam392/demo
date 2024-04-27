package com.example.demo.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentsModelTest {

    @Test
    void testGettersAndSetters() {
        StudentsModel student = new StudentsModel();
        student.setId(1);
        student.setEmail("test@example.com");
        student.setStudentID("c1234567");
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());

        assertEquals(1, student.getId());
        assertEquals("test@example.com", student.getEmail());
        assertEquals("c1234567", student.getStudentID());
        assertEquals("John", student.getFirstName());
        assertEquals("Doe", student.getLastName());
        // Add more assertions as needed
    }
}