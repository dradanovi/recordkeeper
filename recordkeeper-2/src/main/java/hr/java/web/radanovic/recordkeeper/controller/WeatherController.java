package hr.java.web.radanovic.recordkeeper.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.java.web.radanovic.recordkeeper.dto.WeatherResponse;
import hr.java.web.radanovic.recordkeeper.service.WeatherService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/api/weather")
public class WeatherController {

	private final WeatherService weatherService;
	
	@GetMapping("/current")
	public ResponseEntity<WeatherResponse> current(){
		return new ResponseEntity<WeatherResponse>(weatherService.fetchData(), HttpStatus.OK);
	}
	
}
