import java.util.*;

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

class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add booking request (enqueue)
    public void addBookingRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added: "
                + reservation.getGuestName() + " -> " + reservation.getRoomType());
    }

    public void displayRequests() {
        System.out.println("\nCurrent Booking Queue:");

        if (requestQueue.isEmpty()) {
            System.out.println("No booking requests available.");
            return;
        }

        for (Reservation r : requestQueue) {
            System.out.println("Guest: " + r.getGuestName() +
                    ", Room Type: " + r.getRoomType());
        }
    }
}


public class BookMyStayApp {
    public static void main(String[] args) {

        BookingRequestQueue queue = new BookingRequestQueue();


        queue.addBookingRequest(new Reservation("Pratyush", "Single"));
        queue.addBookingRequest(new Reservation("Amit", "Double"));
        queue.addBookingRequest(new Reservation("Riya", "Suite"));
        queue.displayRequests();
    }
}