import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Booking Request
class BookingRequest {
    String customerName;
    String roomType;

    public BookingRequest(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }
}

// Inventory Service with validation
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 1);
        inventory.put("Double", 1);
        inventory.put("Suite", 0);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public boolean isAvailable(String roomType) {
        return inventory.get(roomType) > 0;
    }

    public void decrement(String roomType) throws InvalidBookingException {
        int count = inventory.get(roomType);

        if (count <= 0) {
            throw new InvalidBookingException("Inventory error: No rooms available for " + roomType);
        }

        inventory.put(roomType, count - 1);
    }

    public void showInventory() {
        System.out.println("Inventory: " + inventory);
    }
}

// Validator (Fail-Fast)
class BookingValidator {

    public static void validate(BookingRequest request, InventoryService inventory)
            throws InvalidBookingException {

        // Check null/empty name
        if (request.customerName == null || request.customerName.trim().isEmpty()) {
            throw new InvalidBookingException("Customer name cannot be empty");
        }

        // Validate room type
        if (!inventory.isValidRoomType(request.roomType)) {
            throw new InvalidBookingException("Invalid room type: " + request.roomType);
        }

        // Check availability
        if (!inventory.isAvailable(request.roomType)) {
            throw new InvalidBookingException("No rooms available for " + request.roomType);
        }
    }
}

// Booking Service
class BookingService {
    private InventoryService inventory;
    private Set<String> allocatedRooms = new HashSet<>();
    private int counter = 1;

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    // Generate unique room ID
    private String generateRoomId(String type) {
        String id;
        do {
            id = type.charAt(0) + String.valueOf(counter++);
        } while (allocatedRooms.contains(id));

        allocatedRooms.add(id);
        return id;
    }

    // Process booking with validation
    public void processBooking(BookingRequest request) {
        try {
            // FAIL-FAST validation
            BookingValidator.validate(request, inventory);

            // Allocation (only if valid)
            String roomId = generateRoomId(request.roomType);
            inventory.decrement(request.roomType);

            System.out.println("\nBooking SUCCESS");
            System.out.println("Customer: " + request.customerName);
            System.out.println("Room Type: " + request.roomType);
            System.out.println("Room ID: " + roomId);

        } catch (InvalidBookingException e) {
            // Graceful failure
            System.out.println("\nBooking FAILED: " + e.getMessage());
        }
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        BookingService service = new BookingService(inventory);

        // Test Cases
        BookingRequest b1 = new BookingRequest("Alice", "Single");   // valid
        BookingRequest b2 = new BookingRequest("", "Double");        // invalid name
        BookingRequest b3 = new BookingRequest("Bob", "Deluxe");     // invalid type
        BookingRequest b4 = new BookingRequest("Charlie", "Suite");  // no availability
        BookingRequest b5 = new BookingRequest("David", "Single");   // may fail after 1 booking

        // Process all
        service.processBooking(b1);
        service.processBooking(b2);
        service.processBooking(b3);
        service.processBooking(b4);
        service.processBooking(b5);

        // Final inventory
        System.out.println();
        inventory.showInventory();
    }
}