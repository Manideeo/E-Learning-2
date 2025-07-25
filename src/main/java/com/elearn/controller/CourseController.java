package com.elearn.controller;

import com.elearn.model.Course;
import com.elearn.service.CourseService;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/add/{instructorId}")
    public ResponseEntity<Course> addCourse(@PathVariable Long instructorId, @RequestBody Course course) {
        Course savedCourse = courseService.addCourse(instructorId, course);
        return ResponseEntity.ok(savedCourse);
    }

    @GetMapping
 
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/instructor/{instructorId}")   
    public ResponseEntity<List<Course>> getByInstructor(@PathVariable Long instructorId) {
        return ResponseEntity.ok(courseService.getCoursesByInstructor(instructorId));
    }

    @GetMapping("/{courseId}")   
    public ResponseEntity<Course> getById(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getCourseById(courseId));
    }

    @DeleteMapping("/{courseId}")   
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok().build();
    }
}

