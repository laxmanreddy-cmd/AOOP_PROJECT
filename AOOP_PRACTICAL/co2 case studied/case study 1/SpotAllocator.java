package casestudy1;

import java.time.Instant;
import java.util.Objects;

enum VehicleType { BIKE, CAR, EV, TRUCK }
enum SpotCategory { BIKE, CAR, EV, HEAVY }

record AllocationReceipt(String receiptId, String spotId, String vehicleId, VehicleType vehicleType, Instant allocatedAt) {}

/**
 * Contract interface for Smart Parking Spot Allocation.
 *
 * <p><b>Class Invariants:</b>
 * <ul>
 *   <li>Available spots + Occupied spots == Total capacity at all times.</li>
 *   <li>An OCCUPIED spot always maps to exactly one active vehicleId.</li>
 *   <li>No vehicleId may hold more than one active spot simultaneously.</li>
 * </ul>
 */
public interface SpotAllocator {

    /**
     * Allocates an eligible parking spot to the given vehicle.
     *
     * <p><b>Preconditions:</b>
     * <ul>
     *   <li>{@code vehicleId} must not be null or blank.</li>
     *   <li>{@code type} must not be null.</li>
     *   <li>{@code durationHours} must be strictly between 1 and 24 inclusive.</li>
     *   <li>{@code vehicleId} must not already have an active allocation.</li>
     * </ul>
     *
     * <p><b>Postconditions:</b>
     * <ul>
     *   <li>Returns a non-null {@link AllocationReceipt} referencing the allocated spot.</li>
     *   <li>{@link #getAvailableCount()} decreases by exactly 1.</li>
     *   <li>{@link #isVehicleParked(String)} returns {@code true} for {@code vehicleId}.</li>
     * </ul>
     *
     * <p><b>Additional Clauses:</b>
     * <ul>
     *   <li>Atomic execution: failed allocations leave internal state unchanged.</li>
     *   <li>Vehicle-to-spot compatibility rules are strictly enforced.</li>
     * </ul>
     *
     * @param vehicleId unique vehicle registration number
     * @param type type of vehicle
     * @param durationHours requested parking duration in hours (1..24)
     * @return confirmed allocation receipt
     * @throws IllegalArgumentException if any precondition on arguments is violated
     * @throws IllegalStateException if vehicle is already parked or no compatible spot is available
     */
    AllocationReceipt allocateSpot(String vehicleId, VehicleType type, int durationHours);

    /**
     * Releases a previously allocated spot.
     *
     * @param vehicleId vehicle leaving the parking lot
     * @throws IllegalArgumentException if vehicleId is null/blank
     * @throws IllegalStateException if vehicle is not currently parked
     */
    void releaseSpot(String vehicleId);

    int getAvailableCount();
    int getTotalCapacity();
    boolean isVehicleParked(String vehicleId);
}
