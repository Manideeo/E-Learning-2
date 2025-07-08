package com.elearn.service;

import com.elearn.model.Course;
import com.elearn.model.User;
import com.elearn.repo.CourseRepository;
import com.elearn.repo.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional; // Import Optional

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    public Course addCourse(Long instructorId, Course course) {
        Optional<User> instructorOptional = userRepository.findById(instructorId);
        if (instructorOptional.isEmpty()) {
            throw new RuntimeException("Instructor not found");
        }
        User instructor = instructorOptional.get();

        if (instructor.getRole() != User.Role.INSTRUCTOR) {
            throw new RuntimeException("Only instructors can add courses.");
        }

        course.setInstructor(instructor);
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getCoursesByInstructor(Long instructorId) {
        Optional<User> instructorOptional = userRepository.findById(instructorId);
        if (instructorOptional.isEmpty()) {
            throw new RuntimeException("Instructor not found");
        }
        User instructor = instructorOptional.get();

        return courseRepository.findByInstructor(instructor);
    }

    public Course getCourseById(Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty()) {
            throw new RuntimeException("Course not found");
        }
        return courseOptional.get();
    }

    public void deleteCourse(Long courseId) {
        courseRepository.deleteById(courseId);
    }
}