package hr.java.web.radanovic.recordkeeper.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import hr.java.web.radanovic.recordkeeper.dto.AppUserDto;
import hr.java.web.radanovic.recordkeeper.dto.DetailsDto;
import hr.java.web.radanovic.recordkeeper.mapper.UserMapper;
import hr.java.web.radanovic.recordkeeper.model.AppUser;
import hr.java.web.radanovic.recordkeeper.model.AppUserDetails;
import hr.java.web.radanovic.recordkeeper.repository.UserDetailsRepository;
import hr.java.web.radanovic.recordkeeper.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepo;
	private final UserDetailsRepository detailsRepo;
	private final UserMapper userMapper;

	public List<AppUser> getAllUsers() {
		return userRepo.findAll();
	}

	public List<AppUserDetails> getAllDetails() {
		return userRepo.findAllDetails();
	}

	public List<AppUserDto> getAllUserDetails() {
		List<AppUserDto> userDto = new ArrayList<>();
		getAllUsers().forEach(e -> userDto.add(userMapper.mapUserToUserDto(e)));
		return userDto;
	}

	public void addDetails(DetailsDto detailsDto) {
		log.info("detailsDto " + detailsDto);
		AppUserDetails details = userMapper.mapDetailsDtoToDetails(detailsDto);
		detailsRepo.save(details);
	}

	public DetailsDto getCurrentUserDetailsDto(String username) {
		return userMapper.mapDetailsToDetailsDto(detailsRepo.findByUser(userRepo.findByUsername(username).get()).get());
	}
	
	public AppUserDto getCurrentUserDto(String username) {
		return userMapper.mapUserToUserDto(userRepo.findByUsername(username).get());
	}
	
	public AppUserDetails getCurrentUserDetails(String username) {
		return detailsRepo.findByUser(userRepo.findByUsername(username).get()).get();
	}
	
	public AppUser getCurrentUser(String username) {
		return userRepo.findByUsername(username).get();
	}
	
	@Transactional
	public void updateDetails(DetailsDto detailsDto) {
		AppUserDetails details = userMapper.mapDetailsDtoToDetails(detailsDto);
		AppUserDetails detailsDB = detailsRepo.findByUser(details.getUser()).orElse(null);
		if(detailsDB != null) {
			detailsDB.setFirstName(details.getFirstName());
			detailsDB.setLastName(details.getLastName());
			detailsDB.setOIB(details.getOIB());
			detailsDB.setAddress(details.getAddress());
			detailsRepo.update(detailsDB);
		} else {
			detailsRepo.save(details);
		}
	}

}
