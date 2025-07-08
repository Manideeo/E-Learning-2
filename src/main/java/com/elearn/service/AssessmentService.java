package com.elearn.service;

import com.elearn.model.Assessment;
import com.elearn.model.Course;
import com.elearn.repo.AssessmentRepository;
import com.elearn.repo.CourseRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AssessmentService {
	@Autowired
	private AssessmentRepository assessmentRepository;
	@Autowired
	private CourseRepository courseRepository;

	public Assessment createAssessment(Assessment assessment, Long courseId) {
		Optional<Course> courseOptional = courseRepository.findById(courseId);
		if (courseOptional.isEmpty()) {
			throw new NoSuchElementException("Course not found with ID: " + courseId);
		}
		Course course = courseOptional.get();
		assessment.setCourse(course);
		return assessmentRepository.save(assessment);
	}

	public List<Assessment> getAssessmentsByCourse(Long courseId) {

		if (!courseRepository.existsById(courseId)) {
			throw new NoSuchElementException("Course not found with ID: " + courseId);
		}

		return assessmentRepository.findByCourse_CourseId(courseId);
	}
}