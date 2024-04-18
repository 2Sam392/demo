package com.example.demo.Config;

import com.example.demo.models.CourseModel;
import com.example.demo.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CoursesBuildMisc {

    @Bean
    CommandLineRunner initDatabase(CourseRepository courseRepository) {
        return args -> {
            CourseModel myCourse = new CourseModel();
            myCourse.setCourseName("Java Programming");
            myCourse.setCourseDescription("Learn Java programming language basics");
            myCourse.setCourseFee(90.00);
            myCourse.setCreatedAt(java.time.LocalDateTime.now());
            myCourse.setUpdatedAt(java.time.LocalDateTime.now());
            courseRepository.save(myCourse);

            CourseModel myCourse2 = new CourseModel();
            myCourse2.setCourseName("Spring boot");
            myCourse2.setCourseDescription("Software Engineering for Computing");
            myCourse2.setCourseFee(80.00);
            myCourse2.setCreatedAt(java.time.LocalDateTime.now());
            myCourse2.setUpdatedAt(java.time.LocalDateTime.now());

            courseRepository.save(myCourse2);

            CourseModel myCourse3 = new CourseModel();
            myCourse3.setCourseName("Web Development");
            myCourse3.setCourseDescription("Introduction to web development technologies', 'This course introduces web development technologies such as HTML, CSS, and JavaScript.");
            myCourse3.setCourseFee(30.00);
            myCourse3.setCreatedAt(java.time.LocalDateTime.now());
            myCourse3.setUpdatedAt(java.time.LocalDateTime.now());
            courseRepository.save(myCourse3);

            CourseModel myCourse4 = new CourseModel();
            myCourse4.setCourseName("Data Science");
            myCourse4.setCourseDescription("Foundations of data science and analytics', 'This course provides an overview of data science concepts and techniques.");
            myCourse4.setCourseFee(40.00);
            myCourse4.setCreatedAt(java.time.LocalDateTime.now());
            myCourse4.setUpdatedAt(java.time.LocalDateTime.now());
            courseRepository.save(myCourse4);

            CourseModel myCourse5 = new CourseModel();
            myCourse5.setCourseName("Software Engineering");
            myCourse5.setCourseDescription("Software Engineering for Computing");
            myCourse5.setCourseFee(50.00);
            myCourse5.setCreatedAt(java.time.LocalDateTime.now());
            myCourse5.setUpdatedAt(java.time.LocalDateTime.now());
            courseRepository.save(myCourse5);
        };
    }
}

