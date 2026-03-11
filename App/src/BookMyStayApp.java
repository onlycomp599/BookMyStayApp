// Version 3.1

import java.util.HashMap;
import java.util.Map;

// Version 3.0
class RoomInventory {

    private HashMap<String, Integer> inventory;

    // Constructor to initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();
    }

    // Register a room type with its availability
    public void registerRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Get availability of a room type
    public int getAvailability(String roomType) {
        if (inventory.containsKey(roomType)) {
            return inventory.get(roomType);
        }
        return 0;
    }

    // Update room availability
    public void updateAvailability(String roomType, int newCount) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, newCount);
        } else {
            System.out.println("Room type not found: " + roomType);
        }
    }

    // Display all inventory
    public void displayInventory() {
        System.out.println("Current Room Inventory:");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms available");
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Register room types
        inventory.registerRoomType("Single Room", 10);
        inventory.registerRoomType("Double Room", 5);
        inventory.registerRoomType("Suite", 2);

        // Display initial inventory
        inventory.displayInventory();

        System.out.println();

        // Check availability
        System.out.println("Availability of Double Room: "
                + inventory.getAvailability("Double Room"));

        System.out.println();

        // Update availability
        inventory.updateAvailability("Double Room", 4);

        // Display updated inventory
        System.out.println("Inventory after update:");
        inventory.displayInventory();
    }
}
