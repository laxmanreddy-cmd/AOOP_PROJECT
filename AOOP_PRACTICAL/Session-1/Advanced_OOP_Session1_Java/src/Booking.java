public class Booking {
    public enum Status {
        CREATED, CONFIRMED, ACTIVE, COMPLETED
    }

    private final int bookingId;
    private final int startTime;
    private final int endTime;
    private Status status;

    public Booking(int bookingId, int startTime, int endTime) {
        if (bookingId <= 0) {
            throw new IllegalArgumentException("Invalid booking ID");
        }
        if (endTime <= startTime) {
            throw new IllegalArgumentException(
                    "End time must be after start time");
        }

        this.bookingId = bookingId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = Status.CREATED;
    }

    public void confirmBooking() {
        if (status != Status.CREATED) {
            throw new IllegalStateException(
                    "Only created bookings can be confirmed");
        }
        status = Status.CONFIRMED;
    }

    public void activateBooking() {
        if (status != Status.CONFIRMED) {
            throw new IllegalStateException(
                    "Only confirmed bookings can be activated");
        }
        status = Status.ACTIVE;
    }

    public void completeBooking() {
        if (status != Status.ACTIVE) {
            throw new IllegalStateException(
                    "Only active bookings can be completed");
        }
        status = Status.COMPLETED;
    }

    public void displayStatus() {
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Start time: " + startTime);
        System.out.println("End time: " + endTime);
        System.out.println("Status: " + status);
    }
}
