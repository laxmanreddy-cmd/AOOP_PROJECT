# Case Study 5: Hospital Management System – The Cost of Change and SOLID Principles

## Q1 & Q5: Sources of Coupling & Architectural Comparison
- **Original Monolithic Design**:
  - One god class handled patient registration, appointment scheduling, `if/else` billing logic, and report formatting.
  - High coupling meant adding a new billing tier or diagnostic lab required editing core hospital classes, risking regressions.
- **Redesigned SOLID Architecture**:
  - **SRP**: Separated `PatientRegistrationService`, `BillingService`, and `ReportGenerator`.
  - **OCP & Strategy Pattern**: `BillingStrategy` interface (`StandardBilling`, `InsuranceBilling`, `SeniorCitizenBilling`) allows new pricing rules without modifying `BillingService`.
  - **LSP & ISP**: Segregated `DiagnosticService` from `InPatientCareService` so outpatient radiology labs do not inherit irrelevant ward-bed methods.
  - **DIP, Factory & Observer Patterns**: High-level `HospitalFacade` depends on abstractions; `DiagnosticFactory` creates services; `HospitalEventPublisher` notifies external EHR, Pharmacy, and Billing observers automatically.
