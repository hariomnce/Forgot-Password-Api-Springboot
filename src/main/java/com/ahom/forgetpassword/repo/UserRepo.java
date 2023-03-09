package com.ahom.forgetpassword.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.ahom.forgetpassword.entity.User;

//@EnableJpaRepositories
//@Repository("userRepo")
public interface UserRepo extends JpaRepository<User, Long> {

	User findByEmail(String email);

	User findByToken(String token);

}
