import java.util.*;

// Booking Request
class BookingRequest {
    String customerName;
    String roomType;

    public BookingRequest(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }
}

// Thread-Safe Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 1);
        inventory.put("Double", 1);
    }

    // synchronized = critical section
    public synchronized boolean bookRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public synchronized void showInventory() {
        System.out.println("Final Inventory: " + inventory);
    }
}

// Shared Booking Queue
class BookingQueue {
    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.offer(request);
    }

    public synchronized BookingRequest getRequest() {
        return queue.poll();
    }
}

// Booking Processor (Thread)
class BookingProcessor extends Thread {
    private BookingQueue queue;
    private InventoryService inventory;
    private static int counter = 1;

    public BookingProcessor(BookingQueue queue, InventoryService inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    private synchronized String generateRoomId(String type) {
        return type.charAt(0) + String.valueOf(counter++);
    }

    @Override
    public void run() {
        while (true) {
            BookingRequest request;

            // synchronized access to queue
            synchronized (queue) {
                request = queue.getRequest();
            }

            if (request == null) break;

            System.out.println(Thread.currentThread().getName() +
                    " processing " + request.customerName);

            // Critical section (inventory update)
            boolean success = inventory.bookRoom(request.roomType);

            if (success) {
                String roomId = generateRoomId(request.roomType);

                System.out.println("SUCCESS: " + request.customerName +
                        " got " + roomId);
            } else {
                System.out.println("FAILED: No " + request.roomType +
                        " room for " + request.customerName);
            }
        }
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        BookingQueue queue = new BookingQueue();

        // Multiple requests (simulate users)
        queue.addRequest(new BookingRequest("Alice", "Single"));
        queue.addRequest(new BookingRequest("Bob", "Single"));   // conflict
        queue.addRequest(new BookingRequest("Charlie", "Double"));
        queue.addRequest(new BookingRequest("David", "Double")); // conflict

        // Multiple threads
        BookingProcessor t1 = new BookingProcessor(queue, inventory);
        BookingProcessor t2 = new BookingProcessor(queue, inventory);

        t1.setName("Thread-1");
        t2.setName("Thread-2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inventory.showInventory();
    }
}