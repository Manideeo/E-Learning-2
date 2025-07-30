package com.elearn.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elearn.model.Option;

public interface OptionRepository extends JpaRepository<Option, Long> {

}
