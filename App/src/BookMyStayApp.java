import java.util.*;

// Booking Request Model
class BookingRequest {
    String customerName;
    String roomType;

    public BookingRequest(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}

// Booking Service
class BookingService {
    private Queue<BookingRequest> requestQueue = new LinkedList<>();
    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> roomTypeMap = new HashMap<>();
    private InventoryService inventoryService;

    private int roomCounter = 1;

    public BookingService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // Add booking request
    public void addRequest(BookingRequest request) {
        requestQueue.offer(request);
    }

    // Generate Unique Room ID
    private String generateRoomId(String roomType) {
        String roomId;
        do {
            roomId = roomType.substring(0, 1).toUpperCase() + roomCounter++;
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
    }

    // Process Bookings (FIFO)
    public void processBookings() {
        while (!requestQueue.isEmpty()) {
            BookingRequest request = requestQueue.poll();

            System.out.println("\nProcessing booking for: " + request.customerName);

            if (inventoryService.isAvailable(request.roomType)) {

                // Atomic operation start
                String roomId = generateRoomId(request.roomType);

                allocatedRoomIds.add(roomId);

                roomTypeMap.putIfAbsent(request.roomType, new HashSet<>());
                roomTypeMap.get(request.roomType).add(roomId);

                inventoryService.decrement(request.roomType);
                // Atomic operation end

                System.out.println("Booking CONFIRMED");
                System.out.println("Room Type: " + request.roomType);
                System.out.println("Assigned Room ID: " + roomId);

            } else {
                System.out.println("Booking FAILED - No rooms available for " + request.roomType);
            }
        }
    }

    public void displayAllocations() {
        System.out.println("\nRoom Allocations:");
        for (String type : roomTypeMap.keySet()) {
            System.out.println(type + " -> " + roomTypeMap.get(type));
        }
    }
}

// Main Class
public class BookMystayApp {
    public static void main(String[] args) {

        InventoryService inventoryService = new InventoryService();
        BookingService bookingService = new BookingService(inventoryService);

        // Sample Requests
        bookingService.addRequest(new BookingRequest("Alice", "Single"));
        bookingService.addRequest(new BookingRequest("Bob", "Double"));
        bookingService.addRequest(new BookingRequest("Charlie", "Single"));
        bookingService.addRequest(new BookingRequest("David", "Suite"));
        bookingService.addRequest(new BookingRequest("Eve", "Suite")); // should fail

        // Process bookings
        bookingService.processBookings();

        // Show results
        bookingService.displayAllocations();
        inventoryService.displayInventory();
    }
}