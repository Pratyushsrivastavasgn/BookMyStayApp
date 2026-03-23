import java.util.*;

// Reservation
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
}

// Shared Inventory (Critical Resource)
class Inventory {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    // Synchronized → critical section
    public synchronized boolean allocateRoom(String type) {
        int count = availability.getOrDefault(type, 0);

        if (count > 0) {
            availability.put(type, count - 1);
            return true;
        }
        return false;
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }
}

// Shared Booking Queue
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation getRequest() {
        return queue.poll();
    }
}

// Booking Processor (Thread)
class BookingProcessor extends Thread {
    private BookingQueue queue;
    private Inventory inventory;

    public BookingProcessor(BookingQueue queue, Inventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {

            Reservation r;

            // Critical section → fetching request
            synchronized (queue) {
                r = queue.getRequest();
            }

            if (r == null) break;

            // Critical section → allocation
            boolean success = inventory.allocateRoom(r.getRoomType());

            if (success) {
                System.out.println("✅ Booking Confirmed for " + r.getGuestName() +
                        " (" + r.getRoomType() + ") by " + Thread.currentThread().getName());
            } else {
                System.out.println("❌ Booking Failed for " + r.getGuestName() +
                        " (" + r.getRoomType() + ") by " + Thread.currentThread().getName());
            }
        }
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        // Shared resources
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 2);

        BookingQueue queue = new BookingQueue();

        // Simulate multiple guest requests
        queue.addRequest(new Reservation("Pratyush", "Single"));
        queue.addRequest(new Reservation("Amit", "Single"));
        queue.addRequest(new Reservation("Riya", "Single")); // should fail
        queue.addRequest(new Reservation("Karan", "Single")); // should fail

        // Multiple threads (concurrent processing)
        BookingProcessor t1 = new BookingProcessor(queue, inventory);
        BookingProcessor t2 = new BookingProcessor(queue, inventory);

        t1.setName("Thread-1");
        t2.setName("Thread-2");

        t1.start();
        t2.start();
    }
}