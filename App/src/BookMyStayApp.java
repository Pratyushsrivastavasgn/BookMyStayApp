import java.util.*;

// Reservation (Booking Request)
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

// Inventory Service (State Holder)
class Inventory {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void decrementRoom(String type) {
        availability.put(type, availability.get(type) - 1);
    }
}

// Booking Request Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Booking Service (Core Logic)
class BookingService {
    private Inventory inventory;

    // Track allocated room IDs per room type
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();

    public BookingService(Inventory inventory) {
        this.inventory = inventory;
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        return roomType.substring(0, 1).toUpperCase() + UUID.randomUUID().toString().substring(0, 5);
    }

    public void processBooking(Reservation reservation) {
        String type = reservation.getRoomType();

        // Step 1: Check availability
        if (inventory.getAvailability(type) <= 0) {
            System.out.println("❌ No rooms available for " + type + " (Guest: " + reservation.getGuestName() + ")");
            return;
        }

        // Step 2: Generate unique ID
        String roomId;
        allocatedRooms.putIfAbsent(type, new HashSet<>());

        do {
            roomId = generateRoomId(type);
        } while (allocatedRooms.get(type).contains(roomId)); // uniqueness check

        // Step 3: Assign room (add to set)
        allocatedRooms.get(type).add(roomId);

        // Step 4: Update inventory (atomic step)
        inventory.decrementRoom(type);

        // Step 5: Confirm booking
        System.out.println("✅ Booking Confirmed!");
        System.out.println("Guest: " + reservation.getGuestName());
        System.out.println("Room Type: " + type);
        System.out.println("Room ID: " + roomId);
        System.out.println("---------------------------");
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        // Step 1: Setup Inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 2);
        inventory.addRoom("Double", 1);

        // Step 2: Setup Queue
        BookingRequestQueue queue = new BookingRequestQueue();
        queue.addRequest(new Reservation("Pratyush", "Single"));
        queue.addRequest(new Reservation("Amit", "Single"));
        queue.addRequest(new Reservation("Riya", "Single")); // should fail (only 2 rooms)
        queue.addRequest(new Reservation("Karan", "Double"));

        // Step 3: Booking Service
        BookingService bookingService = new BookingService(inventory);

        // Step 4: Process all requests (FIFO)
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            bookingService.processBooking(r);
        }
    }
}