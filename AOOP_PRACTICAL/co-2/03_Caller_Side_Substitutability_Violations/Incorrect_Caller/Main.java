class ParkingSpot {
    public void parkVehicle(String vehicle) {
        System.out.println("Vehicle parked");
    }
}

class CarParkingSpot extends ParkingSpot {}
class BikeParkingSpot extends ParkingSpot {}

// Incorrect caller code
public class Main {
    public static void main(String[] args) {
        ParkingSpot spot = new BikeParkingSpot();
        if (spot instanceof BikeParkingSpot) {
            System.out.println("Checking bike rules");
        } else if (spot instanceof CarParkingSpot) {
            System.out.println("Checking car rules");
        }
        spot.parkVehicle("TS09AB1234");
    }
}
