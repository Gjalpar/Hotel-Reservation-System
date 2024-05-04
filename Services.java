public abstract class Services{
	private int CustomerID;

	protected abstract String getServiceType();

	protected abstract double calculateService();

	public void setCustomerID(int CustomerID) {
		this.CustomerID = CustomerID;
	}

	public int getCustomerID() {
		return CustomerID;
	}

	public double getCost() {
		return calculateService();
	}

	public String displayServiceInfo() {
		return "Customer ID: " + CustomerID + ", Service Type: " + getServiceType() + ", Cost: " + calculateService();
	}
}