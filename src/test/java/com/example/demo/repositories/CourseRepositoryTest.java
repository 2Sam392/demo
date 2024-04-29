package com.example.demo.repositories;
import com.example.demo.models.CourseModel;
import com.example.demo.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    private CourseModel course1;
    private CourseModel course2;

    @BeforeEach
    void setUp() {
        course1 = new CourseModel(1, "Course 1", "Description 1", 100.0, LocalDateTime.now(), LocalDateTime.now());
        course2 = new CourseModel(2, "Course 2", "Description 2", 200.0, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void testFindById() {
        // Arrange
        courseRepository.save(course1);
        courseRepository.save(course2);

        // Act
        CourseModel foundCourse = courseRepository.findById(course1.getId());

        // Assert
        assertThat(foundCourse).isNotNull();
        assertThat(foundCourse.getId()).isEqualTo(course1.getId());
        assertThat(foundCourse.getCourseName()).isEqualTo(course1.getCourseName());
        assertThat(foundCourse.getCourseDescription()).isEqualTo(course1.getCourseDescription());
        assertThat(foundCourse.getCourseFee()).isEqualTo(course1.getCourseFee());
    }

    @Test
    void testFindByIdWhenNotFound() {
        // Arrange
        courseRepository.save(course1);
        courseRepository.save(course2);
        int nonExistentId = 999;

        // Act
        CourseModel foundCourse = courseRepository.findById(nonExistentId);

        // Assert
        assertThat(foundCourse).isNull();
    }

    @Test
    void testFindByCourseName() {
        // Arrange
        courseRepository.save(course1);
        courseRepository.save(course2);

        // Act
        CourseModel foundCourse = courseRepository.findByCourseName("Course 1");

        // Assert
        assertThat(foundCourse).isNotNull();
        assertThat(foundCourse.getId()).isEqualTo(course1.getId());
        assertThat(foundCourse.getCourseName()).isEqualTo(course1.getCourseName());
        assertThat(foundCourse.getCourseDescription()).isEqualTo(course1.getCourseDescription());
        assertThat(foundCourse.getCourseFee()).isEqualTo(course1.getCourseFee());
    }

    @Test
    void testFindByCourseNameWhenNotFound() {
        // Arrange
        courseRepository.save(course1);
        courseRepository.save(course2);

        // Act
        CourseModel foundCourse = courseRepository.findByCourseName("Non-Existent Course");

        // Assert
        assertThat(foundCourse).isNull();
    }
}