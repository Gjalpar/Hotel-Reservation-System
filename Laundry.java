public class Laundry extends Services {
	private int clothingPieces;
	private double laundryCost = 20;

	void setClothingPieces(int clothingPieces) {
		this.clothingPieces = clothingPieces;
	}

	protected String getServiceType() {
		return "Laundry";
	}

	protected double calculateService() {
		return laundryCost * clothingPieces;
	}
}