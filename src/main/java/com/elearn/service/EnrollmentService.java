package com.elearn.service;

import com.elearn.model.*;
import com.elearn.repo.*;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException; // Consider using NoSuchElementException or custom exceptions instead of RuntimeException

@Service
@RequiredArgsConstructor
public class EnrollmentService {
	@Autowired
	private EnrollmentRepository enrollmentRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private CourseRepository courseRepo;

	public Enrollment enrollInCourse(Long studentId, Long courseId) {
		Optional<User> studentOptional = userRepo.findById(studentId);
		if (studentOptional.isEmpty()) {
			throw new RuntimeException("Student not found");
		}
		User student = studentOptional.get();

		if (student.getRole() != User.Role.STUDENT) {
			throw new RuntimeException("Only students can enroll in courses");
		}

		Optional<Course> courseOptional = courseRepo.findById(courseId);
		if (courseOptional.isEmpty()) {
			throw new RuntimeException("Course not found");
		}
		Course course = courseOptional.get();

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
		Optional<User> studentOptional = userRepo.findById(studentId);
		if (studentOptional.isEmpty()) {
			throw new RuntimeException("Student not found");
		}
		User student = studentOptional.get();

		return enrollmentRepo.findByStudent(student);
	}

	public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
		Optional<Course> courseOptional = courseRepo.findById(courseId);
		if (courseOptional.isEmpty()) {
			throw new RuntimeException("Course not found");
		}
		Course course = courseOptional.get();

		return enrollmentRepo.findByCourse(course);
	}

	public Enrollment updateProgress(Long enrollmentId, double progress) {
		Optional<Enrollment> enrollmentOptional = enrollmentRepo.findById(enrollmentId);
		if (enrollmentOptional.isEmpty()) {
			throw new RuntimeException("Enrollment not found");
		}
		Enrollment enrollment = enrollmentOptional.get();

		enrollment.setProgress(progress);
		return enrollmentRepo.save(enrollment);
	}
}