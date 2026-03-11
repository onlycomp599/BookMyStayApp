import java.util.LinkedList;
import java.util.Queue;

// Reservation class representing a booking request
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayReservation() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}


// Booking Request Queue (FIFO)
class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add booking request to queue
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    // View all pending requests
    public void displayRequests() {

        System.out.println("\nCurrent Booking Requests (FIFO Order):");

        if (requestQueue.isEmpty()) {
            System.out.println("No booking requests.");
            return;
        }

        for (Reservation r : requestQueue) {
            r.displayReservation();
        }
    }
}


// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Guests submit booking requests
        Reservation r1 = new Reservation("Alice", "Single");
        Reservation r2 = new Reservation("Bob", "Double");
        Reservation r3 = new Reservation("Charlie", "Suite");

        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // Display queued requests
        bookingQueue.displayRequests();
    }
}
