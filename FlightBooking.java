
import java.util.ArrayList;
import java.util.Scanner;

// Abstract class
abstract class Passenger {
    private String name;
    private int age;

    public Passenger(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public abstract double getDiscount();

    public abstract String getPassengerType();
}

// Regular passenger
class RegularPassenger extends Passenger {
    public RegularPassenger(String name, int age) {
        super(name, age);
    }

    @Override
    public double getDiscount() {
        return 0.0;
    }

    @Override
    public String getPassengerType() {
        return "Regular";
    }
}

// Student passenger
class StudentPassenger extends Passenger {
    public StudentPassenger(String name, int age) {
        super(name, age);
    }

    @Override
    public double getDiscount() {
        return 0.10;
    }

    @Override
    public String getPassengerType() {
        return "Student";
    }
}

// Flight class
class Flight {
    private String code;
    private String airline;
    private String source;
    private String destination;
    private int availableSeats;
    private double baseFare;

    public Flight(String code, String airline, String source,
                  String destination, int availableSeats,
                  double baseFare) {
        this.code = code;
        this.airline = airline;
        this.source = source;
        this.destination = destination;
        this.availableSeats = availableSeats;
        this.baseFare = baseFare;
    }

    public String getCode() {
        return code;
    }

    public String getAirline() {
        return airline;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public double getBaseFare() {
        return baseFare;
    }

    public boolean reserveSeats(int tickets) {
        if (tickets > 0 && tickets <= availableSeats) {
            availableSeats -= tickets;
            return true;
        }
        return false;
    }

    public void displayFlight() {
        System.out.printf("%-8s %-12s %-15s %-15s %5d %10.2f%n",
                code, airline, source, destination,
                availableSeats, baseFare);
    }
}

// Booking class
class Booking {
    private String bookingId;
    private Flight flight;
    private Passenger passenger;
    private int tickets;
    private double subtotal;
    private double discount;
    private double totalFare;

    public Booking(String bookingId, Flight flight,
                   Passenger passenger, int tickets) {
        this.bookingId = bookingId;
        this.flight = flight;
        this.passenger = passenger;
        this.tickets = tickets;

        subtotal = flight.getBaseFare() * tickets;
        discount = subtotal * passenger.getDiscount();
        totalFare = subtotal - discount;
    }

    public void displaySummary() {
        System.out.println("\n===== BOOKING SUMMARY =====");
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Flight Code: " + flight.getCode());
        System.out.println("Airline: " + flight.getAirline());
        System.out.println("Route: " + flight.getSource()
                + " to " + flight.getDestination());
        System.out.println("Passenger Name: " + passenger.getName());
        System.out.println("Passenger Age: " + passenger.getAge());
        System.out.println("Passenger Type: "
                + passenger.getPassengerType());
        System.out.println("Tickets: " + tickets);

        System.out.printf("Base Fare per Ticket: Rs. %.2f%n",
                flight.getBaseFare());
        System.out.printf("Subtotal: Rs. %.2f%n", subtotal);
        System.out.printf("Discount: Rs. %.2f%n", discount);
        System.out.printf("Final Amount: Rs. %.2f%n", totalFare);

        System.out.println("===========================");
    }
}

// Main flight booking system
public class FlightBooking {
    private ArrayList<Flight> flights;
    private ArrayList<Booking> bookings;
    private Scanner sc;
    private int bookingCounter = 1001;

    public FlightBooking() {
        flights = new ArrayList<>();
        bookings = new ArrayList<>();
        sc = new Scanner(System.in);

        flights.add(new Flight("PN101", "IndiGo",
                "Pune", "Delhi", 20, 4000));

        flights.add(new Flight("MB102", "AirIndia",
                "Mumbai", "Bengaluru", 15, 5000));

        flights.add(new Flight("PN103", "SpiceJet",
                "Pune", "Hyderabad", 25, 3500));
    }

    // Display all flights
    public void displayFlights() {
        System.out.println("\n========== AVAILABLE FLIGHTS ==========");
        System.out.printf("%-8s %-12s %-15s %-15s %5s %10s%n",
                "Code", "Airline", "Source", "Destination",
                "Seats", "Fare");

        for (Flight flight : flights) {
            flight.displayFlight();
        }
    }

    // Find a flight by code
    public Flight findFlight(String code) {
        for (Flight flight : flights) {
            if (flight.getCode().equalsIgnoreCase(code)) {
                return flight;
            }
        }
        return null;
    }

    // Read a positive integer
    private int readPositiveInt(String message) {
        while (true) {
            System.out.print(message);

            try {
                int value = Integer.parseInt(sc.nextLine().trim());

                if (value > 0) {
                    return value;
                }

                System.out.println("Enter a number greater than zero.");

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    // Book a flight
    public void bookFlight() {
        displayFlights();

        System.out.print("\nEnter flight code: ");
        String code = sc.nextLine().trim();

        Flight flight = findFlight(code);

        if (flight == null) {
            System.out.println("Invalid flight code.");
            return;
        }

        System.out.print("Enter passenger name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Passenger name cannot be blank.");
            return;
        }

        int age = readPositiveInt("Enter passenger age: ");

        System.out.print("Passenger type (Regular/Student): ");
        String type = sc.nextLine().trim();

        Passenger passenger;

        if (type.equalsIgnoreCase("Regular")) {
            passenger = new RegularPassenger(name, age);
        } else if (type.equalsIgnoreCase("Student")) {
            passenger = new StudentPassenger(name, age);
        } else {
            System.out.println("Invalid passenger type.");
            return;
        }

        int tickets = readPositiveInt("Enter number of tickets: ");

        if (tickets > flight.getAvailableSeats()) {
            System.out.println(
                    "Booking failed: Not enough seats available.");
            return;
        }

        // Confirm booking only after validation
        if (flight.reserveSeats(tickets)) {
            String id = "BK" + bookingCounter++;

            Booking booking = new Booking(
                    id, flight, passenger, tickets);

            bookings.add(booking);

            System.out.println("\nBooking confirmed!");
            booking.displaySummary();
        }
    }

    // View the latest confirmed booking
    public void viewBooking() {
        if (bookings.isEmpty()) {
            System.out.println("No confirmed booking found.");
        } else {
            bookings.get(bookings.size() - 1).displaySummary();
        }
    }

    // Main menu
    public void start() {
        while (true) {
            System.out.println("\n===== WELCOME TO KIRAN AIR BOOKING =====");
            System.out.println("1. View Flights");
            System.out.println("2. Book Flight");
            System.out.println("3. View Booking");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    displayFlights();
                    break;

                case "2":
                    bookFlight();
                    break;

                case "3":
                    viewBooking();
                    break;

                case "4":
                    System.out.println(
                            "Thank you for using Kiran Air Booking!");
                    sc.close();
                    return;

                default:
                    System.out.println(
                            "Invalid choice. Please select 1 to 4.");
            }
        }
    }

    public static void main(String[] args) {
        FlightBooking system = new FlightBooking();
        system.start();
    }
}
