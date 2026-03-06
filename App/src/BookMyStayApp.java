import java.util.HashMap;
import java.util.Map;



class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 10);
        inventory.put("Double Room", 8);
        inventory.put("Suite", 5);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void updateAvailability(String roomType, int change) {

        int current = inventory.getOrDefault(roomType, 0);
        int updated = current + change;

        if (updated < 0) {
            System.out.println("Error: Not enough rooms available for " + roomType);
            return;
        }

        inventory.put(roomType, updated);
    }

    public void displayInventory() {

        System.out.println("\n===== Current Room Inventory =====");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms available");
        }
    }
}


public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("      Book My Stay - Inventory v3.1");
        System.out.println("=======================================\n");

        RoomInventory inventory = new RoomInventory();

        inventory.displayInventory();

        System.out.println("\nChecking availability for Suite:");
        System.out.println("Available Suites: " + inventory.getAvailability("Suite"));

        System.out.println("\nBooking 2 Suites...");
        inventory.updateAvailability("Suite", -2);

        inventory.displayInventory();

        System.out.println("\nCancelling 1 Suite...");
        inventory.updateAvailability("Suite", 1);

        inventory.displayInventory();
    }
}