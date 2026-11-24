public class ParkingReservation {
    public void reserveParking(String vehicleNumber, int hours) {
        if (vehicleNumber == null || vehicleNumber.isBlank()) {
            throw new IllegalArgumentException("Invalid vehicle number");
        }
        if (hours <= 0) {
            throw new IllegalArgumentException("Hours must be positive");
        }
        System.out.println("Parking reserved for " + vehicleNumber + " for " + hours + " hours.");
    }

    public static void main(String[] args) {
        ParkingReservation reservation = new ParkingReservation();
        reservation.reserveParking("TS09AB1234", 2);
    }
}
