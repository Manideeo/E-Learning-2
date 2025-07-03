package com.elearn.service;

import com.elearn.model.Course;
import com.elearn.model.User;
import com.elearn.repo.CourseRepository;
import com.elearn.repo.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    public Course addCourse(Long instructorId, Course course) {
        User instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

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
        User instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        return courseRepository.findByInstructor(instructor);
    }

    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public void deleteCourse(Long courseId) {
        courseRepository.deleteById(courseId);
    }
}

