import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

enum BookingStatus { CREATED, CONFIRMED, ACTIVE, COMPLETED, CANCELLED }

final class Booking {
    private final int bookingId;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final List<String> notes = new ArrayList<>();
    private BookingStatus status = BookingStatus.CREATED;

    public Booking(int bookingId, LocalDateTime start, LocalDateTime end) {
        if (bookingId <= 0)
            throw new IllegalArgumentException("Invalid booking ID");
        if (start == null || end == null || !end.isAfter(start))
            throw new IllegalArgumentException("End must follow start");
        this.bookingId = bookingId;
        this.start = start;
        this.end = end;
    }

    public void confirm() {
        if (status != BookingStatus.CREATED)
            throw new IllegalStateException("Only CREATED can be confirmed");
        status = BookingStatus.CONFIRMED;
    }
    public void activate() {
        if (status != BookingStatus.CONFIRMED)
            throw new IllegalStateException("Only CONFIRMED can be activated");
        status = BookingStatus.ACTIVE;
    }
    public void complete() {
        if (status != BookingStatus.ACTIVE)
            throw new IllegalStateException("Only ACTIVE can be completed");
        status = BookingStatus.COMPLETED;
    }
    public void addNote(String note) {
        if (note == null || note.isBlank())
            throw new IllegalArgumentException("Note required");
        notes.add(note);
    }
    public List<String> getNotes() { return List.copyOf(notes); }
    public BookingStatus getStatus() { return status; }

    @Override public boolean equals(Object o) {
        return o instanceof Booking b && b.bookingId == bookingId;
    }
    @Override public int hashCode() {
        return Integer.hashCode(bookingId);
    }
    @Override public String toString() {
        return "Booking[" + bookingId + ", " + status + ", " + start
                + " -> " + end + "]";
    }
}

public class ProtectedBookingDemo {
    public static void main(String[] args) {
        LocalDateTime s = LocalDateTime.of(2026, 11, 9, 10, 0);
        Booking b = new Booking(501, s, s.plusHours(2));
        b.confirm();
        b.activate();
        b.addNote("Level 1, near lift");
        b.complete();
        System.out.println(b);
        System.out.println("notes=" + b.getNotes());

        try {
            b.activate();
        } catch (IllegalStateException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
        try {
            b.getNotes().add("hack");
        } catch (UnsupportedOperationException e) {
            System.out.println("Rejected: notes are read-only");
        }
        try {
            new Booking(502, s, s.minusHours(1));
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }
}
