package casestudy5;

import java.util.*;

// Domain Models
record Patient(String id, String name, boolean insured, int age) {}
record Bill(String patientId, double baseAmount, double finalAmount, String billingType) {}

// Q2: Single Responsibility Principle (SRP) - Patient Registration
interface PatientRepository {
    void save(Patient patient);
    Optional<Patient> findById(String id);
}

class InMemoryPatientRepository implements PatientRepository {
    private final Map<String, Patient> store = new HashMap<>();
    @Override public void save(Patient p) { store.put(p.id(), p); }
    @Override public Optional<Patient> findById(String id) { return Optional.ofNullable(store.get(id)); }
}

// Q2 & Q4: Open/Closed Principle (OCP) + Strategy Pattern for Billing
interface BillingStrategy {
    String getType();
    double calculateAmount(Patient patient, double baseAmount);
}

class StandardBillingStrategy implements BillingStrategy {
    @Override public String getType() { return "STANDARD"; }
    @Override public double calculateAmount(Patient p, double base) { return base; }
}

class InsuranceBillingStrategy implements BillingStrategy {
    @Override public String getType() { return "INSURANCE_80_PERCENT"; }
    @Override public double calculateAmount(Patient p, double base) { return base * 0.20; }
}

class BillingService {
    public Bill generateBill(Patient patient, double baseAmount, BillingStrategy strategy) {
        double finalAmt = strategy.calculateAmount(patient, baseAmount);
        return new Bill(patient.id(), baseAmount, finalAmt, strategy.getType());
    }
}

// Q3: Interface Segregation Principle (ISP) & Liskov Substitution Principle (LSP)
interface DiagnosticService {
    String runTest(Patient patient);
}

interface ReportableService {
    String generateClinicalSummary(Patient patient);
}

class BloodTestService implements DiagnosticService, ReportableService {
    @Override public String runTest(Patient p) { return "CBC Blood Panel completed for " + p.name(); }
    @Override public String generateClinicalSummary(Patient p) { return "Hemoglobin: Normal"; }
}

class MRIScanService implements DiagnosticService, ReportableService {
    @Override public String runTest(Patient p) { return "Brain MRI completed for " + p.name(); }
    @Override public String generateClinicalSummary(Patient p) { return "MRI Scan: Clear"; }
}

// Q4: Factory Pattern for Diagnostics
class DiagnosticFactory {
    public static DiagnosticService getService(String testType) {
        return switch (testType.toUpperCase()) {
            case "BLOOD" -> new BloodTestService();
            case "MRI" -> new MRIScanService();
            default -> throw new IllegalArgumentException("Unknown diagnostic test: " + testType);
        };
    }
}

// Q4: Observer Pattern for External Integrations & Notifications
interface HospitalEventListener {
    void onPatientEvent(String eventType, Patient patient, String details);
}

class PharmacyNotifier implements HospitalEventListener {
    @Override
    public void onPatientEvent(String eventType, Patient patient, String details) {
        System.out.println("[Pharmacy Observer] Event=" + eventType + " | Patient=" + patient.name() + " | " + details);
    }
}

class ExternalEHRIntegration implements HospitalEventListener {
    @Override
    public void onPatientEvent(String eventType, Patient patient, String details) {
        System.out.println("[External EHR Sync] Synced " + eventType + " for ID " + patient.id());
    }
}

// Q4: Dependency Inversion Principle (DIP) - High-level coordinator depends only on abstractions
class HospitalCoordinator {
    private final PatientRepository patientRepo;
    private final BillingService billingService;
    private final List<HospitalEventListener> listeners = new ArrayList<>();

    public HospitalCoordinator(PatientRepository patientRepo, BillingService billingService) {
        this.patientRepo = patientRepo;
        this.billingService = billingService;
    }

    public void addListener(HospitalEventListener listener) {
        listeners.add(listener);
    }

    public void registerAndDiagnose(Patient patient, String diagnosticType, BillingStrategy billingStrategy) {
        patientRepo.save(patient);
        DiagnosticService diagnostic = DiagnosticFactory.getService(diagnosticType);
        String result = diagnostic.runTest(patient);
        Bill bill = billingService.generateBill(patient, 5000.0, billingStrategy);

        for (HospitalEventListener listener : listeners) {
            listener.onPatientEvent("DIAGNOSTIC_BILLED", patient, result + " | Payable: Rs." + bill.finalAmount());
        }
    }
}

public class HospitalManagementSOLID {
    public static void main(String[] args) {
        HospitalCoordinator hospital = new HospitalCoordinator(
            new InMemoryPatientRepository(),
            new BillingService()
        );
        hospital.addListener(new PharmacyNotifier());
        hospital.addListener(new ExternalEHRIntegration());

        Patient p1 = new Patient("P-101", "Ananya Sharma", true, 34);
        hospital.registerAndDiagnose(p1, "MRI", new InsuranceBillingStrategy());
    }
}
