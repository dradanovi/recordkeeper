package hr.java.web.radanovic.recordkeeper.service;

public enum Months {
	JAN("JAN", "01"), FEB("FEB", "02"), MAR("MAR", "03"), APR("APR", "04"), MAY("MAY", "05"), JUN("JUN", "06"),
	JUL("JUL", "07"), AUG("AUG", "08"), SEP("SEP", "09"), OCT("OCT", "10"), NOV("NOV", "11"), DEC("DEC", "12");

	private final String value;

	private final String name;

	private Months(String name, String value) {
		this.value = value;
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}
}
