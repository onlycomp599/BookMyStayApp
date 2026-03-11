// Version 2.1 – Refactored Implementation
// Demonstrates Abstract Class, Inheritance, Polymorphism and Static Availability

// Abstract Room Class
abstract class Room {
    private int beds;
    private int size;
    private double price;

    public Room(int beds, int size, double price) {
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public int getBeds() {
        return beds;
    }

    public int getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    // Abstract method
    public abstract String getRoomType();

    // Common display method
    public void displayRoomDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sq ft");
        System.out.println("Price: $" + price);
    }
}

// Single Room Class
class SingleRoom extends Room {

    public SingleRoom() {
        super(1, 200, 100);
    }

    @Override
    public String getRoomType() {
        return "Single Room";
    }
}

// Double Room Class
class DoubleRoom extends Room {

    public DoubleRoom() {
        super(2, 350, 180);
    }

    @Override
    public String getRoomType() {
        return "Double Room";
    }
}

// Suite Room Class
class SuiteRoom extends Room {

    public SuiteRoom() {
        super(3, 600, 350);
    }

    @Override
    public String getRoomType() {
        return "Suite Room";
    }
}

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=== Book My Stay - Room Availability ===\n");

        // Create room objects using polymorphism
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability variables
        int singleAvailable = 10;
        int doubleAvailable = 6;
        int suiteAvailable = 3;

        // Display room information
        single.displayRoomDetails();
        System.out.println("Available Rooms: " + singleAvailable);
        System.out.println("--------------------------");

        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + doubleAvailable);
        System.out.println("--------------------------");

        suite.displayRoomDetails();
        System.out.println("Available Rooms: " + suiteAvailable);
        System.out.println("--------------------------");

        System.out.println("\nApplication Terminated.");
    }
}
