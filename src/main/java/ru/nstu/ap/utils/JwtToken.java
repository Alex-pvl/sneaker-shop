package ru.nstu.ap.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.nstu.ap.model.user.User;

import java.util.Date;

@Component
public class JwtToken {
	private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 1 day

	@Value("${app.jwt.secret}")
	private static String SECRET_KEY;

	public String generateAccessToken(User user) {
		return Jwts.builder()
			.setSubject("%s:%s".formatted(user.getId(), user.getEmail()))
			.setIssuer("AP-e-shop")
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
			.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
			.compact();
	}
}
