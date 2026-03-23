import java.util.*;

class Room {
    private String type;
    private double price;
    private String amenities;
    public Room(String type, double price, String amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }
    public String getType() {
        return type;
    }
    public double getPrice() {
        return price;
    }
    public String getAmenities() {
        return amenities;
    }
}
class Inventory {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public Set<String> getAllRoomTypes() {
        return availability.keySet();
    }
}

class SearchService {
    private Inventory inventory;
    private Map<String, Room> roomData;

    public SearchService(Inventory inventory, Map<String, Room> roomData) {
        this.inventory = inventory;
        this.roomData = roomData;
    }

    public void searchAvailableRooms() {
        System.out.println("Available Rooms:\n");

        for (String type : inventory.getAllRoomTypes()) {
            int available = inventory.getAvailability(type);

            if (available > 0) {
                Room room = roomData.get(type);

                System.out.println("Room Type: " + room.getType());
                System.out.println("Price: ₹" + room.getPrice());
                System.out.println("Amenities: " + room.getAmenities());
                System.out.println("Available: " + available);
                System.out.println("------------------------");
            }
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {

        // Step 1: Create Inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 5);
        inventory.addRoom("Double", 0); // unavailable
        inventory.addRoom("Suite", 3);

        Map<String, Room> roomData = new HashMap<>();
        roomData.put("Single", new Room("Single", 2000, "WiFi, AC"));
        roomData.put("Double", new Room("Double", 3500, "WiFi, AC, TV"));
        roomData.put("Suite", new Room("Suite", 5000, "WiFi, AC, TV, Mini Bar"));

        SearchService searchService = new SearchService(inventory, roomData);

        searchService.searchAvailableRooms();
    }
}