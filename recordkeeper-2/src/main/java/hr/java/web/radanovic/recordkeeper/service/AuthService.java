package hr.java.web.radanovic.recordkeeper.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hr.java.web.radanovic.recordkeeper.dto.AuthenticationResponse;
import hr.java.web.radanovic.recordkeeper.dto.LoginRequest;
import hr.java.web.radanovic.recordkeeper.dto.RefreshTokenRequest;
import hr.java.web.radanovic.recordkeeper.dto.RegisterRequest;
import hr.java.web.radanovic.recordkeeper.exception.UserException;
import hr.java.web.radanovic.recordkeeper.model.AppUser;
import hr.java.web.radanovic.recordkeeper.model.Authority;
import hr.java.web.radanovic.recordkeeper.model.NotificationEmail;
import hr.java.web.radanovic.recordkeeper.model.VerificationToken;
import hr.java.web.radanovic.recordkeeper.repository.AuthoritiesRepository;
import hr.java.web.radanovic.recordkeeper.repository.UserRepository;
import hr.java.web.radanovic.recordkeeper.repository.VerificationTokenRepository;
import hr.java.web.radanovic.recordkeeper.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class AuthService {
	private final int NUMBER_OF_CHARACTERS = 8;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepo;
	private final VerificationTokenRepository verTokenRepo;
	private final MailService mailService;
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	private final RefreshTokenService refreshTokenService;
	private final AuthoritiesRepository roleRepo;

	public void signup(RegisterRequest registerRequest) {
		AppUser user = new AppUser();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
//		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setPassword(generateRandomPassword());
		log.info(generateRandomPassword());

		user.setCreated(Instant.now());
		user.setEnabled(false);
		user.getRoles().add(roleRepo.findByAuthority(Authority.ROLE_EMPLOYEE));

		userRepo.save(user);
	}

	public void sendMail(String username) {
		AppUser user = userRepo.findByUsername(username).get();
		String token = generateVerificationToken(user);
		log.info("verification token " + token);

		mailService.sendMail(new NotificationEmail("Activation Link\n", user.getEmail(),
				"Activate account with link \n" + "http://localhost:8080/api/auth/accountVerification/" + token
						+ " \n Login with link \n http://localhost:4200/login" + "\n and credentials:\n username: "
						+ user.getUsername() + " \npassword: " + user.getPassword()));
	}

	public String generateRandomPassword() {
		Random rand = new Random();
		List<Integer> offests = new ArrayList<Integer>();
		offests.add(33);
		offests.add(65);
		offests.add(97);

		String password = "";
		for (int i = 0; i < NUMBER_OF_CHARACTERS; i++) {
			password = password.concat(Character.toString((char) rand.nextInt(25) + 1 + offests.get(rand.nextInt(2))));
		}

		return password;
	}
	
	

	@Transactional
	public AppUser getCurrentUser() {
		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userRepo.findByUsername(principal.getUsername())
				.orElseThrow(() -> new UserException("User name not found - " + principal.getUsername()));
	}

	private void fetchUserAndEnable(VerificationToken verificationToken) {
		String username = verificationToken.getUser().getUsername();
		AppUser user = userRepo.findByUsername(username)
				.orElseThrow(() -> new UserException("User not found with name - " + username));
		user.setEnabled(true);
		userRepo.save(user);
	}

	private String generateVerificationToken(AppUser user) {
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationToken.setExpiration(Instant.now().plus(10, ChronoUnit.HOURS));

		verTokenRepo.save(verificationToken);
		return token;
	}

	public void verifyAccount(String token) {
		Optional<VerificationToken> verificationToken = verTokenRepo.findByToken(token);
		fetchUserAndEnable(verificationToken.orElseThrow(() -> new UserException("Invalid Token")));
		verTokenRepo.remove(verificationToken.get());
		AppUser user = verificationToken.get().getUser();
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepo.save(user);
	}

	public AuthenticationResponse login(LoginRequest loginRequest) {
		log.info("login service " + loginRequest.getUsername());
		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authenticate);
		String token = jwtProvider.generateToken(authenticate);
		return AuthenticationResponse.builder().authenticationToken(token)
				.refreshToken(refreshTokenService.generateRefreshToken().getToken())
				.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
				.username(loginRequest.getUsername()).build();
	}

	public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
		refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
		String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
		log.info(refreshTokenRequest.getRefreshToken());
		return AuthenticationResponse.builder().authenticationToken(token)
				.refreshToken(refreshTokenRequest.getRefreshToken())
				.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
				.username(refreshTokenRequest.getUsername()).build();
	}

	public Boolean getRolesForUser(String username, String role) {
		return userRepo.findByUsername(username).get().getRoles().stream().map(e -> e.getAuthority().toString())
				.collect(Collectors.toList()).contains(role);
	}
}
