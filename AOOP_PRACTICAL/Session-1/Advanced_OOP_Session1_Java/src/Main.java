public class Main {
    public static void main(String[] args) {
        System.out.println("=== WAYPOINT PARKING MANAGEMENT ===");

        // Create and test three parking spots.
        ParkingSpot spot1 = new ParkingSpot(101, "Hyderabad");
        ParkingSpot spot2 = new ParkingSpot(102, "Bachupally");
        ParkingSpot spot3 = new ParkingSpot(103, "Kukatpally");

        spot1.parkVehicle("TS08AB1234");
        spot2.parkVehicle("TS09CD5678");

        spot1.displayDetails();
        spot2.displayDetails();
        spot3.displayDetails();

        // Release a vehicle and show that the spot becomes free.
        System.out.println("Releasing vehicle from Spot 101...");
        spot1.releaseVehicle();
        spot1.displayDetails();

        // Demonstrate the booking's valid state transitions.
        System.out.println("=== BOOKING LIFECYCLE ===");
        Booking booking = new Booking(501, 10, 12);
        booking.confirmBooking();
        booking.activateBooking();
        booking.completeBooking();
        booking.displayStatus();

        // Uncomment one line at a time to test invariant protection:
        // ParkingSpot invalidSpot = new ParkingSpot(-1, "Hyderabad");
        // spot2.parkVehicle("TS10EF0001"); // Already occupied
        // Booking invalidBooking = new Booking(502, 12, 10);
        // booking.activateBooking(); // Completed booking cannot be activated
    }
}
