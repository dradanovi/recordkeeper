package hr.java.web.radanovic.recordkeeper.controller;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.java.web.radanovic.recordkeeper.dto.AppUserDto;
import hr.java.web.radanovic.recordkeeper.dto.DetailsDto;
import hr.java.web.radanovic.recordkeeper.dto.RegisterRequest;
import hr.java.web.radanovic.recordkeeper.service.AuthService;
import hr.java.web.radanovic.recordkeeper.service.PictureService;
import hr.java.web.radanovic.recordkeeper.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

	private final UserService userService;
	private final AuthService authService;
	private final PictureService picService;

	@GetMapping("/findAll")
	public ResponseEntity<List<AppUserDto>> userDetails() {
		return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUserDetails());
	}

	@PostMapping("/addDetails")
	public ResponseEntity<String> newUser(@RequestBody DetailsDto detailsDto) {
		userService.addDetails(detailsDto);
		return new ResponseEntity<>("User Details Added", HttpStatus.OK);
	}

	@PostMapping("/new")
	public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
		authService.signup(registerRequest);
		return new ResponseEntity<>("User Registration Successful", HttpStatus.OK);
	}

	@GetMapping("/view/details")
	public ResponseEntity<DetailsDto> viewSelfDetails() {
		log.info("view/details");
		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.status(HttpStatus.OK).body(userService.getCurrentUserDetailsDto(principal.getUsername()));
	}

	@GetMapping("/view")
	public ResponseEntity<AppUserDto> viewSelf() {
		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.status(HttpStatus.OK).body(userService.getCurrentUserDto(principal.getUsername()));
	}
	
	@GetMapping("/img/{username}")
	public ResponseEntity<Resource> getImg(@PathVariable("username") String username){
		return picService.picUrl(username);
	}

}
