package hr.java.web.radanovic.recordkeeper.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hr.java.web.radanovic.recordkeeper.dto.DetailsDto;
import hr.java.web.radanovic.recordkeeper.dto.RegisterRequest;
import hr.java.web.radanovic.recordkeeper.dto.WorkHoursDto;
import hr.java.web.radanovic.recordkeeper.dto.WorkSubjectDto;
import hr.java.web.radanovic.recordkeeper.mapper.UserMapper;
import hr.java.web.radanovic.recordkeeper.repository.UserDetailsRepository;
import hr.java.web.radanovic.recordkeeper.repository.UserRepository;
import hr.java.web.radanovic.recordkeeper.service.AuthService;
import hr.java.web.radanovic.recordkeeper.service.PictureService;
import hr.java.web.radanovic.recordkeeper.service.UserService;
import hr.java.web.radanovic.recordkeeper.service.WorkHoursService;
import hr.java.web.radanovic.recordkeeper.service.WorkSubjectsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

	private final AuthService authService;
	private final UserRepository userRepo;
	private final UserDetailsRepository detailsRepo;
	private final UserService userService;
	private final UserMapper userMapper;
	private final WorkHoursService hoursService;
	private final WorkSubjectsService subjectService;
	private final PictureService picService;

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
		log.info("signup");
		authService.signup(registerRequest);
		return new ResponseEntity<>("User Registration Successful", HttpStatus.OK);
	}

	@GetMapping("/users/all")
	public ResponseEntity<List<String>> allUsers() {
		return new ResponseEntity<List<String>>(userRepo.findAllUsernames(), HttpStatus.OK);
	}
	
	@PostMapping("/user/detail")
	public ResponseEntity<DetailsDto> getUserDetail(@RequestBody String username){
		return new ResponseEntity<DetailsDto>(userMapper.mapDetailsToDetailsDto(detailsRepo.findByUser(userRepo.findByUsername(username).get()).orElse(null)), HttpStatus.OK);
	}
	
	@PostMapping("/user/detail/edit")
	public ResponseEntity<String> editDetails(@RequestBody DetailsDto detailsDto){
		userService.updateDetails(detailsDto);
		return new ResponseEntity<>("Details edited", HttpStatus.OK);
	}
	
	@PostMapping("/user/hours/display")
	public ResponseEntity<List<WorkHoursDto>> displayHours(@RequestBody WorkHoursDto hoursDto) {
		return ResponseEntity.status(HttpStatus.OK).body(hoursService.getHoursByUserDisplayFilter(hoursDto));
	}

	@PostMapping("/user/subject/display")
	public ResponseEntity<List<WorkSubjectDto>> displaySubjects(@RequestBody WorkHoursDto subjectsDto) {
		return ResponseEntity.status(HttpStatus.OK).body(subjectService.getSubjectsByUserDisplayFilter(subjectsDto));
	}
	
	@PostMapping("/user/img/save/{username}")
	public ResponseEntity<String> saveImg(@RequestBody MultipartFile file, @PathVariable("username") String username){
		picService.saveOrRepolacePic(file, username);
		return ResponseEntity.status(HttpStatus.CREATED).body("Picture Created");
	}
	
	@PostMapping("/mail/{username}")
	public ResponseEntity<String> mail(@PathVariable("username") String username){
		authService.sendMail(username);
		return new ResponseEntity<String>("Mail sent", HttpStatus.OK);
	}

}
