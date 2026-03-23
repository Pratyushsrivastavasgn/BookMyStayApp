import java.io.*;
import java.util.*;

// Reservation (Serializable)
class Reservation implements Serializable {
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
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
}

// Inventory (Serializable)
class Inventory implements Serializable {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    public Map<String, Integer> getAvailabilityMap() {
        return availability;
    }
}

// System State (Wrapper for persistence)
class SystemState implements Serializable {
    private Inventory inventory;
    private List<Reservation> bookings;

    public SystemState(Inventory inventory, List<Reservation> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public List<Reservation> getBookings() {
        return bookings;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // Save state
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    // Load state
    public static SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("System state loaded successfully.");
            return state;

        } catch (FileNotFoundException e) {
            System.out.println(" No previous state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state. Starting with safe defaults.");
        }

        return null;
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {

        SystemState loadedState = PersistenceService.load();

        Inventory inventory;
        List<Reservation> bookings;

        if (loadedState != null) {
            inventory = loadedState.getInventory();
            bookings = loadedState.getBookings();
        } else {
            // Fresh start
            inventory = new Inventory();
            inventory.addRoom("Single", 2);
            inventory.addRoom("Double", 1);

            bookings = new ArrayList<>();
        }

        // Step 2: Simulate new booking
        Reservation r1 = new Reservation("Pratyush", "Single", "S101");
        bookings.add(r1);

        System.out.println("\nCurrent Bookings:");
        for (Reservation r : bookings) {
            System.out.println(r.getGuestName() + " | " + r.getRoomType() + " | " + r.getRoomId());
        }

        // Step 3: Save system state before shutdown
        SystemState state = new SystemState(inventory, bookings);
        PersistenceService.save(state);
    }
}