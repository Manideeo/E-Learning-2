package com.elearn.controller;

import com.elearn.model.Enrollment;
import com.elearn.service.EnrollmentService;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
	@Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/enroll/{studentId}/{courseId}")
 
    public Enrollment enroll(@PathVariable Long studentId, @PathVariable Long courseId) {
        return enrollmentService.enrollInCourse(studentId, courseId);
    }

    @GetMapping("/by-student/{studentId}")    
    public List<Enrollment> getByStudent(@PathVariable Long studentId) {
        return enrollmentService.getEnrollmentsByStudent(studentId);
    }

    @GetMapping("/by-course/{courseId}")
   
    public List<Enrollment> getByCourse(@PathVariable Long courseId) {
        return enrollmentService.getEnrollmentsByCourse(courseId);
    }

    @PutMapping("/{enrollmentId}/progress/{progress}")
   
    public Enrollment updateProgress(@PathVariable Long enrollmentId, @PathVariable double progress) {
        return enrollmentService.updateProgress(enrollmentId, progress);
    }
}