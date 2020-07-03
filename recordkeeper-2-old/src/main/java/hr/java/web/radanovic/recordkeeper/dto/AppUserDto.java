package hr.java.web.radanovic.recordkeeper.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUserDto {

	private String username;
	private String email;
	private LocalDateTime created;
	
}
