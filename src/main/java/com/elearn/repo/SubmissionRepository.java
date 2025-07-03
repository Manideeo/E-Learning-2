package com.elearn.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elearn.model.Submission;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

}
