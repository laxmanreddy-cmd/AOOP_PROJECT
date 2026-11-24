# 🚚 Smart Fleet Management System

A centralized, Java-based application designed to streamline the management of an organization's vehicles, drivers, trips, maintenance schedules, and vehicle utilization. Developed as part of the **Advance Object-Oriented Programming (25CS2205E)** course at **Koneru Lakshmaiah Education Foundation (KLH University)**.

---

## 📖 Overview

In many organizations, fleet operations rely on manual logs or spreadsheets, leading to duplicate assignments, outdated vehicle statuses, missed servicing, and inaccurate trip records. The **Smart Fleet Management System** solves these challenges by providing a reliable, modular Java application that automates availability tracking, enforces business validation rules, and maintains persistent operational records.

### ✨ Key Features
* **Vehicle & Driver Management:** Maintain structured, centralized records for drivers and diverse vehicle types (Cars, Buses, Trucks).
* **Intelligent Trip Assignment:** Assign available vehicles and drivers to trips while automatically preventing overlapping schedules.
* **Data Validation & Integrity:** Enforce strict rules—such as blocking vehicles under maintenance from trip assignments and preventing odometer readings from decreasing.
* **Maintenance Scheduling:** Schedule routine servicing and automatically update vehicle availability statuses.
* **Utilization Reporting:** Generate insightful fleet-utilization and maintenance reports.
* **Persistent Storage:** Save and load records across sessions using Java File I/O (or database connectivity).

---

## 🧠 Object-Oriented Programming & Java Concepts Applied

This project demonstrates core and advanced Java programming principles:

| Concept / Feature | Implementation in Project |
| :--- | :--- |
| **Classes & Objects** | Models real-world entities including `Vehicle`, `Driver`, `Trip`, and `Maintenance`. |
| **Encapsulation** | Secures sensitive data fields as `private` with controlled access via getters, setters, and validation methods. |
| **Inheritance & Polymorphism** | Extends a base `Vehicle` class into specialized subclasses (`Car`, `Bus`, `Truck`) with type-specific behaviors. |
| **Abstraction & Interfaces** | Defines common fleet operations and exposes essential functionality while hiding implementation details. |
| **Collections Framework** | Utilizes `ArrayList`, `HashMap`, and other collections for dynamic, in-memory record management. |
| **Exception Handling** | Catches and handles invalid operations (e.g., overlapping trips, invalid odometer inputs) gracefully. |
| **File I/O / Persistence** | Ensures fleet records are saved and retrieved reliably between program executions. |

---

## 🛠️ Tech Stack & Tools

* **Language:** Java (JDK / JVM)
* **Core APIs:** Java Collections Framework, Exception Handling, Java File I/O
* **IDEs:** IntelliJ IDEA / Eclipse / VS Code
* **Version Control:** Git & GitHub

---

## 🚀 Getting Started

### Prerequisites
* Java Development Kit (JDK) 11 or higher installed on your system.
* Git installed (optional, for cloning).

### Installation & Execution
1. **Clone the repository:**
   ```bash
   git clone https://github.com/laxmanreddy-cmd/AOOP_PROJECT.git
   cd AOOP_PROJECT
   ```

2. **Compile the Java source files:**
   *(Update the path below if your source files are inside a `src/` directory)*
   ```bash
   javac *.java
   ```

3. **Run the application:**
   *(Replace `Main` with your actual entry-point class name)*
   ```bash
   java Main
   ```

---

## 👥 Team Details (Section 1 — Team 22)

| Roll Number | Student Name | Role / Contribution |
| :--- | :--- | :--- |
| `2520030097` | **P. Laxman Reddy** | Vehicle and driver record management |
| `2520030136` | **P. Pranaw Reddy** | Trip assignment, trip logging, and validation |
| `2520030239` | **Ch. Manikanta** | Maintenance scheduling and status updates |
| `2520030506` | **Y. Yashwanth** | Reports, testing, and system integration |

---

## 📄 Course Information
* **Course:** Advance Object-Oriented Programming (`25CS2205E`)
* **Academic Year:** 2026–27, Term-II
* **Institution:** Koneru Lakshmaiah Education Foundation (Deemed to be University), Hyderabad