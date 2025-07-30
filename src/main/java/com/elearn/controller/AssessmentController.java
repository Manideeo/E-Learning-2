package com.elearn.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.elearn.model.Assessment;
import com.elearn.model.Option;
import com.elearn.model.Question;
import com.elearn.repo.AssessmentRepository;
import com.elearn.repo.QuestionRepository;
import com.elearn.service.AssessmentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;
import java.util.*;

@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
public class AssessmentController {
	@Autowired
	private AssessmentService assessmentService;

	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private AssessmentRepository assessmentRepository;

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
//	@GetMapping("/questions/{assessmentId}")
//	public ResponseEntity<?> getQuestions(@PathVariable Long assessmentId) {
//	    return ResponseEntity.ok(questionRepository.findByAssessment_AssessmentId(assessmentId));
//	}

	@GetMapping("/questions/{assessmentId}")
	public ResponseEntity<?> getQuestions(@PathVariable Long assessmentId) {
	    List<Question> questions = questionRepository.findByAssessment_AssessmentId(assessmentId);
	 
	    List<Map<String, Object>> response = new ArrayList<>();
	 
	    for (Question question : questions) {
	        Map<String, Object> questionData = new HashMap<>();
	        questionData.put("id", question.getId());
	        questionData.put("questionText", question.getQuestionText());
	        questionData.put("correctAnswer", question.getCorrectAnswer());
	 
	        List<String> optionsList = new ArrayList<>();
	        if (question.getOptions() != null) {
	            for (Option option : question.getOptions()) {
	                optionsList.add(option.getOptionText()); // Only the text, not the ID
	            }
	        }
	 
	        questionData.put("options", optionsList);
	        response.add(questionData);
	    }
	 
	    return ResponseEntity.ok(response);
	}
}

//package com.elearn.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.elearn.model.Assessment;
//import com.elearn.model.Option;
//import com.elearn.model.Question;
//import com.elearn.repo.AssessmentRepository;
//import com.elearn.repo.QuestionRepository;
//import com.elearn.service.AssessmentService;
//
//import java.util.List;
//import java.util.*;
//
//@RestController
//@RequestMapping("/api/assessments")
//@RequiredArgsConstructor
//public class AssessmentController {
//    
//    @Autowired
//    private AssessmentService assessmentService;
//
//    @Autowired
//    private QuestionRepository questionRepository;
//    
//    @Autowired
//    private AssessmentRepository assessmentRepository;
//
//    @PostMapping("/add/{courseId}/{instructorId}")
//    public ResponseEntity<?> addAssessment(@RequestBody Assessment assessment,
//                                           @PathVariable Long courseId,
//                                           @PathVariable Long instructorId) {
//        try {
//            Assessment saved = assessmentService.createAssessment(assessment, courseId, instructorId);
//            
//            // Create a clean response to avoid circular references
//            Map<String, Object> response = new HashMap<>();
//            response.put("assessmentId", saved.getAssessmentId());
//            response.put("maxScore", saved.getMaxScore());
//            response.put("questionsCount", saved.getQuestions() != null ? saved.getQuestions().size() : 0);
//            response.put("message", "Assessment created successfully");
//            
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            Map<String, String> errorResponse = new HashMap<>();
//            errorResponse.put("error", e.getMessage());
//            return ResponseEntity.badRequest().body(errorResponse);
//        }
//    }
//
//    @GetMapping("/by-course/{courseId}")
//    public ResponseEntity<?> getByCourse(@PathVariable Long courseId) {
//        try {
//            List<Assessment> assessments = assessmentService.getAssessmentsByCourse(courseId);
//            
//            List<Map<String, Object>> response = new ArrayList<>();
//            for (Assessment assessment : assessments) {
//                Map<String, Object> assessmentData = new HashMap<>();
//                assessmentData.put("assessmentId", assessment.getAssessmentId());
//                assessmentData.put("maxScore", assessment.getMaxScore());
//                assessmentData.put("questionsCount", assessment.getQuestions() != null ? assessment.getQuestions().size() : 0);
//                response.add(assessmentData);
//            }
//            
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
//        }
//    }
//
//    @GetMapping("/questions/{assessmentId}")
//    public ResponseEntity<?> getQuestions(@PathVariable Long assessmentId) {
//        try {
//            List<Question> questions = questionRepository.findByAssessment_AssessmentId(assessmentId);
//         
//            List<Map<String, Object>> response = new ArrayList<>();
//         
//            for (Question question : questions) {
//                Map<String, Object> questionData = new HashMap<>();
//                questionData.put("id", question.getId());
//                questionData.put("questionText", question.getQuestionText());
//                questionData.put("correctAnswer", question.getCorrectAnswer());
//         
//                List<Map<String, Object>> optionsList = new ArrayList<>();
//                if (question.getOptions() != null) {
//                    for (Option option : question.getOptions()) {
//                        Map<String, Object> optionData = new HashMap<>();
//                        optionData.put("id", option.getId());
//                        optionData.put("optionText", option.getOptionText());
//                        optionsList.add(optionData);
//                    }
//                }
//         
//                questionData.put("options", optionsList);
//                response.add(questionData);
//            }
//         
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
//        }
//    }
//
//    @GetMapping("/{assessmentId}")
//    public ResponseEntity<?> getAssessmentById(@PathVariable Long assessmentId) {
//        try {
//            Optional<Assessment> assessmentOpt = assessmentRepository.findById(assessmentId);
//            if (!assessmentOpt.isPresent()) {
//                return ResponseEntity.notFound().build();
//            }
//            
//            Assessment assessment = assessmentOpt.get();
//            Map<String, Object> response = new HashMap<>();
//            response.put("assessmentId", assessment.getAssessmentId());
//            response.put("maxScore", assessment.getMaxScore());
//            response.put("questionsCount", assessment.getQuestions() != null ? assessment.getQuestions().size() : 0);
//            
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
//        }
//    }
//}