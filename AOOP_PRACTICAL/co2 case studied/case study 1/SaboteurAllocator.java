package casestudy1;

import java.time.Instant;
import java.util.*;

/**
 * Saboteur Implementation: Intentionally violates contract clauses:
 * 1. Ignores durationHours <= 0 precondition.
 * 2. Allows duplicate vehicle allocation (violates invariant).
 * 3. Returns null instead of throwing IllegalStateException when full.
 */
public class SaboteurAllocator implements SpotAllocator {
    private final Map<String, SpotCategory> spots;
    private final Map<String, String> allocations = new HashMap<>();

    public SaboteurAllocator(Map<String, SpotCategory> spots) {
        this.spots = new LinkedHashMap<>(spots);
    }

    @Override
    public AllocationReceipt allocateSpot(String vehicleId, VehicleType type, int durationHours) {
        // BUG 1: Does not check durationHours <= 0 or duplicate vehicleId!
        for (String spotId : spots.keySet()) {
            if (!allocations.containsValue(spotId)) {
                allocations.put(vehicleId, spotId);
                return new AllocationReceipt("SAB-1", spotId, vehicleId, type, Instant.now());
            }
        }
        // BUG 2: Returns null instead of throwing IllegalStateException
        return null;
    }

    @Override
    public void releaseSpot(String vehicleId) {
        allocations.remove(vehicleId);
    }

    @Override
    public int getAvailableCount() {
        return spots.size() - allocations.size();
    }

    @Override
    public int getTotalCapacity() {
        return spots.size();
    }

    @Override
    public boolean isVehicleParked(String vehicleId) {
        return allocations.containsKey(vehicleId);
    }
}
