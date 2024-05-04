import java.util.Random;

public class Reservation extends Services implements Comparable<Reservation>, Runnable {
	public static int totalNumOfReservations = 0;
	private String hotelName;
	private String totalHotelName;
	private String city;
	private String roomType;
	private String reservationMonth;
	private int reservationStart;
	private int reservationEnd;
	private int dailyCost;
	private final int sleepTime;

	public Reservation(String hotelName, String city, String roomType, String reservationMonth, int reservationStart, int reservationEnd) {
		this.hotelName = hotelName;
		this.city = city;
		totalHotelName = hotelName + " " + city;
		this.roomType = roomType;
		this.reservationMonth = reservationMonth;
		this.reservationStart = reservationStart;
		this.reservationEnd = reservationEnd;
		sleepTime = new Random().nextInt(5000);
		equals();
	}

	protected String getServiceType() {
		return "Room Booking";
	}

	protected double calculateService() {
		return (double)calculateTotalPrice();
	}

	private void equals() {
		switch (roomType) {
			case "Single":
				Single room = new Single();
				dailyCost = room.getCost();
				break;

			case "Double":
				Double room1 = new Double();
				dailyCost = room1.getCost();
				break;

			case "Club":
				Club room2 = new Club();
				dailyCost = room2.getCost();
				break;

			case "Family":
				Family room3 = new Family();
				dailyCost = room3.getCost();
				break;

			case "FamilyWithView":
				FamilyWithView room4 = new FamilyWithView();
				dailyCost = room4.getCost();
				break;

			case "Suite":
				Suite room5 = new Suite();
				dailyCost = room5.getCost();
				break;
		}
	}

	public static String getRoomInfos() {
		return "\nROOM INFOS:\n\n" + new Single().getInfo() + "\n" + new Double().getInfo() + "\n" + new Club().getInfo() + "\n" + new Family().getInfo() + "\n" + new FamilyWithView().getInfo() + "\n" + new Suite().getInfo();
	}

	public String displayInfo() {
		return "Reservation for a " + roomType + " room in " + totalHotelName + " starts on " + getReservationStartDay() + " and ends on " + getReservationEndDay() + ".\nReservaion has a total cost of $" + calculateTotalPrice() + ".\n";
	}

	public String getReservationStartDay() {
		return reservationMonth + " " + reservationStart;
	}

	public String getReservationEndDay() {
		return reservationMonth + " " + reservationEnd;
	}

	public String getMonth() {
		return reservationMonth;
	}

	public String getCity() {
		return city;
	}

	public String getHotelName() {
		return hotelName;
	}

	public String getTotalHotelName() {
		return totalHotelName;
	}

	private int calculateTotalPrice() {
		int reservationNumOfDay = reservationEnd - reservationStart;

		return calculateTotalPrice(reservationMonth, reservationNumOfDay, dailyCost);
	}

	private int calculateTotalPrice(String reservationMonth, int reservationNumOfDay, int dailyCost) {
		if (reservationMonth.equalsIgnoreCase("June") || reservationMonth.equalsIgnoreCase("July") || reservationMonth.equalsIgnoreCase("August"))
			return 2 * reservationNumOfDay * dailyCost;

		else
			return reservationNumOfDay * dailyCost;
	}

	public int compareTo(Reservation reservation) {
		int i = 0;

		while (reservation.getTotalHotelName().charAt(i) == this.totalHotelName.charAt(i) && reservation.getTotalHotelName().charAt(i) != '\0' && this.totalHotelName.charAt(i) != '\0')
			i++;

		int x = reservation.getTotalHotelName().charAt(i), y = this.totalHotelName.charAt(i);

		if (x > y)
			return -1;

		else if (x < y)
			return 1;

		return 0;
	}

	@Override
	public void run() {
		if (hotelName.equals(Screen.hotelName)) {
			try {
				Thread.sleep(sleepTime);
			}

			catch (InterruptedException ex) {
				ex.printStackTrace();
			}

			Screen.string += getCustomerID() + " ";
			Screen.setText(Screen.string);
		}
	}

	public String getRoomType() {
		return roomType;
	}

	public int getReservatioStart() {
		return reservationStart;
	}

	public int getReservatioEnd() {
		return reservationEnd;
	}
}

class Room {
	private String roomType;
	private int dailyCost;
	private int roomSize;
	private boolean hasBath;

	public Room(String roomType, int dailyCost, int roomSize, boolean hasBath) {
		this.roomType = roomType;
		this.dailyCost = dailyCost;
		this.roomSize = roomSize;
		this.hasBath = hasBath;
	}

	public String getInfo() {
		return "Room Type: " + roomType + ", Daily Cost: " + dailyCost + ", Room Size: " + roomSize + ", Has Bath: " + hasBath;
	}

	public int getCost() {
		return dailyCost;
	}
}

class Single extends Room {
	public Single() {
		super("Single", 100, 15, false);
	}
}

class Double extends Room {
	public Double() {
		super("Double", 180, 30, false);
	}
}

class Club extends Room {
	public Club() {
		super("Club", 250, 45, true);
	}
}

class Family extends Room {
	public Family() {
		super("Family", 400, 50, false);
	}
}

class FamilyWithView extends Room {
	public FamilyWithView() {
		super("Family with View", 450, 50, true);
	}
}

class Suite extends Room {
	public Suite() {
		super("Suite", 650, 80, true);
	}
}