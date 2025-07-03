package com.elearn.service;

import com.elearn.model.*;
import com.elearn.repo.*;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
	@Autowired
    private  EnrollmentRepository enrollmentRepo;
	@Autowired
    private  UserRepository userRepo;
	@Autowired
    private  CourseRepository courseRepo;

    public Enrollment enrollInCourse(Long studentId, Long courseId) {
        User student = userRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        if (student.getRole() != User.Role.STUDENT) {
            throw new RuntimeException("Only students can enroll in courses");
        }

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Optional<Enrollment> existing = enrollmentRepo.findByStudentAndCourse(student, course);
        if (existing.isPresent()) {
            throw new RuntimeException("Already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setProgress(0.0);

        return enrollmentRepo.save(enrollment);
    }

    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        User student = userRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return enrollmentRepo.findByStudent(student);
    }

    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return enrollmentRepo.findByCourse(course);
    }

    public Enrollment updateProgress(Long enrollmentId, double progress) {
        Enrollment enrollment = enrollmentRepo.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setProgress(progress);
        return enrollmentRepo.save(enrollment);
    }
}
