import java.util.*;

// Reservation (Represents booking request)
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

// Booking Request Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add request (enqueue)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all requests (read-only)
    public void displayQueue() {
        System.out.println("\nBooking Request Queue:\n");

        if (queue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        for (Reservation r : queue) {
            System.out.println("Guest: " + r.getGuestName() +
                    " | Room Type: " + r.getRoomType());
        }
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulating multiple booking requests (arrival order)
        bookingQueue.addRequest(new Reservation("Pratyush", "Single"));
        bookingQueue.addRequest(new Reservation("Amit", "Suite"));
        bookingQueue.addRequest(new Reservation("Riya", "Double"));

        // Display queue (FIFO order)
        bookingQueue.displayQueue();
    }
}