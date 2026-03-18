import java.util.*;

// Reservation Model
class Reservation {
    String reservationId;
    String customerName;
    String roomType;
    boolean isCancelled;

    public Reservation(String reservationId, String customerName, String roomType) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.roomType = roomType;
        this.isCancelled = false;
    }

    @Override
    public String toString() {
        return reservationId + " | " + customerName + " | " + roomType +
                (isCancelled ? " (CANCELLED)" : " (ACTIVE)");
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 1);
        inventory.put("Double", 1);
        inventory.put("Suite", 1);
    }

    public boolean isAvailable(String type) {
        return inventory.getOrDefault(type, 0) > 0;
    }

    public void decrement(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }

    public void increment(String type) {
        inventory.put(type, inventory.get(type) + 1);
    }

    public void showInventory() {
        System.out.println("Inventory: " + inventory);
    }
}

// Booking Service
class BookingService {
    private Map<String, Reservation> reservations = new HashMap<>();
    private Set<String> allocatedRooms = new HashSet<>();
    private Stack<String> rollbackStack = new Stack<>();
    private InventoryService inventory;
    private int counter = 1;

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    // Generate Room ID
    private String generateRoomId(String type) {
        String id = type.charAt(0) + String.valueOf(counter++);
        allocatedRooms.add(id);
        return id;
    }

    // Confirm Booking
    public void book(String name, String roomType) {
        if (!inventory.isAvailable(roomType)) {
            System.out.println("Booking FAILED for " + name + " (No rooms available)");
            return;
        }

        String roomId = generateRoomId(roomType);
        inventory.decrement(roomType);

        Reservation r = new Reservation(roomId, name, roomType);
        reservations.put(roomId, r);

        System.out.println("Booking CONFIRMED: " + r);
    }

    // Cancel Booking (Rollback)
    public void cancel(String reservationId) {
        System.out.println("\nAttempting cancellation for: " + reservationId);

        // Validation
        if (!reservations.containsKey(reservationId)) {
            System.out.println("Cancellation FAILED: Reservation does not exist");
            return;
        }

        Reservation r = reservations.get(reservationId);

        if (r.isCancelled) {
            System.out.println("Cancellation FAILED: Already cancelled");
            return;
        }

        // Rollback process (LIFO concept)
        rollbackStack.push(reservationId);

        // Controlled mutation
        r.isCancelled = true;
        allocatedRooms.remove(reservationId);
        inventory.increment(r.roomType);

        System.out.println("Cancellation SUCCESS: " + r);
    }

    public void showAllReservations() {
        System.out.println("\n--- All Reservations ---");
        for (Reservation r : reservations.values()) {
            System.out.println(r);
        }
    }

    public void showRollbackStack() {
        System.out.println("\nRollback Stack (Recent Releases): " + rollbackStack);
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        BookingService service = new BookingService(inventory);

        // Bookings
        service.book("Alice", "Single");
        service.book("Bob", "Double");
        service.book("Charlie", "Suite");

        // Cancel operations
        service.cancel("S1");   // valid
        service.cancel("S1");   // duplicate cancel
        service.cancel("X99");  // invalid ID

        // Final State
        service.showAllReservations();
        service.showRollbackStack();
        inventory.showInventory();
    }
}