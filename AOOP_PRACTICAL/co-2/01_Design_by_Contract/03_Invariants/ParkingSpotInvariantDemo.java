enum SpotStatus { AVAILABLE, OCCUPIED }

class ParkingSpot {
    private SpotStatus status = SpotStatus.AVAILABLE;
    private String vehicleNumber = null;

    public void parkVehicle(String vehicle) {
        if (status == SpotStatus.OCCUPIED) {
            throw new IllegalStateException("Spot already occupied");
        }
        if (vehicle == null || vehicle.isBlank()) {
            throw new IllegalArgumentException("Invalid vehicle");
        }
        vehicleNumber = vehicle;
        status = SpotStatus.OCCUPIED;
    }

    public SpotStatus getStatus() {
        return status;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }
}

public class ParkingSpotInvariantDemo {
    public static void main(String[] args) {
        ParkingSpot spot = new ParkingSpot();
        spot.parkVehicle("TS09AB1234");
        System.out.println("Status: " + spot.getStatus() + ", Vehicle: " + spot.getVehicleNumber());
    }
}
