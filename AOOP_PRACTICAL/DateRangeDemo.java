import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

// Value Object: a hotel stay includes check-in and excludes check-out.
final class DateRange {
    private final LocalDate checkIn;
    private final LocalDate checkOut;

    private DateRange(LocalDate checkIn, LocalDate checkOut) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public static DateRange of(LocalDate checkIn, LocalDate checkOut) {
        Objects.requireNonNull(checkIn, "Check-in is required");
        Objects.requireNonNull(checkOut, "Check-out is required");

        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException(
                "Check-out must be after check-in"
            );
        }
        return new DateRange(checkIn, checkOut);
    }

    // Half-open range: [check-in, check-out)
    public boolean overlaps(DateRange other) {
        Objects.requireNonNull(other, "Other stay is required");
        return checkIn.isBefore(other.checkOut)
            && other.checkIn.isBefore(checkOut);
    }

    public long numberOfNights() {
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }
}

public class DateRangeDemo {
    public static void main(String[] args) {
        DateRange first = DateRange.of(
            LocalDate.of(2026, 6, 10),
            LocalDate.of(2026, 6, 12)
        );

        // Consecutive stay: first guest checks out as next guest checks in.
        DateRange next = DateRange.of(
            LocalDate.of(2026, 6, 12),
            LocalDate.of(2026, 6, 14)
        );

        // This stay overlaps the first stay for one night.
        DateRange conflict = DateRange.of(
            LocalDate.of(2026, 6, 11),
            LocalDate.of(2026, 6, 13)
        );

        System.out.println("First stay nights: " + first.numberOfNights());
        System.out.println("Consecutive stays overlap: " + first.overlaps(next));
        System.out.println("Conflicting stays overlap: " + first.overlaps(conflict));

        try {
            DateRange.of(
                LocalDate.of(2026, 6, 15),
                LocalDate.of(2026, 6, 14)
            );
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid stay rejected: " + e.getMessage());
        }
    }
}
