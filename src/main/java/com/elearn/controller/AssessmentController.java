  package com.elearn.controller;



import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.elearn.model.Assessment;
import com.elearn.service.AssessmentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

//@RestController
//@RequestMapping("/api/assessments")
//@RequiredArgsConstructor
//@SecurityRequirement(name="bearerAuth")
//public class AssessmentController {
//	@Autowired
//    private  AssessmentService assessmentService;
//
//    @PreAuthorize("hasRole('INSTRUCTOR')")
//    @PostMapping("/add/{courseId}")
//    public Assessment create(@RequestBody Assessment assessment, @PathVariable Long courseId) {
//        return assessmentService.createAssessment(assessment, courseId);
//    }
//
//    @PreAuthorize("hasAnyRole('STUDENT', 'INSTRUCTOR')")
//    @GetMapping("/by-course/{courseId}")
//    public List<Assessment> getByCourse(@PathVariable Long courseId) {
//        return assessmentService.getAssessmentsByCourse(courseId);
//    }
//}
@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
public class AssessmentController {
	@Autowired
    private  AssessmentService assessmentService;
 
    @PostMapping("/add/{courseId}/{instructorId}")
    public ResponseEntity<?> addAssessment(@RequestBody Assessment assessment,
                                           @PathVariable Long courseId,
                                           @PathVariable Long instructorId) {
        Assessment saved = assessmentService.createAssessment(assessment, courseId, instructorId);
        return ResponseEntity.ok(saved);
    }
 
    @GetMapping("/by-course/{courseId}")
    public List<Assessment> getByCourse(@PathVariable Long courseId) {
        return assessmentService.getAssessmentsByCourse(courseId);
    }
}