package com.elearn.controller;


import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.elearn.model.Submission;
import com.elearn.service.SubmissionService;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {
	@Autowired
    private SubmissionService submissionService;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/{studentId}/{assessmentId}/{score}")
    public Submission submit(@PathVariable Long studentId,
                             @PathVariable Long assessmentId,
                             @PathVariable int score) {
        return submissionService.submitAssessment(studentId, assessmentId, score);
    }

    @PreAuthorize("hasAnyRole('STUDENT', 'INSTRUCTOR')")

    @GetMapping("/by-student/{studentId}")
    public List<Submission> getByStudent(@PathVariable Long studentId) {
        return submissionService.getSubmissionsByStudent(studentId);
    }
}