MONEY AS AN IMMUTABLE VALUE - JAVA PROGRAMS

Requirements:
- JDK 17 or later
- No external libraries are required.

Files:
1. MoneyDemo.java     Complete Money immutable value class and demonstration.
2. Program1Demo.java  Why double should not be used for money.
3. Program2Demo.java  Detecting long overflow with Math.addExact().
4. Program5Demo.java  BigDecimal equals(), compareTo(), HashSet and TreeSet.
5. Program4Demo.java  Identity vs value using a Money record and Account entity.

HOW TO RUN (Windows):
1. Extract this ZIP file.
2. Open the extracted folder in VS Code or Command Prompt.
3. Open Terminal in that folder.
4. Compile the program you want:
      javac MoneyDemo.java
5. Run it:
      java MoneyDemo

For the other examples, replace MoneyDemo with the filename without .java:
      javac Program1Demo.java
      java Program1Demo

      javac Program2Demo.java
      java Program2Demo

      javac Program5Demo.java
      java Program5Demo

      javac Program4Demo.java
      java Program4Demo

IMPORTANT:
- Each public class must be saved in a file with the same name.
- MoneyDemo.java contains the complete implementation from the case study.
- Money is stored in whole minor units (paise/cents) using long.
- JDK 17 is required because Program4Demo uses a record.
