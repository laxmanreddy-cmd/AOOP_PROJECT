ADVANCED OBJECT-ORIENTED PROGRAMMING - SESSION 1
Java implementation based on the uploaded Session-1 PDF.

FILES
-----
src/ParkingSpot.java  - Parking spot object and its invariants
src/Booking.java      - Booking lifecycle and temporal invariants
src/Main.java         - Main method and demonstration of three parking spots

REQUIREMENTS
------------
JDK 11 or later

HOW TO RUN (Windows Command Prompt / VS Code terminal)
-------------------------------------------------------
1. Extract the ZIP file.
2. Open the extracted Advanced_OOP_Session1_Java folder in VS Code.
3. Open Terminal > New Terminal.
4. Compile:
   javac -d out src\*.java
5. Run:
   java -cp out Main

EXPECTED OUTPUT
---------------
=== WAYPOINT PARKING MANAGEMENT ===
Spot ID: 101
Location: Hyderabad
Available: false
Vehicle: TS08AB1234

Spot ID: 102
Location: Bachupally
Available: false
Vehicle: TS09CD5678

Spot ID: 103
Location: Kukatpally
Available: true
Vehicle: None

Releasing vehicle from Spot 101...
Spot ID: 101
Location: Hyderabad
Available: true
Vehicle: None

=== BOOKING LIFECYCLE ===
Booking ID: 501
Start time: 10
End time: 12
Status: COMPLETED

CONCEPTS COVERED
----------------
1. Classes, objects, attributes and methods
2. Encapsulation using private fields
3. Constructor validation
4. Single-field invariant: spotId must be positive
5. Multi-field invariant:
   - Available spot => vehicleNumber is null
   - Occupied spot => vehicleNumber is not null
6. Temporal invariants and booking state transitions:
   CREATED -> CONFIRMED -> ACTIVE -> COMPLETED
7. IllegalArgumentException for invalid input
8. IllegalStateException for invalid operations
9. Reachable state space: public methods prevent invalid spot/booking states

NOTE
----
The PDF asks students to create three ParkingSpot objects and test them.
The Main class does this. The releaseVehicle method is included to demonstrate
the return from OCCUPIED to FREE while preserving the multi-field invariant.
