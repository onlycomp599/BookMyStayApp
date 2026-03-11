import java.util.HashMap;
import java.util.Map;

// Inventory class (state holder)
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    // register room types
    public void registerRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // read-only access
    public int getAvailability(String roomType) {
        if (inventory.containsKey(roomType)) {
            return inventory.get(roomType);
        }
        return 0;
    }

    // expose inventory for search (read-only usage)
    public Map<String, Integer> getInventory() {
        return inventory;
    }
}


// Room domain model
class Room {

    private String type;
    private double price;
    private String amenities;

    public Room(String type, double price, String amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: $" + price);
        System.out.println("Amenities: " + amenities);
    }
}


// Search service (read-only operations)
class RoomSearchService {

    private RoomInventory inventory;
    private HashMap<String, Room> rooms;

    public RoomSearchService(RoomInventory inventory, HashMap<String, Room> rooms) {
        this.inventory = inventory;
        this.rooms = rooms;
    }

    // search available rooms
    public void searchAvailableRooms() {

        System.out.println("Available Rooms:");

        for (String roomType : rooms.keySet()) {

            int available = inventory.getAvailability(roomType);

            // defensive check
            if (available > 0) {
                Room room = rooms.get(roomType);

                room.displayDetails();
                System.out.println("Available Count: " + available);
                System.out.println("---------------------------");
            }
        }
    }
}


// Main setup
public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        inventory.registerRoomType("Single", 5);
        inventory.registerRoomType("Double", 0);
        inventory.registerRoomType("Suite", 2);

        // Room domain objects
        HashMap<String, Room> rooms = new HashMap<>();

        rooms.put("Single", new Room("Single", 100, "WiFi, TV"));
        rooms.put("Double", new Room("Double", 150, "WiFi, TV, Mini Bar"));
        rooms.put("Suite", new Room("Suite", 300, "WiFi, TV, Mini Bar, Jacuzzi"));

        // Search service
        RoomSearchService searchService = new RoomSearchService(inventory, rooms);

        // Guest initiates search
        searchService.searchAvailableRooms();
    }
}