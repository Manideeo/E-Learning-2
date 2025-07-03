package com.elearn.service;

import com.elearn.model.*;
import com.elearn.repo.*;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssessmentService {
     @Autowired
    private  AssessmentRepository assessmentRepository;
     @Autowired
    private  CourseRepository courseRepository;

    public Assessment createAssessment(Assessment assessment, Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        assessment.setCourse(course);
        return assessmentRepository.save(assessment);
    }

    public List<Assessment> getAssessmentsByCourse(Long courseId) {
        return assessmentRepository.findAll().stream()
                .filter(a -> a.getCourse().getCourseId().equals(courseId))
                .toList();
    }
}
