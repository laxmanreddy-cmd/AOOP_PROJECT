package casestudy1;

import java.time.Instant;
import java.util.*;

/**
 * Implementation 3: Allocates nearest-to-exit (reverse ID) compatible spots first.
 * Satisfies all contract invariants and postconditions.
 */
public class PriorityReverseAllocator extends StrictMatchAllocator {

    public PriorityReverseAllocator(Map<String, SpotCategory> initialSpots) {
        super(initialSpots);
        spots.sort((a, b) -> b.id.compareTo(a.id));
    }
}
