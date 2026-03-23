import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

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

// Inventory
class Inventory {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    public boolean isValidRoomType(String type) {
        return availability.containsKey(type);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void decrementRoom(String type) throws InvalidBookingException {
        int count = availability.get(type);

        if (count <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + type);
        }

        availability.put(type, count - 1);
    }
}

class BookingValidator {

    public static void validate(Reservation reservation, Inventory inventory)
            throws InvalidBookingException {

        if (reservation.getGuestName() == null || reservation.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (reservation.getRoomType() == null || reservation.getRoomType().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty.");
        }

        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + reservation.getRoomType());
        }

        if (inventory.getAvailability(reservation.getRoomType()) <= 0) {
            throw new InvalidBookingException(
                    "Room not available for type: " + reservation.getRoomType());
        }
    }
}

class BookingService {
    private Inventory inventory;

    public BookingService(Inventory inventory) {
        this.inventory = inventory;
    }

    public void confirmBooking(Reservation reservation) {
        try {
            BookingValidator.validate(reservation, inventory);

            inventory.decrementRoom(reservation.getRoomType());

            System.out.println(" Booking Confirmed for " + reservation.getGuestName() +
                    " (Room: " + reservation.getRoomType() + ")");

        } catch (InvalidBookingException e) {
            // Graceful failure
            System.out.println(" Booking Failed: " + e.getMessage());
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {

        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 1);
        inventory.addRoom("Double", 0);

        BookingService service = new BookingService(inventory);

        service.confirmBooking(new Reservation("Pratyush", "Single"));
        service.confirmBooking(new Reservation("Amit", "Suite"));
        service.confirmBooking(new Reservation("Riya", "Double"));
        service.confirmBooking(new Reservation("", "Single"));
    }
}