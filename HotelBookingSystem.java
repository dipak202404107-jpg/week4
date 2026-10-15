
import java.util.ArrayList;
import java.util.Scanner;

class Booking {
    int bookingId;
    String customerName;
    int roomNumber;
    int nights;
    double pricePerNight;

    Booking(int bookingId, String customerName,
            int roomNumber, int nights, double pricePerNight) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.nights = nights;
        this.pricePerNight = pricePerNight;
    }

    double getTotalCost() {
        return nights * pricePerNight;
    }

    void display() {
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Customer: " + customerName);
        System.out.println("Room Number: " + roomNumber);
        System.out.println("Nights: " + nights);
        System.out.printf("Total Cost: Rs. %.2f%n", getTotalCost());
        System.out.println("---------------------------");
    }
}

public class HotelBookingSystem {
    static ArrayList<Booking> bookings = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static int nextId = 1;

    static boolean roomIsBooked(int roomNumber) {
        for (Booking b : bookings) {
            if (b.roomNumber == roomNumber) {
                return true;
            }
        }
        return false;
    }

    static void addBooking() {
        try {
            System.out.print("Enter customer name: ");
            String name = sc.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Name cannot be empty.");
                return;
            }

            System.out.print("Enter room number: ");
            int room = Integer.parseInt(sc.nextLine());

            if (room <= 0) {
                System.out.println("Invalid room number.");
                return;
            }

            if (roomIsBooked(room)) {
                System.out.println("Room is already booked.");
                return;
            }

            System.out.print("Enter number of nights: ");
            int nights = Integer.parseInt(sc.nextLine());

            if (nights <= 0) {
                System.out.println("Nights must be positive.");
                return;
            }

            System.out.print("Price per night: ");
            double price = Double.parseDouble(sc.nextLine());

            if (!Double.isFinite(price) || price <= 0) {
                System.out.println("Invalid room price.");
                return;
            }

            Booking booking = new Booking(
                nextId++, name, room, nights, price
            );

            bookings.add(booking);

            System.out.println("Booking successful!");
            booking.display();

        } catch (NumberFormatException e) {
            System.out.println("Enter valid numeric values.");
        }
    }

    static void viewBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        System.out.println("\n--- All Hotel Bookings ---");

        for (Booking b : bookings) {
            b.display();
        }
    }

    static void cancelBooking() {
        try {
            System.out.print("Enter booking ID to cancel: ");
            int id = Integer.parseInt(sc.nextLine());

            for (Booking b : bookings) {
                if (b.bookingId == id) {
                    bookings.remove(b);
                    System.out.println("Booking cancelled.");
                    return;
                }
            }

            System.out.println("Booking ID not found.");

        } catch (NumberFormatException e) {
            System.out.println("Enter a valid booking ID.");
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Hotel Booking System ===");
            System.out.println("1. Add Booking");
            System.out.println("2. View Bookings");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    addBooking();
                    break;

                case "2":
                    viewBookings();
                    break;

                case "3":
                    cancelBooking();
                    break;

                case "4":
                    System.out.println("Program closed.");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
