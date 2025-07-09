package com.elearn.service;

import com.elearn.model.Assessment;
import com.elearn.model.Course;
import com.elearn.model.Submission;
import com.elearn.model.User;
import com.elearn.model.User.Role;
import com.elearn.repo.AssessmentRepository;
import com.elearn.repo.SubmissionRepository;
import com.elearn.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubmissionServiceTest {

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SubmissionService submissionService;

    private User studentUser;
    private User instructorUser;
    private Course testCourse;
    private Assessment testAssessment;
    private Submission testSubmission;

    @BeforeEach
    void setUp() {
        studentUser = new User();
        studentUser.setId(1L);
        studentUser.setEmail("student@example.com");
        studentUser.setRole(Role.STUDENT);
        studentUser.setName("Alice");

        instructorUser = new User();
        instructorUser.setId(2L);
        instructorUser.setEmail("instructor@example.com");
        instructorUser.setRole(Role.INSTRUCTOR);
        instructorUser.setName("Prof. Smith");

        testCourse = new Course();
        testCourse.setCourseId(101L);
        testCourse.setTitle("Introduction to Java");

        testAssessment = new Assessment();
        testAssessment.setAssessmentId(201L);
        testAssessment.setType("Quiz");
        testAssessment.setMaxScore(100);
        testAssessment.setCourse(testCourse);

        testSubmission = new Submission();
        testSubmission.setSubmissionId(1L);
        testSubmission.setStudent(studentUser);
        testSubmission.setAssessment(testAssessment);
        testSubmission.setScore(85);
    }

    @Test
    void testSubmitAssessment_Success() {
        when(userRepository.findById(studentUser.getId())).thenReturn(Optional.of(studentUser));
        when(assessmentRepository.findById(testAssessment.getAssessmentId())).thenReturn(Optional.of(testAssessment));
        when(submissionRepository.save(any(Submission.class))).thenReturn(testSubmission);

        Submission submitted = submissionService.submitAssessment(studentUser.getId(), testAssessment.getAssessmentId(), 85);

        assertNotNull(submitted);
        assertEquals(testSubmission.getSubmissionId(), submitted.getSubmissionId());
        assertEquals(studentUser.getId(), submitted.getStudent().getId());
        assertEquals(testAssessment.getAssessmentId(), submitted.getAssessment().getAssessmentId());
        assertEquals(85, submitted.getScore());

        verify(userRepository).findById(studentUser.getId());
        verify(assessmentRepository).findById(testAssessment.getAssessmentId());
        verify(submissionRepository).save(any(Submission.class));
    }

    @Test
    void testSubmitAssessment_StudentNotFound() {
        Long nonExistentStudentId = 99L;
        when(userRepository.findById(nonExistentStudentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            submissionService.submitAssessment(nonExistentStudentId, testAssessment.getAssessmentId(), 80);
        });

        verify(userRepository).findById(nonExistentStudentId);
        verifyNoInteractions(assessmentRepository);
        verifyNoInteractions(submissionRepository);
    }

    @Test
    void testSubmitAssessment_AssessmentNotFound() {
        Long nonExistentAssessmentId = 999L;
        when(userRepository.findById(studentUser.getId())).thenReturn(Optional.of(studentUser));
        when(assessmentRepository.findById(nonExistentAssessmentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            submissionService.submitAssessment(studentUser.getId(), nonExistentAssessmentId, 70);
        });

        verify(userRepository).findById(studentUser.getId());
        verify(assessmentRepository).findById(nonExistentAssessmentId);
        verifyNoInteractions(submissionRepository);
    }

    @Test
    void testSubmitAssessment_UserNotStudentRole() {
        when(userRepository.findById(instructorUser.getId())).thenReturn(Optional.of(instructorUser));

        assertThrows(IllegalStateException.class, () -> {
            submissionService.submitAssessment(instructorUser.getId(), testAssessment.getAssessmentId(), 50);
        });

        verify(userRepository).findById(instructorUser.getId());
        verifyNoInteractions(assessmentRepository);
        verifyNoInteractions(submissionRepository);
    }

    @Test
    void testGetSubmissionsByStudent_Success() {
        Submission submission2 = new Submission();
        submission2.setSubmissionId(2L);
        submission2.setStudent(studentUser);
        submission2.setAssessment(testAssessment);
        submission2.setScore(90);

        List<Submission> studentSubmissions = Arrays.asList(testSubmission, submission2);
        
        when(submissionRepository.findByStudent_Id(studentUser.getId())).thenReturn(studentSubmissions);

        List<Submission> result = submissionService.getSubmissionsByStudent(studentUser.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(testSubmission));
        assertTrue(result.contains(submission2));

        verify(submissionRepository).findByStudent_Id(studentUser.getId());
        verifyNoInteractions(userRepository);
        verifyNoInteractions(assessmentRepository);
    }

    @Test
    void testGetSubmissionsByStudent_StudentNotFoundInRepoOrNoSubmissions() {
        Long nonExistentStudentId = 99L;
        
        when(submissionRepository.findByStudent_Id(nonExistentStudentId)).thenReturn(Collections.emptyList());

        List<Submission> result = submissionService.getSubmissionsByStudent(nonExistentStudentId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(submissionRepository).findByStudent_Id(nonExistentStudentId);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(assessmentRepository);
    }

    @Test
    void testGetSubmissionsByStudent_NoSubmissionsForStudent() {
        when(submissionRepository.findByStudent_Id(studentUser.getId())).thenReturn(Collections.emptyList());

        List<Submission> result = submissionService.getSubmissionsByStudent(studentUser.getId());

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(submissionRepository).findByStudent_Id(studentUser.getId());
        verifyNoInteractions(userRepository);
        verifyNoInteractions(assessmentRepository);
    }
}