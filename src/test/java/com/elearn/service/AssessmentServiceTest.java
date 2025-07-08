
package com.elearn.service;

import com.elearn.model.Assessment;
import com.elearn.model.Course;
import com.elearn.repo.AssessmentRepository;
import com.elearn.repo.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
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

    @InjectMocks
    private AssessmentService assessmentService;

    private Course testCourse;
    private Course anotherCourse;
    private Assessment testAssessment;

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
        testAssessment.setCourse(testCourse); // Initially set for convenience, but service will set it
    }

    @Test
    void testCreateAssessment_Success() {
        when(courseRepository.findById(testCourse.getCourseId())).thenReturn(Optional.of(testCourse));
        when(assessmentRepository.save(any(Assessment.class))).thenReturn(testAssessment);

        Assessment createdAssessment = assessmentService.createAssessment(new Assessment(), testCourse.getCourseId());

        assertNotNull(createdAssessment);
        assertEquals(testAssessment.getAssessmentId(), createdAssessment.getAssessmentId());
        assertEquals(testCourse.getCourseId(), createdAssessment.getCourse().getCourseId());
        assertEquals(testAssessment.getType(), createdAssessment.getType());
        assertEquals(testAssessment.getMaxScore(), createdAssessment.getMaxScore());

    }

    @Test
    void testCreateAssessment_CourseNotFound() {
        Long nonExistentCourseId = 999L;
        when(courseRepository.findById(nonExistentCourseId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            assessmentService.createAssessment(new Assessment(), nonExistentCourseId);
        });

      
    }

    @Test
    void testGetAssessmentsByCourse_Success() {
        Assessment assessment2 = new Assessment();
        assessment2.setAssessmentId(2L);
        assessment2.setType("Assignment");
        assessment2.setMaxScore(50);
        assessment2.setCourse(testCourse);

        Assessment assessment3 = new Assessment();
        assessment3.setAssessmentId(3L);
        assessment3.setType("Quiz");
        assessment3.setMaxScore(20);
        assessment3.setCourse(anotherCourse); 

        List<Assessment> allAssessments = Arrays.asList(testAssessment, assessment2, assessment3);
        when(assessmentRepository.findAll()).thenReturn(allAssessments);

        List<Assessment> result = assessmentService.getAssessmentsByCourse(testCourse.getCourseId());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(testAssessment));
        assertTrue(result.contains(assessment2));
        assertFalse(result.contains(assessment3));

    }

    @Test
    void testGetAssessmentsByCourse_NoAssessmentsFoundForCourse() {
        Assessment assessment3 = new Assessment();
        assessment3.setAssessmentId(3L);
        assessment3.setType("Quiz");
        assessment3.setMaxScore(20);
        assessment3.setCourse(anotherCourse);

        List<Assessment> allAssessments = Arrays.asList(assessment3);
        when(assessmentRepository.findAll()).thenReturn(allAssessments);

        List<Assessment> result = assessmentService.getAssessmentsByCourse(testCourse.getCourseId());

        assertNotNull(result);
        assertTrue(result.isEmpty());

      
    }

    @Test
    void testGetAssessmentsByCourse_NoAssessmentsAtAll() {
        when(assessmentRepository.findAll()).thenReturn(Arrays.asList());

        List<Assessment> result = assessmentService.getAssessmentsByCourse(testCourse.getCourseId());

        assertNotNull(result);
        assertTrue(result.isEmpty());

        
    }
}
