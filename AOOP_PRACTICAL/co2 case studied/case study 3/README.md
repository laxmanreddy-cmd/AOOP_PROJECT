# Case Study 3: University Course Registration System – Interfaces, Abstract Classes, and Sealed Classes

## Q1 & Q5: Architectural Evaluation
- **Abstract Class (`AbstractCourse`)**: Holds shared state (`courseCode`, `title`, `credits`) and common validation logic.
- **Role Interfaces (`Registerable`, `Assessable`, `PrerequisiteCheckable`)**: Decouple capabilities so components depend only on what they invoke (Interface Segregation Principle), simplifying mock creation in unit tests.
- **Sealed Class (`CourseCategory`)**: Restricts the domain taxonomy (`RegularCourse`, `LabCourse`, `ElectiveCourse`, `ResearchCourse`) so exhaustive `switch` expressions guarantee compile-time completeness without `default` branches.
- **Cost of Change**: Adding a new capability (e.g., `Auditable`) requires zero changes to existing courses; adding a new sealed subtype triggers compiler guidance everywhere exhaustive pattern matching is used.
