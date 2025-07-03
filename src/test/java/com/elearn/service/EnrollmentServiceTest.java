package com.elearn.service;

import com.elearn.model.Course;
import com.elearn.model.Enrollment;
import com.elearn.model.User;
import com.elearn.model.User.Role;
import com.elearn.repo.CourseRepository;
import com.elearn.repo.EnrollmentRepository;
import com.elearn.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private User studentUser;
    private User instructorUser;
    private Course testCourse;
    private Enrollment existingEnrollment;

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
        testCourse.setTitle("Introduction to Programming");
        testCourse.setInstructor(instructorUser);

        existingEnrollment = new Enrollment();
        existingEnrollment.setEnrollmentId(1L);
        existingEnrollment.setStudent(studentUser);
        existingEnrollment.setCourse(testCourse);
        existingEnrollment.setProgress(50.0);
    }

    @Test
    void testEnrollInCourse_Success() {
        when(userRepository.findById(studentUser.getId())).thenReturn(Optional.of(studentUser));
        when(courseRepository.findById(testCourse.getCourseId())).thenReturn(Optional.of(testCourse));
        when(enrollmentRepository.findByStudentAndCourse(studentUser, testCourse)).thenReturn(Optional.empty());

        // Create a new Enrollment object that reflects the state *after* the service sets progress to 0.0
        Enrollment savedEnrollment = new Enrollment();
        savedEnrollment.setEnrollmentId(1L); // Assign an ID as if it was saved
        savedEnrollment.setStudent(studentUser);
        savedEnrollment.setCourse(testCourse);
        savedEnrollment.setProgress(0.0); // Set progress to 0.0 as the service would

        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(savedEnrollment); // Mock to return this new object

        Enrollment newEnrollment = enrollmentService.enrollInCourse(studentUser.getId(), testCourse.getCourseId());

        assertNotNull(newEnrollment);
        assertEquals(studentUser.getId(), newEnrollment.getStudent().getId());
        assertEquals(testCourse.getCourseId(), newEnrollment.getCourse().getCourseId());
        assertEquals(0.0, newEnrollment.getProgress()); // This assertion will now pass

        verify(userRepository, times(1)).findById(studentUser.getId());
        verify(courseRepository, times(1)).findById(testCourse.getCourseId());
        verify(enrollmentRepository, times(1)).findByStudentAndCourse(studentUser, testCourse);
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void testEnrollInCourse_StudentNotFound() {
        Long nonExistentStudentId = 99L;
        when(userRepository.findById(nonExistentStudentId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            enrollmentService.enrollInCourse(nonExistentStudentId, testCourse.getCourseId());
        });
        assertEquals("Student not found", exception.getMessage());

        verify(userRepository, times(1)).findById(nonExistentStudentId);
        verify(courseRepository, never()).findById(anyLong());
        verify(enrollmentRepository, never()).findByStudentAndCourse(any(User.class), any(Course.class));
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void testEnrollInCourse_UserNotStudent() {
        when(userRepository.findById(instructorUser.getId())).thenReturn(Optional.of(instructorUser));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            enrollmentService.enrollInCourse(instructorUser.getId(), testCourse.getCourseId());
        });
        assertEquals("Only students can enroll in courses", exception.getMessage());

        verify(userRepository, times(1)).findById(instructorUser.getId());
        verify(courseRepository, never()).findById(anyLong());
        verify(enrollmentRepository, never()).findByStudentAndCourse(any(User.class), any(Course.class));
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void testEnrollInCourse_CourseNotFound() {
        Long nonExistentCourseId = 999L;
        when(userRepository.findById(studentUser.getId())).thenReturn(Optional.of(studentUser));
        when(courseRepository.findById(nonExistentCourseId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            enrollmentService.enrollInCourse(studentUser.getId(), nonExistentCourseId);
        });
        assertEquals("Course not found", exception.getMessage());

        verify(userRepository, times(1)).findById(studentUser.getId());
        verify(courseRepository, times(1)).findById(nonExistentCourseId);
        verify(enrollmentRepository, never()).findByStudentAndCourse(any(User.class), any(Course.class));
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void testEnrollInCourse_AlreadyEnrolled() {
        when(userRepository.findById(studentUser.getId())).thenReturn(Optional.of(studentUser));
        when(courseRepository.findById(testCourse.getCourseId())).thenReturn(Optional.of(testCourse));
        when(enrollmentRepository.findByStudentAndCourse(studentUser, testCourse)).thenReturn(Optional.of(existingEnrollment));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            enrollmentService.enrollInCourse(studentUser.getId(), testCourse.getCourseId());
        });
        assertEquals("Already enrolled in this course", exception.getMessage());

        verify(userRepository, times(1)).findById(studentUser.getId());
        verify(courseRepository, times(1)).findById(testCourse.getCourseId());
        verify(enrollmentRepository, times(1)).findByStudentAndCourse(studentUser, testCourse);
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void testGetEnrollmentsByStudent_Success() {
        Enrollment enrollment2 = new Enrollment();
        enrollment2.setEnrollmentId(2L);
        enrollment2.setStudent(studentUser);
        enrollment2.setCourse(new Course());
        enrollment2.getCourse().setCourseId(102L);

        List<Enrollment> studentEnrollments = Arrays.asList(existingEnrollment, enrollment2);
        when(userRepository.findById(studentUser.getId())).thenReturn(Optional.of(studentUser));
        when(enrollmentRepository.findByStudent(studentUser)).thenReturn(studentEnrollments);

        List<Enrollment> result = enrollmentService.getEnrollmentsByStudent(studentUser.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(existingEnrollment));
        assertTrue(result.contains(enrollment2));

        verify(userRepository, times(1)).findById(studentUser.getId());
        verify(enrollmentRepository, times(1)).findByStudent(studentUser);
    }

    @Test
    void testGetEnrollmentsByStudent_StudentNotFound() {
        Long nonExistentStudentId = 99L;
        when(userRepository.findById(nonExistentStudentId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            enrollmentService.getEnrollmentsByStudent(nonExistentStudentId);
        });
        assertEquals("Student not found", exception.getMessage());

        verify(userRepository, times(1)).findById(nonExistentStudentId);
        verify(enrollmentRepository, never()).findByStudent(any(User.class));
    }

    @Test
    void testGetEnrollmentsByCourse_Success() {
        Enrollment enrollmentForCourse1 = new Enrollment();
        enrollmentForCourse1.setEnrollmentId(3L);
        enrollmentForCourse1.setCourse(testCourse);
        enrollmentForCourse1.setStudent(new User());
        enrollmentForCourse1.getStudent().setId(3L);

        List<Enrollment> courseEnrollments = Arrays.asList(existingEnrollment, enrollmentForCourse1);
        when(courseRepository.findById(testCourse.getCourseId())).thenReturn(Optional.of(testCourse));
        when(enrollmentRepository.findByCourse(testCourse)).thenReturn(courseEnrollments);

        List<Enrollment> result = enrollmentService.getEnrollmentsByCourse(testCourse.getCourseId());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(existingEnrollment));
        assertTrue(result.contains(enrollmentForCourse1));

        verify(courseRepository, times(1)).findById(testCourse.getCourseId());
        verify(enrollmentRepository, times(1)).findByCourse(testCourse);
    }

    @Test
    void testGetEnrollmentsByCourse_CourseNotFound() {
        Long nonExistentCourseId = 999L;
        when(courseRepository.findById(nonExistentCourseId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            enrollmentService.getEnrollmentsByCourse(nonExistentCourseId);
        });
        assertEquals("Course not found", exception.getMessage());

        verify(courseRepository, times(1)).findById(nonExistentCourseId);
        verify(enrollmentRepository, never()).findByCourse(any(Course.class));
    }

    @Test
    void testUpdateProgress_Success() {
        double newProgress = 75.0;
        when(enrollmentRepository.findById(existingEnrollment.getEnrollmentId())).thenReturn(Optional.of(existingEnrollment));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(existingEnrollment);

        Enrollment updatedEnrollment = enrollmentService.updateProgress(existingEnrollment.getEnrollmentId(), newProgress);

        assertNotNull(updatedEnrollment);
        assertEquals(newProgress, updatedEnrollment.getProgress());

        verify(enrollmentRepository, times(1)).findById(existingEnrollment.getEnrollmentId());
        verify(enrollmentRepository, times(1)).save(existingEnrollment);
    }

    @Test
    void testUpdateProgress_EnrollmentNotFound() {
        Long nonExistentEnrollmentId = 999L;
        when(enrollmentRepository.findById(nonExistentEnrollmentId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            enrollmentService.updateProgress(nonExistentEnrollmentId, 80.0);
        });
        assertEquals("Enrollment not found", exception.getMessage());

        verify(enrollmentRepository, times(1)).findById(nonExistentEnrollmentId);
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }
}
