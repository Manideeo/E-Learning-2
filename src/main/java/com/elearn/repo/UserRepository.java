package com.elearn.repo;




import org.springframework.data.jpa.repository.JpaRepository;

import com.elearn.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
 Optional<User> findByEmail(String email);
}
