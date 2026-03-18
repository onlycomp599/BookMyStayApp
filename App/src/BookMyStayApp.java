import java.io.*;
import java.util.*;

// Reservation Model (Serializable)
class Reservation implements Serializable {
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

// Wrapper class to store full system state
class SystemState implements Serializable {
    Map<String, Integer> inventory;
    List<Reservation> bookingHistory;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save state to file
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("\nSystem state SAVED to file.");

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load state from file
    public static SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("System state LOADED from file.");
            return (SystemState) ois.readObject();

        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading data. Starting with safe defaults.");
        }

        // Return default state if error occurs
        Map<String, Integer> defaultInventory = new HashMap<>();
        defaultInventory.put("Single", 2);
        defaultInventory.put("Double", 2);
        defaultInventory.put("Suite", 1);

        return new SystemState(defaultInventory, new ArrayList<>());
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        // STEP 1: Load previous state (Recovery)
        SystemState state = PersistenceService.load();

        Map<String, Integer> inventory = state.inventory;
        List<Reservation> history = state.bookingHistory;

        System.out.println("\n--- Current Inventory ---");
        System.out.println(inventory);

        System.out.println("\n--- Booking History ---");
        for (Reservation r : history) {
            System.out.println(r);
        }

        // STEP 2: Simulate new booking
        System.out.println("\nAdding new booking...");

        if (inventory.get("Single") > 0) {
            String newId = "S" + (history.size() + 1);

            Reservation newRes = new Reservation(newId, "NewUser", "Single");
            history.add(newRes);

            inventory.put("Single", inventory.get("Single") - 1);

            System.out.println("Booking Added: " + newRes);
        } else {
            System.out.println("No Single rooms available");
        }

        // STEP 3: Save updated state
        PersistenceService.save(new SystemState(inventory, history));
    }
}