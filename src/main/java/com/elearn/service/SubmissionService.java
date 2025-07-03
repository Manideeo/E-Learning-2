//package com.elearn.service;
//
//import com.elearn.model.*;
//import com.elearn.repo.*;
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class SubmissionService {
//    @Autowired
//    private  SubmissionRepository submissionRepository;
//    @Autowired
//    private  AssessmentRepository assessmentRepository;
//    @Autowired
//    private  UserRepository userRepository;
//
//    public Submission submitAssessment(Long studentId, Long assessmentId, int score) {
//        User student = userRepository.findById(studentId).orElseThrow();
//        Assessment assessment = assessmentRepository.findById(assessmentId).orElseThrow();
//
//        Submission submission = new Submission();
//        submission.setStudent(student);
//        submission.setAssessment(assessment);
//        submission.setScore(score);
//              
//
//        return submissionRepository.save(submission);
//    }
//
//    public List<Submission> getSubmissionsByStudent(Long studentId) {
//        return submissionRepository.findAll().stream()
//                .filter(s -> s.getStudent().getId().equals(studentId))
//                .toList();
//    }
//}
package com.elearn.service;

import com.elearn.model.*;
import com.elearn.repo.*;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException; // Import this for orElseThrow()

@Service
@RequiredArgsConstructor
public class SubmissionService {
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;
    @Autowired
    private UserRepository userRepository;

    public Submission submitAssessment(Long studentId, Long assessmentId, int score) {
        User student = userRepository.findById(studentId).orElseThrow(() -> new NoSuchElementException("Student not found"));
        Assessment assessment = assessmentRepository.findById(assessmentId).orElseThrow(() -> new NoSuchElementException("Assessment not found"));

        Submission submission = new Submission();
        submission.setStudent(student);
        submission.setAssessment(assessment);
        submission.setScore(score);

        return submissionRepository.save(submission);
    }

    public List<Submission> getSubmissionsByStudent(Long studentId) {
        // --- THIS IS THE CRUCIAL CHANGE ---
        User student = userRepository.findById(studentId).orElseThrow(() -> new NoSuchElementException("Student not found"));
        // --- END OF CRUCIAL CHANGE ---

        return submissionRepository.findAll().stream()
                .filter(s -> s.getStudent().getId().equals(studentId))
                .toList();
    }
}
