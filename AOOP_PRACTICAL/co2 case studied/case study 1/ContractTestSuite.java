package casestudy1;

import java.util.Map;
import java.util.function.Function;

/**
 * Common Contract-Testing Suite that evaluates any SpotAllocator implementation.
 */
public class ContractTestSuite {

    public static void runContractTests(String implName, Function<Map<String, SpotCategory>, SpotAllocator> factory) {
        System.out.println("=== Testing Contract on: " + implName + " ===");
        Map<String, SpotCategory> layout = Map.of(
            "S1", SpotCategory.CAR,
            "S2", SpotCategory.BIKE
        );

        // Test 1: Precondition - Invalid Duration
        try {
            SpotAllocator alloc = factory.apply(layout);
            alloc.allocateSpot("TS09AB1234", VehicleType.CAR, 0);
            System.out.println("[FAIL] Precondition test failed: accepted 0 hours");
        } catch (IllegalArgumentException expected) {
            System.out.println("[PASS] Precondition rejected invalid duration");
        }

        // Test 2: Postcondition & Invariant - Valid Allocation
        try {
            SpotAllocator alloc = factory.apply(layout);
            int initialAvail = alloc.getAvailableCount();
            AllocationReceipt receipt = alloc.allocateSpot("TS09AB1234", VehicleType.CAR, 2);
            if (receipt == null || alloc.getAvailableCount() != initialAvail - 1 || !alloc.isVehicleParked("TS09AB1234")) {
                System.out.println("[FAIL] Postcondition/Invariant failed after valid allocation");
            } else {
                System.out.println("[PASS] Postcondition & Invariant verified on valid allocation");
            }
        } catch (Exception e) {
            System.out.println("[FAIL] Unexpected exception on valid allocation: " + e);
        }

        // Test 3: Clause - Duplicate Vehicle Rejection
        try {
            SpotAllocator alloc = factory.apply(layout);
            alloc.allocateSpot("TS09AB1234", VehicleType.CAR, 2);
            alloc.allocateSpot("TS09AB1234", VehicleType.CAR, 2);
            System.out.println("[FAIL] Duplicate vehicle allocation was allowed");
        } catch (IllegalStateException expected) {
            System.out.println("[PASS] Duplicate vehicle rejected");
        }

        // Test 4: Clause - Full Capacity Throws IllegalStateException
        try {
            SpotAllocator alloc = factory.apply(layout);
            alloc.allocateSpot("CAR-1", VehicleType.CAR, 1);
            alloc.allocateSpot("CAR-2", VehicleType.CAR, 1);
            System.out.println("[FAIL] Capacity overflow did not throw IllegalStateException");
        } catch (IllegalStateException expected) {
            System.out.println("[PASS] Full capacity threw IllegalStateException");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        runContractTests("StrictMatchAllocator", StrictMatchAllocator::new);
        runContractTests("FlexibleFallbackAllocator", FlexibleFallbackAllocator::new);
        runContractTests("PriorityReverseAllocator", PriorityReverseAllocator::new);
        runContractTests("SaboteurAllocator", SaboteurAllocator::new);
    }
}
