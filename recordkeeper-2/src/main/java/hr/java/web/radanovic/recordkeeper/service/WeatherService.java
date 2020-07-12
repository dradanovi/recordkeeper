package hr.java.web.radanovic.recordkeeper.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import hr.java.web.radanovic.recordkeeper.dto.WeatherResponse;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WeatherService {

	public WeatherResponse fetchData() {
//		http://api.weatherstack.com/current
//			95085a1df367570ba5cfc8d47cb0149a
//			zagreb
//		https://api.weatherstack.com/current?access_key=95085a1df367570ba5cfc8d47cb0149a&query=Zagreb
		BufferedReader br = null;
		String line;
		StringBuilder sb = new StringBuilder();

		try {

			URL url = new URL("http://api.weatherstack.com/current?access_key=95085a1df367570ba5cfc8d47cb0149a&query=Zagreb");
			br = new BufferedReader(new InputStreamReader(url.openStream()));

			while ((line = br.readLine()) != null) {

				sb.append(line);
				sb.append(System.lineSeparator());
			}

		} catch (IOException e) {
			// TODO: handle exception
			System.out.println("weather io exception");
		} finally {

			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("weather close exception");
				}
			}
		}
		
		JSONObject obj = new JSONObject(sb.toString());
		JSONArray arr = obj.getJSONObject("current").getJSONArray("weather_descriptions");
		String condition = "";
		for(int i = 0; i < arr.length(); i++) {
			condition = condition.concat(arr.getString(i) + " ");
		}
		int temp = obj.getJSONObject("current").getInt("temperature");
		int feels = obj.getJSONObject("current").getInt("feelslike");		
		int speed = obj.getJSONObject("current").getInt("wind_speed");
		String direction = obj.getJSONObject("current").getString("wind_dir");
		int humidity = obj.getJSONObject("current").getInt("humidity");
		
		return new WeatherResponse(condition, temp + " °C", feels + " °C", speed + " km/h " + direction, humidity + " %");
	}

}
