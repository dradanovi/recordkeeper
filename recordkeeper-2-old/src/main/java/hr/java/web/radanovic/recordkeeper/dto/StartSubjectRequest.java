package hr.java.web.radanovic.recordkeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartSubjectRequest {

	private String title;
	private String desc;
	
}
