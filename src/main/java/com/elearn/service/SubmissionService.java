package com.elearn.service;

import com.elearn.model.*;
import com.elearn.repo.*;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException; // Import this for NoSuchElementException
import java.util.Optional; // Import Optional
@Service
@RequiredArgsConstructor
public class SubmissionService {
	@Autowired
    private  SubmissionRepository submissionRepo;
	@Autowired
    private  AssessmentRepository assessmentRepo;
	@Autowired
    private  UserRepository userRepo;
 
    public Submission submitAssessment(Long studentId, Long assessmentId, int score) {
        User student = userRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("User not found"));
 
        if (student.getRole() != User.Role.STUDENT) {
            throw new IllegalStateException("Only students can submit assessments");
        }
 
        Assessment assessment = assessmentRepo.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));
 
    
        Submission submission = new Submission();
        submission.setStudent(student);
        submission.setAssessment(assessment);
        submission.setScore(score);
 
return submissionRepo.save(submission);
    }
 
    public List<Submission> getSubmissionsByStudent(Long studentId) {
        return submissionRepo.findByStudent_Id(studentId);
    }
    }
//@Service
//@RequiredArgsConstructor
//public class SubmissionService {
//    @Autowired
//    private SubmissionRepository submissionRepository;
//    @Autowired
//    private AssessmentRepository assessmentRepository;
//    @Autowired
//    private UserRepository userRepository;
//
//    public Submission submitAssessment(Long studentId, Long assessmentId, int score) {
//         
//        Optional<User> studentOptional = userRepository.findById(studentId);
//        if (studentOptional.isEmpty()) {  
//            throw new NoSuchElementException("Student not found");
//        }
//        User student = studentOptional.get();
//
//         
//        Optional<Assessment> assessmentOptional = assessmentRepository.findById(assessmentId);
//        if (assessmentOptional.isEmpty()) {  
//            throw new NoSuchElementException("Assessment not found");
//        }
//        Assessment assessment = assessmentOptional.get();
//
//        Submission submission = new Submission();
//        submission.setStudent(student);
//        submission.setAssessment(assessment);
//        submission.setScore(score);
//
//        return submissionRepository.save(submission);
//    }
//
//    public List<Submission> getSubmissionsByStudent(Long studentId) {
//       
//        Optional<User> studentOptional = userRepository.findById(studentId);
//        if (studentOptional.isEmpty()) {  
//            throw new NoSuchElementException("Student not found");
//        }
//        User student = studentOptional.get();  
//        
//        return submissionRepository.findByStudent_Id(student.getId());
//    }
//}
