package hr.java.web.radanovic.recordkeeper.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class PasswordGenerationService {

	private final int NUMBER_OF_CHARACTERS = 8;

	public String generatePassword() {
		Random rand = new Random();
		List<Integer> offests = new ArrayList<Integer>();
		offests.add(33);
		offests.add(65);
		offests.add(97);

		String password = "";
		for (int i = 0; i < NUMBER_OF_CHARACTERS; i++) {
			password = password.concat(Character.toString((char) rand.nextInt(25) + 1 + offests.get(rand.nextInt(2))));
		}

		return password;
	}
}
