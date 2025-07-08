package com.elearn.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elearn.model.Assessment;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
	List<Assessment> findByCourse_CourseId(Long courseId);
}
