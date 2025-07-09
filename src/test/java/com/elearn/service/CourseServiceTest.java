package com.elearn.service;

import com.elearn.model.Course;
import com.elearn.model.User;
import com.elearn.model.User.Role;
import com.elearn.repo.CourseRepository;
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
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CourseService courseService;

    private User instructorUser;
    private User studentUser;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        instructorUser = new User();
        instructorUser.setId(1L);
        instructorUser.setEmail("instructor@example.com");
        instructorUser.setRole(Role.INSTRUCTOR);
        instructorUser.setName("Prof. Smith");
        studentUser = new User();
        studentUser.setId(2L);
        studentUser.setEmail("student@example.com");
        studentUser.setRole(Role.STUDENT);
        studentUser.setName("Alice");
        testCourse = new Course();
        testCourse.setCourseId(101L); 
        testCourse.setTitle("Introduction to Programming");
        testCourse.setDescription("Learn the basics of programming.");
        testCourse.setInstructor(instructorUser);
    }

    @Test
    void testAddCourse_Success() {
        when(userRepository.findById(instructorUser.getId())).thenReturn(Optional.of(instructorUser));
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);
        Course addedCourse = courseService.addCourse(instructorUser.getId(), testCourse);
        assertNotNull(addedCourse);
        assertEquals(testCourse.getCourseId(), addedCourse.getCourseId()); 
        assertEquals(instructorUser.getId(), addedCourse.getInstructor().getId());

        
    }

    @Test
    void testAddCourse_InstructorNotFound() {
        Long nonExistentInstructorId = 99L;
        when(userRepository.findById(nonExistentInstructorId)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            courseService.addCourse(nonExistentInstructorId, testCourse);        });
        assertEquals("Instructor not found", exception.getMessage());

       
    }

    @Test
    void testAddCourse_NotAnInstructor() {
        when(userRepository.findById(studentUser.getId())).thenReturn(Optional.of(studentUser));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            courseService.addCourse(studentUser.getId(), testCourse);
        });
        assertEquals("Only instructors can add courses.", exception.getMessage());

        }

    @Test
    void testGetAllCourses_Success() {
        Course course2 = new Course();
        course2.setCourseId(102L);
        course2.setTitle("Advanced Java");
        course2.setInstructor(instructorUser);
        List<Course> allCourses = Arrays.asList(testCourse, course2);
        when(courseRepository.findAll()).thenReturn(allCourses);
        List<Course> resultCourses = courseService.getAllCourses();
        assertNotNull(resultCourses);
        assertEquals(2, resultCourses.size());
        assertTrue(resultCourses.contains(testCourse));
        assertTrue(resultCourses.contains(course2));

       
    }

    @Test
    void testGetAllCourses_NoCoursesFound() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList());
        List<Course> resultCourses = courseService.getAllCourses();
        assertNotNull(resultCourses);
        assertTrue(resultCourses.isEmpty());

       
    }

    @Test
    void testGetCoursesByInstructor_Success() {
        Course instructorCourse1 = new Course();
        instructorCourse1.setCourseId(201L); 
        instructorCourse1.setTitle("Data Structures");
        instructorCourse1.setInstructor(instructorUser);
        Course instructorCourse2 = new Course();
        instructorCourse2.setCourseId(202L); 
        instructorCourse2.setTitle("Algorithms");
        instructorCourse2.setInstructor(instructorUser);
        List<Course> coursesByThisInstructor = Arrays.asList(instructorCourse1, instructorCourse2);
        when(userRepository.findById(instructorUser.getId())).thenReturn(Optional.of(instructorUser));
        when(courseRepository.findByInstructor(instructorUser)).thenReturn(coursesByThisInstructor);
        List<Course> resultCourses = courseService.getCoursesByInstructor(instructorUser.getId());
        assertNotNull(resultCourses);
        assertEquals(2, resultCourses.size());
        assertTrue(resultCourses.contains(instructorCourse1));
        assertTrue(resultCourses.contains(instructorCourse2));

       
    }

    @Test
    void testGetCoursesByInstructor_InstructorNotFound() {
        Long nonExistentInstructorId = 99L;
        when(userRepository.findById(nonExistentInstructorId)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            courseService.getCoursesByInstructor(nonExistentInstructorId);
        });
        assertEquals("Instructor not found", exception.getMessage());

    
    }

    @Test
    void testGetCourseById_Success() {
        when(courseRepository.findById(testCourse.getCourseId())).thenReturn(Optional.of(testCourse));
        Course foundCourse = courseService.getCourseById(testCourse.getCourseId()); 
        assertNotNull(foundCourse);
        assertEquals(testCourse.getCourseId(), foundCourse.getCourseId()); 
        assertEquals(testCourse.getTitle(), foundCourse.getTitle());

    }

    @Test
    void testGetCourseById_CourseNotFound() {
        Long nonExistentCourseId = 999L;
        when(courseRepository.findById(nonExistentCourseId)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            courseService.getCourseById(nonExistentCourseId);
            });
        assertEquals("Course not found", exception.getMessage());

    }

    @Test
    void testDeleteCourse_Success() {
        Long courseToDeleteId = 101L;
        doNothing().when(courseRepository).deleteById(courseToDeleteId);
        courseService.deleteCourse(courseToDeleteId);

    }
}
