import java.util.*;

// Add-On Service Model
class AddOnService {
    String serviceName;
    double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + cost + ")";
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add service to a reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return serviceMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total additional cost
    public double calculateTotalCost(String reservationId) {
        double total = 0;
        for (AddOnService s : getServices(reservationId)) {
            total += s.cost;
        }
        return total;
    }

    // Display services
    public void displayServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("Add-On Services for Reservation " + reservationId + ":");
        for (AddOnService s : services) {
            System.out.println(" - " + s);
        }

        System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId));
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        AddOnServiceManager manager = new AddOnServiceManager();

        // Assume these reservation IDs came from Use Case 6
        String reservation1 = "S1";
        String reservation2 = "D2";

        // Create Services
        AddOnService breakfast = new AddOnService("Breakfast", 250);
        AddOnService wifi = new AddOnService("WiFi", 100);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 500);
        AddOnService spa = new AddOnService("Spa", 800);

        // Guest selects services
        manager.addService(reservation1, breakfast);
        manager.addService(reservation1, wifi);
        manager.addService(reservation1, spa);

        manager.addService(reservation2, airportPickup);

        // Display services & cost
        System.out.println("\n--- Reservation 1 ---");
        manager.displayServices(reservation1);

        System.out.println("\n--- Reservation 2 ---");
        manager.displayServices(reservation2);
    }
}