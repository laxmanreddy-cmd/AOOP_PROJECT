package casestudy4;

import java.util.*;

enum PaymentStatus { SUCCESS, FAILED }

record PaymentResult(PaymentStatus status, String transactionId, double chargedAmount, String message) {}

// Q1: PaymentProcessor Interface Contract
interface PaymentProcessor {
    PaymentResult processPayment(String orderId, double amount, String currency);
}

// Q2: Compliant Implementations
class CreditCardProcessor implements PaymentProcessor {
    private final Set<String> processedOrders = new HashSet<>();

    @Override
    public PaymentResult processPayment(String orderId, double amount, String currency) {
        validate(orderId, amount, currency);
        if (!processedOrders.add(orderId)) {
            throw new IllegalStateException("Duplicate order payment: " + orderId);
        }
        return new PaymentResult(PaymentStatus.SUCCESS, "CC-" + UUID.randomUUID(), amount, "Card approved");
    }

    private void validate(String orderId, double amount, String currency) {
        if (orderId == null || orderId.isBlank()) throw new IllegalArgumentException("Invalid orderId");
        if (amount <= 0) throw new IllegalArgumentException("Amount must be > 0");
        if (currency == null || currency.length() != 3) throw new IllegalArgumentException("Invalid currency");
    }
}

class UPIProcessor implements PaymentProcessor {
    private final Set<String> processedOrders = new HashSet<>();

    @Override
    public PaymentResult processPayment(String orderId, double amount, String currency) {
        if (orderId == null || orderId.isBlank()) throw new IllegalArgumentException("Invalid orderId");
        if (amount <= 0 || amount > 100000) throw new IllegalArgumentException("UPI amount out of range");
        if (currency == null || !currency.equals("INR")) throw new IllegalArgumentException("UPI supports INR only");
        if (!processedOrders.add(orderId)) throw new IllegalStateException("Duplicate order: " + orderId);
        return new PaymentResult(PaymentStatus.SUCCESS, "UPI-" + UUID.randomUUID(), amount, "UPI settled");
    }
}

class NetBankingProcessor implements PaymentProcessor {
    private final Set<String> processedOrders = new HashSet<>();

    @Override
    public PaymentResult processPayment(String orderId, double amount, String currency) {
        if (orderId == null || orderId.isBlank()) throw new IllegalArgumentException("Invalid orderId");
        if (amount <= 0) throw new IllegalArgumentException("Amount must be > 0");
        if (currency == null || currency.length() != 3) throw new IllegalArgumentException("Invalid currency");
        if (!processedOrders.add(orderId)) throw new IllegalStateException("Duplicate order: " + orderId);
        return new PaymentResult(PaymentStatus.SUCCESS, "NB-" + UUID.randomUUID(), amount, "NetBanking cleared");
    }
}

// Q5: Intentionally Defective Payment Implementation (for Autopsy)
class DefectivePaymentProcessor implements PaymentProcessor {
    @Override
    public PaymentResult processPayment(String orderId, double amount, String currency) {
        // Defect 1: Accepts negative or zero amounts
        // Defect 2: Returns null transactionId on SUCCESS (violates postcondition)
        // Defect 3: Allows duplicate orderId charges
        return new PaymentResult(PaymentStatus.SUCCESS, null, 0.0, "OK");
    }
}

// Q3: Reusable Contract-Testing Suite
public class PaymentContractDemo {
    public static void runContractTests(String name, PaymentProcessor processor) {
        System.out.println("=== Contract Suite: " + name + " ===");

        // 1. Precondition check: negative amount
        try {
            processor.processPayment("ORD-1", -100.0, "INR");
            System.out.println("[FAIL] Accepted negative amount");
        } catch (IllegalArgumentException e) {
            System.out.println("[PASS] Rejected negative amount");
        }

        // 2. Postcondition check: valid payment
        try {
            PaymentResult res = processor.processPayment("ORD-2", 1500.0, "INR");
            if (res == null || res.status() != PaymentStatus.SUCCESS
                    || res.transactionId() == null || res.transactionId().isBlank()
                    || res.chargedAmount() != 1500.0) {
                System.out.println("[FAIL] Postcondition violated on valid payment: " + res);
            } else {
                System.out.println("[PASS] Valid payment postconditions satisfied");
            }
        } catch (Exception e) {
            System.out.println("[FAIL] Unexpected exception: " + e);
        }

        // 3. Idempotency / Duplicate order check
        try {
            processor.processPayment("ORD-2", 1500.0, "INR");
            System.out.println("[FAIL] Allowed duplicate orderId");
        } catch (IllegalStateException e) {
            System.out.println("[PASS] Prevented duplicate orderId");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        runContractTests("CreditCardProcessor", new CreditCardProcessor());
        runContractTests("UPIProcessor", new UPIProcessor());
        runContractTests("NetBankingProcessor", new NetBankingProcessor());
        runContractTests("DefectivePaymentProcessor", new DefectivePaymentProcessor());
    }
}
