# Case Study 1: Smart Parking Management System – Design by Contract

## Q1: Preconditions, Postconditions, Class Invariants & 6 Additional Clauses
- **Preconditions**:
  1. `vehicleId != null && !vehicleId.isBlank()`
  2. `vehicleType != null`
  3. `durationHours > 0 && durationHours <= 24`
  4. Vehicle is not already parked in the system.
- **Postconditions**:
  1. Returned `AllocationReceipt` is non-null with status `ALLOCATED`.
  2. Assigned spot is marked `OCCUPIED` and bound to `vehicleId`.
  3. Total available spots decreases by exactly 1.
  4. Allocation timestamp is recorded.
- **Class Invariants**:
  1. `0 <= occupiedSpots <= totalSpots`
  2. Every `OCCUPIED` spot has a non-null `vehicleId`; every `AVAILABLE` spot has `vehicleId == null`.
  3. No `vehicleId` occupies more than one spot simultaneously.
- **Six Additional Contract Clauses (Beyond Method Signature)**:
  1. **Idempotency / Duplicate Rejection**: Re-allocating an already parked `vehicleId` throws `IllegalStateException`.
  2. **Compatibility Rule**: `BIKE` can park in `BIKE` or `CAR` spots (in fallback implementations), `EV` requires `EV` charger spots, `TRUCK` requires `HEAVY` spots.
  3. **Thread-Safety / Atomicity**: Allocation must fail atomically without partial state updates if no spot is available.
  4. **Time-Window Restriction**: Reservations exceeding 24 hours are rejected with `IllegalArgumentException`.
  5. **Determinism / Ordering**: Within the same priority tier, spots are allocated in ascending `spotId` order (unless overridden by strategy).
  6. **Exception Transparency**: Caller errors throw `IllegalArgumentException`; capacity/state conflicts throw `IllegalStateException`.

## Q5: Caller vs. Implementer Contract Violations
- **Caller Violations**: Passing `null`/`blank` `vehicleId`, passing `durationHours <= 0`, requesting allocation for a vehicle already parked.
- **Implementer Violations**: Double-booking an occupied spot, failing to decrement available spot count, assigning a `TRUCK` to a `BIKE` spot, returning `null` instead of throwing `IllegalStateException` when full.
