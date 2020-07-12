package hr.java.web.radanovic.recordkeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {

	private String condition;
	private String temperature;
	private String feelslike;
	private String wind;
	private String humidity;
	
}
