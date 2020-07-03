package hr.java.web.radanovic.recordkeeper.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class FormatDateService {

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	private final DateTimeFormatter rawformatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

	// 01/01/2020 00:00 => 16
	// Jun 08 2020 		=> 11
	public LocalDateTime stringToLDT(String date, boolean bool) {
		if (date == null || date.length() < 8) {
			return LocalDateTime.now();
		}
		
		if(date.length() == 16) {
			return LocalDateTime.parse(date, formatter);
		}
		
		date = monthFormat(date);		

		List<String> array = Arrays.asList(date.split(" "));
		for (int i = 0; i < 2; i++) {
			if (array.size() < 2) {
				array.set(i, "0" + array.get(i));
			}
		}

		String stringDate = "";
		for (String string : array) {
			stringDate = stringDate.concat("/" + string);
		}
		stringDate = stringDate.substring(1);

		if (stringDate.length() == 10) {
			if (bool == true) {
				stringDate = stringDate.concat(" 00:00");
			} else {
				stringDate = stringDate.concat(" 23:59");
			}
		}

		return LocalDateTime.parse(stringDate, rawformatter);
	}

	public String LDTToString(LocalDateTime date) {
		return date.format(formatter);
	}

	private String monthFormat(String raw) {
		raw = raw.toUpperCase();
		switch (raw.substring(0, 3)) {
		case "JAN":
			return StringUtils.replace(raw, "JAN", "01");
		case "FEB":
			return StringUtils.replace(raw, "FEB", "02");
		case "MAR":
			return StringUtils.replace(raw, "MAR", "03");
		case "APR":
			return StringUtils.replace(raw, "APR", "04");
		case "MAY":
			return StringUtils.replace(raw, "MAY", "05");
		case "JUN":
			return StringUtils.replace(raw, "JUN", "06");
		case "JUL":
			return StringUtils.replace(raw, "JUL", "07");
		case "AUG":
			return StringUtils.replace(raw, "AUG", "08");
		case "SEP":
			return StringUtils.replace(raw, "SEP", "09");
		case "OCT":
			return StringUtils.replace(raw, "OCT", "10");
		case "NOV":
			return StringUtils.replace(raw, "NOV", "11");
		case "DEC":
			return StringUtils.replace(raw, "DEC", "12");
		default:
			return raw;
		}

	}

}
