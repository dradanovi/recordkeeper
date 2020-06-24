package hr.java.web.radanovic.recordkeeper.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.java.web.radanovic.recordkeeper.dto.AuthenticationResponse;
import hr.java.web.radanovic.recordkeeper.dto.LoginRequest;
import hr.java.web.radanovic.recordkeeper.dto.RefreshTokenRequest;
import hr.java.web.radanovic.recordkeeper.service.AuthService;
import hr.java.web.radanovic.recordkeeper.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
    	log.info("accountverification");
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
    	log.info("refresh token request " + refreshTokenRequest);
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Refresh Token Deleted Successfully!!");
    }
    
    @GetMapping("/role/{username}/{role}")
    public ResponseEntity<Boolean> checkAuthorities(@PathVariable("username") String username, @PathVariable("role") String role){
    	System.out.println(username);
    	return new ResponseEntity<Boolean>(authService.getRolesForUser(username, role), HttpStatus.OK);
    }
}
