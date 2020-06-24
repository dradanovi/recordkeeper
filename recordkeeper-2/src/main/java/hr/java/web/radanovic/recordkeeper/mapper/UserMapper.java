package hr.java.web.radanovic.recordkeeper.mapper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import hr.java.web.radanovic.recordkeeper.dto.AppUserDto;
import hr.java.web.radanovic.recordkeeper.dto.DetailsDto;
import hr.java.web.radanovic.recordkeeper.model.AppUser;
import hr.java.web.radanovic.recordkeeper.model.AppUserDetails;
import hr.java.web.radanovic.recordkeeper.repository.UserRepository;
import lombok.AllArgsConstructor;

//@Mapper(config = SpringMapperConfig.class)
@Service
@AllArgsConstructor
public class UserMapper {

	private final UserRepository userRepo;
	
//	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

//	@Mapping(target = "created", expression = "java(mapLocalDateTime(user.getCreated()))")
//	AppUserDto mapUserToUserDto(AppUser user);
//
//	default LocalDateTime mapLocalDateTime(Instant created) {
//		return LocalDateTime.ofInstant(created, ZoneOffset.UTC);
//	}

//	@Mapping(target = "created", expression = "java(mapLocalDateTime(user.getCreated()))")
//	AppUserDto mapUserToUserDto(AppUser user, AppUserDetails details);
//	
//	default LocalDateTime mapLocalDateTime(Instant created) {
//		return LocalDateTime.ofInstant(created, ZoneOffset.UTC);
//	}
//	

//	@InheritInverseConfiguration
//	@Mapping(target = "created", expression = "java(mapInstant(userDto.getCreated()))")
//	AppUser mapUserDtoToUser(AppUserDto userDto);
//
//	default Instant mapInstant(LocalDateTime local) {
//		return local.toInstant(ZoneOffset.UTC);
//	}

	public AppUserDto mapUserToUserDto(AppUser user) {
		AppUserDto userDto = new AppUserDto();
		userDto.setUsername(user.getUsername());
		userDto.setEmail(user.getEmail());
		userDto.setCreated(LocalDateTime.ofInstant(user.getCreated(), ZoneOffset.UTC));
		return userDto;
	}

	public AppUser mapUserDtoToUser(AppUserDto userDto) {
		AppUser user = new AppUser();
		user.setUsername(userDto.getUsername());
		user.setEmail(userDto.getEmail());
		user.setCreated(userDto.getCreated().toInstant(ZoneOffset.UTC));
		user.setEnabled(true);
		return user;
	}

	public DetailsDto mapDetailsToDetailsDto(AppUserDetails details) {
		if(details == null) {
			return new DetailsDto();
		}
		DetailsDto detailsDto = new DetailsDto();
		detailsDto.setFirstName(details.getFirstName());
		detailsDto.setLastName(details.getLastName());
		detailsDto.setAddress(details.getAddress());
		detailsDto.setOib(details.getOIB());
		detailsDto.setUsername(details.getUser().getUsername());

		return detailsDto;
	}

	public AppUserDetails mapDetailsDtoToDetails(DetailsDto detailsDto) {
		AppUserDetails details = new AppUserDetails();
		details.setFirstName(detailsDto.getFirstName());
		details.setLastName(detailsDto.getLastName());
		details.setAddress(detailsDto.getAddress());
		details.setOIB(detailsDto.getOib());
		details.setUser(userRepo.findByUsername(detailsDto.getUsername()).get());

		return details;
	}
}
