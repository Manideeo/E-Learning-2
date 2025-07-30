package com.elearn.service;

import com.elearn.model.Assessment;
import com.elearn.model.Course;
import com.elearn.model.Option;
import com.elearn.model.Question;
import com.elearn.model.User;
import com.elearn.repo.AssessmentRepository;
import com.elearn.repo.CourseRepository;
import com.elearn.repo.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AssessmentService {
	@Autowired
	private AssessmentRepository assessmentRepo;
	@Autowired
	private CourseRepository courseRepo;
	@Autowired
	private UserRepository userRepo;

	public Assessment createAssessment(Assessment assessment, Long courseId, Long instructorId) {
	    User instructor = userRepo.findById(instructorId)
	        .orElseThrow(() -> new RuntimeException("User not found"));
	 
	    if (instructor.getRole() != User.Role.INSTRUCTOR) {
	        throw new IllegalStateException("Only instructors can add assessments");
	    }
	 
	    Course course = courseRepo.findById(courseId)
	        .orElseThrow(() -> new RuntimeException("Course not found"));
	 
	    assessment.setCourse(course);
	 
	    if (assessment.getQuestions() != null) {
	        for (Question q : assessment.getQuestions()) {
	            q.setAssessment(assessment);
	            if (q.getOptions() != null) {
	                for (Option opt : q.getOptions()) {
	                    opt.setQuestion(q);
	                }
	            }
	        }
	    }
	 
	return assessmentRepo.save(assessment);
	}

	public List<Assessment> getAssessmentsByCourse(Long courseId) {
		return assessmentRepo.findByCourse_CourseId(courseId);
	}
}