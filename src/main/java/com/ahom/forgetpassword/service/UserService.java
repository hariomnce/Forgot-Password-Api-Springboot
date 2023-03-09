package com.ahom.forgetpassword.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahom.forgetpassword.entity.User;
import com.ahom.forgetpassword.repo.UserRepo;

@Service
public class UserService {

	private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30;

	@Autowired
	UserRepo userRepo;

	public void saveUser(User user) {
		userRepo.save(user);
	}

	public String forgotPassword(String email) {
//		LocalDateTime localDateTime;

		Optional<User> userOptional = Optional.ofNullable(userRepo.findByEmail(email));
		if (!userOptional.isPresent()) {
			return "Invalid email id.";
		}

		User user = userOptional.get();
		//user.setCreatedOn(new Date());
		user.setToken(generateToken());
		user.setTokenCreationDate(LocalDateTime.now());
		user = userRepo.save(user);
		return user.getToken();
	}

	public String resetPassword(String token, String password) {
		Optional<User> userOptional = Optional.ofNullable(userRepo.findByToken(token));
		if (!userOptional.isPresent()) {
			return "Invalid token.";
		}

		LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();
		if (isTokenExpired(tokenCreationDate)) {
			return "Token expired.";
		}

		User user = userOptional.get();
		user.setPassword(password);
		user.setToken(null);
		user.setTokenCreationDate(null);
		userRepo.save(user);
		return "Your password successfully updated.";
	}

	private String generateToken() {
		StringBuilder token = new StringBuilder();
		return token.append(UUID.randomUUID().toString()).append(UUID.randomUUID().toString()).toString();
	}

	private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {
		LocalDateTime now = LocalDateTime.now();
		Duration diff = Duration.between(tokenCreationDate, now);
		return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
	}
	
}