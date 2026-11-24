package casestudy1;

import java.time.Instant;
import java.util.*;

/**
 * Implementation 1: Strict 1-to-1 VehicleType to SpotCategory matching in ascending ID order.
 */
public class StrictMatchAllocator implements SpotAllocator {
    protected static class Spot {
        final String id;
        final SpotCategory category;
        String occupiedBy;

        Spot(String id, SpotCategory category) {
            this.id = id;
            this.category = category;
        }
    }

    protected final List<Spot> spots = new ArrayList<>();
    protected final Map<String, Spot> activeVehicles = new HashMap<>();

    public StrictMatchAllocator(Map<String, SpotCategory> initialSpots) {
        initialSpots.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(e -> spots.add(new Spot(e.getKey(), e.getValue())));
    }

    protected void validatePreconditions(String vehicleId, VehicleType type, int durationHours) {
        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException("Vehicle ID must not be null or blank");
        }
        if (type == null) {
            throw new IllegalArgumentException("Vehicle type must not be null");
        }
        if (durationHours <= 0 || durationHours > 24) {
            throw new IllegalArgumentException("Duration must be between 1 and 24 hours");
        }
        if (activeVehicles.containsKey(vehicleId)) {
            throw new IllegalStateException("Vehicle already parked: " + vehicleId);
        }
    }

    protected boolean isCompatible(VehicleType vType, SpotCategory sCat) {
        return switch (vType) {
            case BIKE -> sCat == SpotCategory.BIKE;
            case CAR -> sCat == SpotCategory.CAR;
            case EV -> sCat == SpotCategory.EV;
            case TRUCK -> sCat == SpotCategory.HEAVY;
        };
    }

    @Override
    public AllocationReceipt allocateSpot(String vehicleId, VehicleType type, int durationHours) {
        validatePreconditions(vehicleId, type, durationHours);
        int beforeAvailable = getAvailableCount();

        for (Spot spot : spots) {
            if (spot.occupiedBy == null && isCompatible(type, spot.category)) {
                spot.occupiedBy = vehicleId;
                activeVehicles.put(vehicleId, spot);
                AllocationReceipt receipt = new AllocationReceipt(
                    UUID.randomUUID().toString(), spot.id, vehicleId, type, Instant.now()
                );
                // Postcondition assertion
                assert getAvailableCount() == beforeAvailable - 1 : "Postcondition violated: count mismatch";
                assert isVehicleParked(vehicleId) : "Postcondition violated: vehicle not recorded";
                return receipt;
            }
        }
        throw new IllegalStateException("No compatible spot available for " + type);
    }

    @Override
    public void releaseSpot(String vehicleId) {
        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException("Invalid vehicleId");
        }
        Spot spot = activeVehicles.remove(vehicleId);
        if (spot == null) {
            throw new IllegalStateException("Vehicle is not parked: " + vehicleId);
        }
        spot.occupiedBy = null;
    }

    @Override
    public int getAvailableCount() {
        return spots.size() - activeVehicles.size();
    }

    @Override
    public int getTotalCapacity() {
        return spots.size();
    }

    @Override
    public boolean isVehicleParked(String vehicleId) {
        return activeVehicles.containsKey(vehicleId);
    }
}
