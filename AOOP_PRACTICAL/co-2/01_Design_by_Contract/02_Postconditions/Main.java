import java.util.*;

enum SpotStatus { AVAILABLE, OCCUPIED }
enum BookingStatus { PENDING, CONFIRMED }

record CustomerId(int value) {}
record ParkingSpotId(int value) {}
record Money(int amount, String currency) {}

class Booking {
    private BookingStatus status = BookingStatus.PENDING;

    void confirm() {
        status = BookingStatus.CONFIRMED;
    }

    BookingStatus getStatus() {
        return status;
    }
}

class ParkingSpot {
    private SpotStatus status = SpotStatus.AVAILABLE;
    private String vehicleNumber;

    void park(String vehicle) {
        if (status == SpotStatus.OCCUPIED) {
            throw new IllegalStateException("Spot occupied");
        }
        vehicleNumber = vehicle;
        status = SpotStatus.OCCUPIED;
    }

    SpotStatus getStatus() {
        return status;
    }
}

class Waypoint {
    private final Map<Integer, ParkingSpot> spots = new HashMap<>();
    private final List<Booking> bookings = new ArrayList<>();

    Waypoint() {
        spots.put(201, new ParkingSpot());
    }

    Booking reserve(CustomerId c, ParkingSpotId s, String vehicle, int hours, Money fee) {
        // Preconditions
        if (vehicle == null || vehicle.isBlank()) {
            throw new IllegalArgumentException("Invalid vehicle");
        }
        if (hours <= 0) {
            throw new IllegalArgumentException("Hours must be > 0");
        }
        ParkingSpot spot = spots.get(s.value());
        if (spot == null) {
            throw new IllegalArgumentException("No such spot");
        }

        boolean paid = fee.amount() > 0; // simulated payment
        spot.park(vehicle);

        Booking booking = new Booking();
        booking.confirm();
        bookings.add(booking);

        // Postconditions
        if (!bookings.contains(booking) || !paid
                || booking.getStatus() != BookingStatus.CONFIRMED
                || spot.getStatus() != SpotStatus.OCCUPIED) {
            throw new IllegalStateException("Postcondition bad");
        }

        return booking;
    }
}

public class Main {
    public static void main(String[] args) {
        Waypoint waypoint = new Waypoint();
        Booking booking = waypoint.reserve(
            new CustomerId(101),
            new ParkingSpotId(201),
            "TS09AB1234",
            2,
            new Money(5000, "INR")
        );
        System.out.println(booking.getStatus());
    }
}
