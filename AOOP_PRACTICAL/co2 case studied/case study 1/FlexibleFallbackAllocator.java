package casestudy1;

import java.util.Map;

/**
 * Implementation 2: Allows smaller vehicles (BIKE) to fall back to CAR spots when BIKE spots are full.
 * Satisfies the contract by preserving all preconditions, postconditions, invariants, and safety rules.
 */
public class FlexibleFallbackAllocator extends StrictMatchAllocator {

    public FlexibleFallbackAllocator(Map<String, SpotCategory> initialSpots) {
        super(initialSpots);
    }

    @Override
    protected boolean isCompatible(VehicleType vType, SpotCategory sCat) {
        if (vType == VehicleType.BIKE && sCat == SpotCategory.CAR) {
            return true; // Safe upward compatibility
        }
        return super.isCompatible(vType, sCat);
    }
}
