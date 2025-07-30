package com.elearn.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

	@PostMapping("/submit/{studentId}/{assessmentId}/{score}")
	public ResponseEntity<?> submitAssessment(@PathVariable Long studentId, @PathVariable Long assessmentId,
			@PathVariable int score) {
		Submission submission = submissionService.submitAssessment(studentId, assessmentId, score);
		return ResponseEntity.ok(submission);
	}

	@GetMapping("/by-student/{studentId}")
	public List<Submission> getSubmissions(@PathVariable Long studentId) {
		return submissionService.getSubmissionsByStudent(studentId);
	}
}