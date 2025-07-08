package com.elearn.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elearn.model.Submission;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
	List<Submission> findByStudent_Id(Long studentId);

}
