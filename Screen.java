import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class Screen extends JFrame {
	private static final long serialVersionUID = 1L;
	public static String hotelName, string;
	private String csvFile = "file.csv";
	private ArrayList<Reservation> arraylist = new ArrayList<Reservation>();
	private ArrayList<Services> arraylist2 = new ArrayList<Services>();
	private JButton button1, button2, button3, button4, button5, button6;
	private JMenuItem i1, i2, i3, i4, i5;
	private static final JTextArea textArea= new JTextArea(38, 50);

	public Screen() {
		super("Hotel Reservation System");
		setLayout(new FlowLayout());

		JMenuBar mb = new JMenuBar();
		JMenu menu1 = new JMenu("File");

		i1 = new JMenuItem("Exit");
		menu1.add(i1);

		JMenu menu2 = new JMenu("New");

		i2 = new JMenuItem("Reservation");
		i3 = new JMenuItem("Services");
		menu2.add(i2);
		menu2.add(i3);

		JMenu menu3 = new JMenu("Help");

		i4 = new JMenuItem("Contents");
		i5 = new JMenuItem("About");
		menu3.add(i4);
		menu3.add(i5);

		mb.add(menu1);
		mb.add(menu2);
		mb.add(menu3);
		setJMenuBar(mb);

		button1 = new JButton("Display Reservations");
		add(button1);

		button2 = new JButton("Display Extra Services");
		add(button2);

		button3 = new JButton("Disp. Res. For City");
		add(button3);

		button4 = new JButton("Multithread Search");
		add(button4);

		add(textArea);

		button5 = new JButton("Save Reservations");
		add(button5);

		button6 = new JButton("Load Reservations");
		add(button6);

		ButtonHandler handler = new ButtonHandler();
		button1.addActionListener(handler);
		button2.addActionListener(handler);
		button3.addActionListener(handler);
		button4.addActionListener(handler);
		button5.addActionListener(handler);
		button6.addActionListener(handler);
		i1.addActionListener(handler);
		i2.addActionListener(handler);
		i3.addActionListener(handler);
		i4.addActionListener(handler);
		i5.addActionListener(handler);
	}

	private class ButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			string = "";
			if (event.getSource() == button1) {
				int i = 1;

				for (Reservation reservation : arraylist) {
					string += "Reservation ID #" + i + "\n" + reservation.displayInfo() + "\n";
					i++;
				}

				setText(string);
			}

			if (event.getSource() == button2) {
				int i = 1;

				for(Services service : arraylist2) {
					string += "Reservation ID #" + i + "\n" + service.displayServiceInfo() + "\n";
					i++;
				}

				setText(string);
			}

			if (event.getSource() == button3) {
				Iterator<Reservation> itera = arraylist.iterator();
				String city = JOptionPane.showInputDialog("Type a city name for a reservation search:");
				Reservation reservation;
				string += "Reservations for " + city + ":\n";

				while (itera.hasNext()) {
					reservation = itera.next();

					if (city.equalsIgnoreCase(reservation.getCity()))
						string += "Reservation ID #" + reservation.getCustomerID() + "\n" + reservation.displayInfo() + "\n";
				}

				setText(string);
			}

			if (event.getSource() == button4) {
				if (Reservation.totalNumOfReservations < 8)
					JOptionPane.showMessageDialog(Screen.this, "Please enter at least 8 reservations - now it is only " + Reservation.totalNumOfReservations);

				else {
					hotelName = JOptionPane.showInputDialog("Enter Hotel Name: ");
					string = "Reservation IDs for " + hotelName + ":\n";
					ExecutorService threadExecutor = Executors.newCachedThreadPool();
					for (Reservation reservation : arraylist) threadExecutor.execute(reservation);
					threadExecutor.shutdown();
				}
			}

			if (event.getSource() == button5) {
				try {
					FileWriter write = new FileWriter(csvFile);
					write.append("HotelName, CityName, RoomType, ReservationMonth, ReservationStart, ReservationEnd\n");

					for (Reservation reservation : arraylist) {
						write.append(reservation.getHotelName()); write.append(", ");
						write.append(reservation.getCity()); write.append(", ");
						write.append(reservation.getRoomType()); write.append(", ");
						write.append(reservation.getMonth()); write.append(", ");
						write.append("" + reservation.getReservatioStart()); write.append(", ");
						write.append("" + reservation.getReservatioEnd());
					}

					write.flush();
					write.close();

					JOptionPane.showMessageDialog(Screen.this, "Saved!", "Info", JOptionPane.INFORMATION_MESSAGE);
				}

				catch (IOException e) {
					JOptionPane.showMessageDialog(Screen.this, "Save failed.");
				}
			}

			if (event.getSource() == button6) {
				try {
					String line;
					BufferedReader read = new BufferedReader(new FileReader(csvFile));
					read.readLine();

					while ((line = read.readLine()) != null) {
						String[] data = line.split(", ");
						Reservation newReservation = new Reservation(data[0], data[1], data[2], data[3], Integer.parseInt(data[4]), Integer.parseInt(data[5]));
						newReservation.setCustomerID(++Reservation.totalNumOfReservations);
						arraylist.add(newReservation);
					}

					read.close();
				}

				catch (IOException e) {
					JOptionPane.showMessageDialog(Screen.this, "Load failed.");
				}
			}

			else if (event.getSource() == i1) {
				System.exit(1);
			}

			else if (event.getSource() == i2) {
				Reservation newReservation = newReservation();
				newReservation.setCustomerID(++Reservation.totalNumOfReservations);
				arraylist.add(newReservation);
			}

			else if (event.getSource() == i3) {
				boolean isErr;
				int control = Integer.parseInt(JOptionPane.showInputDialog("Please select one of the extra services from below:\n1. Laundry Service\n2. Spa Service"));

				if (control == 1 && Reservation.totalNumOfReservations != 0) {
					Laundry laundry = new Laundry();
					int id = -1;

					while (id < 1 || id > Reservation.totalNumOfReservations) {
						id = Integer.parseInt(JOptionPane.showInputDialog("Type the reservation ID to credit this service:"));

						if (id < 1 || id > Reservation.totalNumOfReservations)
							JOptionPane.showMessageDialog(Screen.this, "You entered the wrong number. Please enter a number between 1 and " + Reservation.totalNumOfReservations + ".");
					}

					laundry.setCustomerID(id);
					int clothingPieces = 0;

					do {
						try {
							isErr = false;
							clothingPieces = Integer.parseInt(JOptionPane.showInputDialog("How many pieces of clothing?"));
						}

						catch (InputMismatchException e) {
							JOptionPane.showMessageDialog(Screen.this, "Clothing count must be a numeric value!");
							isErr = true;
						}
					} while (isErr);

					laundry.setClothingPieces(clothingPieces);
					arraylist2.add(laundry);
				}

				else if (control == 2 && Reservation.totalNumOfReservations != 0) {
					Spa spa = new Spa();
					int id = -1;

					while (id < 1 || id > Reservation.totalNumOfReservations) {
						id =  Integer.parseInt(JOptionPane.showInputDialog("Type the reservation ID to credit this service:"));

						if (id < 1 || id > Reservation.totalNumOfReservations)
							JOptionPane.showMessageDialog(Screen.this, "You entered the wrong number. Please enter a number between 1 and "+ Reservation.totalNumOfReservations +".");
					}

					spa.setCustomerID(id);
					int days = 0;

					do {
						try {
							isErr = false;
							days = Integer.parseInt(JOptionPane.showInputDialog("How many days?"));
						}

						catch (InputMismatchException e) {
							JOptionPane.showMessageDialog(Screen.this, "Day count must be a numeric value!");
							isErr = true;
						}
					} while (isErr);

					System.out.println();
					spa.setDays(days);
					arraylist2.add(spa);
				}

				else if (Reservation.totalNumOfReservations == 0)
					JOptionPane.showMessageDialog(Screen.this, "This transaction cannot be performed because no reservation has been made yet.");

				else
					JOptionPane.showMessageDialog(Screen.this, "You entered the wrong number. Please enter 1 or 2.");
			}

			else if (event.getSource() == i4) {
				string = "Reservations and Services";
				JOptionPane.showMessageDialog(Screen.this, string);
			}

			else if (event.getSource() == i5) {
				string = "HOTEL RESERVATION SYSTEM®\nALL RIGHTS RESERVED©\n\nDeveloper Information:\n\nName: Eren\nSurname: Dumlupınar\nAge: 21\nCity of Residence: Manisa\nDistrict of Residence: Selendi\nAttended School: Yeditepe University\nFaculty: Faculty of Engineering\nDepartment: Computer Engineering\n";
				JOptionPane.showMessageDialog(Screen.this, string);
			}
		}
	}

	public static void setText(String string) {
		textArea.setText(string);
	}

	private Reservation newReservation() {
		String hotelName = JOptionPane.showInputDialog("Hotel Name: ");
		String city = JOptionPane.showInputDialog("Enter City: ");
		String roomType = "";
		boolean isErr;

		do {
			try {
				isErr = false;
				roomType = next(JOptionPane.showInputDialog(Reservation.getRoomInfos() + "\n\nRoom Type: "));
			}

			catch (RoomTypeException e) {
				JOptionPane.showMessageDialog(Screen.this, e.getMessage());
				isErr = true;
			}
		} while (isErr);

		String reservationMonth = JOptionPane.showInputDialog("Reservation Month: ");
		int reservationStart = 0;
		int reservationEnd = 0;

		do {
			try {
				isErr = false;
				reservationStart = Integer.parseInt(JOptionPane.showInputDialog("Reservation Start:"));
			}

			catch (InputMismatchException e) {
				JOptionPane.showMessageDialog(Screen.this, "Reservation Start must be a numeric value!");
				isErr = true;
			}
		} while (isErr);

		do {
			try {
				isErr = false;
				reservationEnd = Integer.parseInt(JOptionPane.showInputDialog("Reservation End:"));
			}

			catch (InputMismatchException e) {
				JOptionPane.showMessageDialog(Screen.this, "Reservation End must be a numeric value!");
				isErr = true;
			}
		} while (isErr);

		Reservation reservation = new Reservation(hotelName, city, roomType, reservationMonth, reservationStart, reservationEnd);
		return reservation;
	}

	private String next(String input) throws RoomTypeException {
		String[] types = {"Single", "Double", "Club", "Family", "Family with View", "Suite"};
		boolean inTypes = false;

		for (int i = 0; i < 6; i++) {
			if (input.equals(types[i])) inTypes = true;
		}

		if (inTypes)
			return input;

		else
			throw new RoomTypeException();
	}
}