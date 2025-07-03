package com.elearn.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elearn.model.Assessment;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

}
