import java.util.*;

// Reservation (Confirmed Booking)
class Reservation {
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

// Booking History (List → ordered storage)
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    // Add confirmed booking
    public void addBooking(Reservation reservation) {
        history.add(reservation);
    }

    // Read-only access
    public List<Reservation> getAllBookings() {
        return history;
    }
}

// Reporting Service (Read-only)
class BookingReportService {
    private BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    // Display all bookings
    public void showAllBookings() {
        System.out.println("\n📋 Booking History:\n");

        List<Reservation> bookings = bookingHistory.getAllBookings();

        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : bookings) {
            System.out.println("Guest: " + r.getGuestName() +
                    " | Room Type: " + r.getRoomType() +
                    " | Room ID: " + r.getRoomId());
        }
    }

    // Generate summary report
    public void generateSummaryReport() {
        System.out.println("\n📊 Booking Summary Report:\n");

        Map<String, Integer> countByType = new HashMap<>();

        for (Reservation r : bookingHistory.getAllBookings()) {
            countByType.put(r.getRoomType(),
                    countByType.getOrDefault(r.getRoomType(), 0) + 1);
        }

        for (String type : countByType.keySet()) {
            System.out.println("Room Type: " + type +
                    " | Total Booked: " + countByType.get(type));
        }
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        // Step 1: Booking History
        BookingHistory history = new BookingHistory();

        // Step 2: Simulating confirmed bookings
        history.addBooking(new Reservation("Pratyush", "Single", "S101"));
        history.addBooking(new Reservation("Amit", "Double", "D201"));
        history.addBooking(new Reservation("Riya", "Single", "S102"));
        history.addBooking(new Reservation("Karan", "Suite", "SU301"));

        // Step 3: Reporting Service
        BookingReportService reportService = new BookingReportService(history);

        // Step 4: Admin views history
        reportService.showAllBookings();

        // Step 5: Admin views summary
        reportService.generateSummaryReport();
    }
}