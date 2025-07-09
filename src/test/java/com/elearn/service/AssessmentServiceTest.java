package com.elearn.service;

import com.elearn.model.Assessment;
import com.elearn.model.Course;
import com.elearn.model.User;
import com.elearn.model.User.Role;
import com.elearn.repo.AssessmentRepository;
import com.elearn.repo.CourseRepository;
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
public class AssessmentServiceTest {

    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AssessmentService assessmentService;

    private Course testCourse;
    private Course anotherCourse;
    private Assessment testAssessment;
    private User instructorUser;
    private User studentUser;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setCourseId(101L);
        testCourse.setTitle("Introduction to Java");

        anotherCourse = new Course();
        anotherCourse.setCourseId(102L);
        anotherCourse.setTitle("Data Structures");

        testAssessment = new Assessment();
        testAssessment.setAssessmentId(1L);
        testAssessment.setType("Quiz");
        testAssessment.setMaxScore(100);

        instructorUser = new User();
        instructorUser.setId(10L);
        instructorUser.setEmail("instructor@example.com");
        instructorUser.setRole(Role.INSTRUCTOR);

        studentUser = new User();
        studentUser.setId(20L);
        studentUser.setEmail("student@example.com");
        studentUser.setRole(Role.STUDENT); // Ensure this is definitely Role.STUDENT
    }

    @Test
    void testCreateAssessment_Success() {
        Assessment newAssessment = new Assessment();
        newAssessment.setType("Quiz");
        newAssessment.setMaxScore(100);

        when(userRepository.findById(instructorUser.getId())).thenReturn(Optional.of(instructorUser));
        when(courseRepository.findById(testCourse.getCourseId())).thenReturn(Optional.of(testCourse));
        
        when(assessmentRepository.save(any(Assessment.class))).thenAnswer(invocation -> {
            Assessment savedAssessment = invocation.getArgument(0);
            savedAssessment.setAssessmentId(1L);
            savedAssessment.setCourse(testCourse);
            return savedAssessment;
        });

        Assessment createdAssessment = assessmentService.createAssessment(newAssessment, testCourse.getCourseId(), instructorUser.getId());

        assertNotNull(createdAssessment);
        assertEquals(1L, createdAssessment.getAssessmentId());
        assertEquals(testCourse.getCourseId(), createdAssessment.getCourse().getCourseId());
        assertEquals("Quiz", createdAssessment.getType());
        assertEquals(100, createdAssessment.getMaxScore());
    }

    @Test
    void testCreateAssessment_CourseNotFound() {
        Long nonExistentCourseId = 999L;
        
        when(userRepository.findById(instructorUser.getId())).thenReturn(Optional.of(instructorUser));
        when(courseRepository.findById(nonExistentCourseId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            assessmentService.createAssessment(new Assessment(), nonExistentCourseId, instructorUser.getId());
        });
    }

    @Test
    void testCreateAssessment_InstructorNotFound() {
        Long nonExistentInstructorId = 999L;
        when(userRepository.findById(nonExistentInstructorId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            assessmentService.createAssessment(new Assessment(), testCourse.getCourseId(), nonExistentInstructorId);
        });
    }

   

    @Test
    void testGetAssessmentsByCourse_Success() {
        Assessment assessment2 = new Assessment();
        assessment2.setAssessmentId(2L);
        assessment2.setType("Assignment");
        assessment2.setMaxScore(50);
        assessment2.setCourse(testCourse);

        List<Assessment> assessmentsForTestCourse = Arrays.asList(testAssessment, assessment2);
        when(assessmentRepository.findByCourse_CourseId(testCourse.getCourseId())).thenReturn(assessmentsForTestCourse);

        List<Assessment> result = assessmentService.getAssessmentsByCourse(testCourse.getCourseId());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(testAssessment));
        assertTrue(result.contains(assessment2));
    }

    @Test
    void testGetAssessmentsByCourse_NoAssessmentsFoundForCourse() {
        when(assessmentRepository.findByCourse_CourseId(testCourse.getCourseId())).thenReturn(Collections.emptyList());

        List<Assessment> result = assessmentService.getAssessmentsByCourse(testCourse.getCourseId());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAssessmentsByCourse_CourseWithNoAssessmentsOrNonExistent() {
        Long nonExistentCourseId = 999L;
        when(assessmentRepository.findByCourse_CourseId(nonExistentCourseId)).thenReturn(Collections.emptyList());

        List<Assessment> result = assessmentService.getAssessmentsByCourse(nonExistentCourseId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}