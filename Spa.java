public class Spa extends Services {
	private int days;
	private double spaCost = 100;

	void setDays(int days) {
		this.days = days;
	}

	protected String getServiceType() {
		return "Spa";
	}

	protected double calculateService() {
		return spaCost * days;
	}
}