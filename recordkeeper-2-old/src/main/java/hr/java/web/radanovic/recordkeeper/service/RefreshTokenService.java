package hr.java.web.radanovic.recordkeeper.service;

import java.time.Instant;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import hr.java.web.radanovic.recordkeeper.exception.JwtException;
import hr.java.web.radanovic.recordkeeper.model.RefreshToken;
import hr.java.web.radanovic.recordkeeper.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {
	 private final RefreshTokenRepository refreshTokenRepository;

	    public RefreshToken generateRefreshToken() {
	        RefreshToken refreshToken = new RefreshToken();
	        refreshToken.setToken(UUID.randomUUID().toString());
	        refreshToken.setCreated(Instant.now());

	        return refreshTokenRepository.save(refreshToken);
	    }

	    void validateRefreshToken(String token) {
	        refreshTokenRepository.findByToken(token)
	                .orElseThrow(() -> new JwtException("Invalid refresh Token"));
	    }

	    public void deleteRefreshToken(String token) {
	        refreshTokenRepository.deleteByToken(token);
	    }
}
