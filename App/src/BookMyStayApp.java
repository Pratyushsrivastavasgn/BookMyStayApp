import java.util.*;

// Reservation (Confirmed Booking)
class Reservation {
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean isCancelled;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        isCancelled = true;
    }
}

// Inventory
class Inventory {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    public void incrementRoom(String type) {
        availability.put(type, availability.getOrDefault(type, 0) + 1);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }
}

// Booking History
class BookingHistory {
    private Map<String, Reservation> bookings = new HashMap<>();

    public void addBooking(Reservation r) {
        bookings.put(r.getRoomId(), r);
    }

    public Reservation getBooking(String roomId) {
        return bookings.get(roomId);
    }
}

// Cancellation Service (Rollback Logic)
class CancellationService {
    private Inventory inventory;
    private BookingHistory history;

    // Stack for rollback tracking (LIFO)
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(Inventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(String roomId) {

        Reservation reservation = history.getBooking(roomId);

        // Validation
        if (reservation == null) {
            System.out.println("❌ Cancellation Failed: Booking does not exist.");
            return;
        }

        if (reservation.isCancelled()) {
            System.out.println("❌ Cancellation Failed: Already cancelled.");
            return;
        }

        // Step 1: Push to rollback stack
        rollbackStack.push(roomId);

        // Step 2: Restore inventory
        inventory.incrementRoom(reservation.getRoomType());

        // Step 3: Mark as cancelled
        reservation.cancel();

        // Step 4: Confirm cancellation
        System.out.println("✅ Booking Cancelled Successfully!");
        System.out.println("Room ID: " + roomId);
        System.out.println("Guest: " + reservation.getGuestName());
        System.out.println("-----------------------------");
    }

    // Optional: Show rollback stack
    public void showRollbackStack() {
        System.out.println("\nRollback Stack (Recent Cancellations): " + rollbackStack);
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        // Setup Inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 1);

        // Setup Booking History
        BookingHistory history = new BookingHistory();

        Reservation r1 = new Reservation("Pratyush", "Single", "S101");
        history.addBooking(r1);

        // Cancellation Service
        CancellationService service = new CancellationService(inventory, history);

        // Valid cancellation
        service.cancelBooking("S101");

        // Duplicate cancellation
        service.cancelBooking("S101");

        // Invalid booking
        service.cancelBooking("S999");

        // Show rollback history
        service.showRollbackStack();
    }
}