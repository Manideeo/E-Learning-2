package com.elearn.repo;

import com.elearn.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
	List<Enrollment> findByStudent(User student);

	List<Enrollment> findByCourse(Course course);

	Optional<Enrollment> findByStudentAndCourse(User student, Course course);
}