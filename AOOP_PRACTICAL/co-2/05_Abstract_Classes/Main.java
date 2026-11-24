// Abstract class
abstract class ParkingSpot {
    protected int spotId;
    protected String location;

    // Constructor
    public ParkingSpot(int spotId, String location) {
        this.spotId = spotId;
        this.location = location;
    }

    // Concrete method
    public void displayDetails() {
        System.out.println("Spot ID: " + spotId);
        System.out.println("Location: " + location);
    }

    // Abstract method
    abstract double calculateFare(int hours);
}

// Car Parking implementation
class CarParkingSpot extends ParkingSpot {
    public CarParkingSpot(int spotId, String location) {
        super(spotId, location);
    }

    @Override
    double calculateFare(int hours) {
        return hours * 50.0;
    }
}

// Bike Parking implementation
class BikeParkingSpot extends ParkingSpot {
    public BikeParkingSpot(int spotId, String location) {
        super(spotId, location);
    }

    @Override
    double calculateFare(int hours) {
        return hours * 20.0;
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        // Creating objects using abstract class reference
        ParkingSpot car = new CarParkingSpot(101, "Bachupally");
        ParkingSpot bike = new BikeParkingSpot(102, "Nizampet");

        // Display car parking details
        System.out.println("=== Car Parking Details ===");
        car.displayDetails();
        System.out.println("Car Fare for 2 hours: Rs. " + car.calculateFare(2));

        // Display bike parking details
        System.out.println("\n=== Bike Parking Details ===");
        bike.displayDetails();
        System.out.println("Bike Fare for 2 hours: Rs. " + bike.calculateFare(2));
    }
}
