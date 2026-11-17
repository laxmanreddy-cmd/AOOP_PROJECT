public class ParkingSpot {
    private final int spotId;
    private final String location;
    private boolean available;
    private String vehicleNumber;

    public ParkingSpot(int spotId, String location) {
        if (spotId <= 0) {
            throw new IllegalArgumentException("Spot ID must be positive");
        }
        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException("Location is required");
        }

        this.spotId = spotId;
        this.location = location;
        this.available = true;
        this.vehicleNumber = null;
    }

    public void parkVehicle(String vehicleNumber) {
        if (!available) {
            throw new IllegalStateException("Parking spot is already occupied");
        }
        if (vehicleNumber == null || vehicleNumber.isBlank()) {
            throw new IllegalArgumentException("Vehicle number cannot be empty");
        }

        this.vehicleNumber = vehicleNumber;
        this.available = false;
    }

    public void releaseVehicle() {
        if (available) {
            throw new IllegalStateException("Parking spot is already free");
        }
        this.vehicleNumber = null;
        this.available = true;
    }

    public int getSpotId() {
        return spotId;
    }

    public String getLocation() {
        return location;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void displayDetails() {
        System.out.println("Spot ID: " + spotId);
        System.out.println("Location: " + location);
        System.out.println("Available: " + available);
        System.out.println("Vehicle: " +
                (vehicleNumber == null ? "None" : vehicleNumber));
        System.out.println();
    }
}
