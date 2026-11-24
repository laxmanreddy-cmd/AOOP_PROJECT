# Case Study 4: E-Commerce Order Processing System – Contract Testing and Debugging

## Q1: PaymentProcessor Contract
- **Preconditions**: `orderId != null && !orderId.isBlank()`, `amount > 0`, `currency != null && currency.length() == 3`.
- **Postconditions**: Returns non-null `PaymentResult`; if `status == SUCCESS`, `transactionId != null && !transactionId.isBlank()` and `chargedAmount == amount`.
- **Invariants**: Processor never mutates input parameters; duplicate `orderId` payments are rejected or idempotent without double charging.

## Q4 & Q5: Runtime Error Analysis & Design Autopsy
- `NullPointerException` when `orderId` is null -> **Caller violation** if caller passed null, or **Implementer violation** if implementer failed to validate preconditions with `IllegalArgumentException`.
- Returning `PaymentResult(SUCCESS, null, 0)` -> **Implementer postcondition violation**.
- `DefectivePaymentProcessor` accepts negative amounts, overwrites previous transactions silently, and returns `null` on gateway timeout instead of a failed `PaymentResult` or controlled exception.
