import java.util.*;

// Reservation Model
class Reservation {
    String reservationId;
    String customerName;
    String roomType;

    public Reservation(String reservationId, String customerName, String roomType) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return reservationId + " | " + customerName + " | " + roomType;
    }
}

// Booking History (Stores confirmed bookings)
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        history.add(reservation); // maintains insertion order
    }

    // Get all bookings
    public List<Reservation> getAllReservations() {
        return history;
    }
}

// Reporting Service
class BookingReportService {

    // Display all bookings
    public void showAllBookings(List<Reservation> reservations) {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    // Generate summary report
    public void generateSummary(List<Reservation> reservations) {
        Map<String, Integer> roomCount = new HashMap<>();

        for (Reservation r : reservations) {
            roomCount.put(r.roomType,
                    roomCount.getOrDefault(r.roomType, 0) + 1);
        }

        System.out.println("\n--- Booking Summary Report ---");
        for (String type : roomCount.keySet()) {
            System.out.println(type + " Rooms Booked: " + roomCount.get(type));
        }

        System.out.println("Total Reservations: " + reservations.size());
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulating confirmed bookings (from Use Case 6)
        history.addReservation(new Reservation("S1", "Alice", "Single"));
        history.addReservation(new Reservation("D2", "Bob", "Double"));
        history.addReservation(new Reservation("S3", "Charlie", "Single"));
        history.addReservation(new Reservation("SU4", "David", "Suite"));

        // Admin views booking history
        reportService.showAllBookings(history.getAllReservations());

        // Admin generates report
        reportService.generateSummary(history.getAllReservations());
    }
}