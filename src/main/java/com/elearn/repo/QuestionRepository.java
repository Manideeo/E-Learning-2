package com.elearn.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elearn.model.Question;
import java.util.*;


public interface QuestionRepository extends JpaRepository<Question, Long> {
	    List<Question> findByAssessment_AssessmentId(Long assessmentId);
}

