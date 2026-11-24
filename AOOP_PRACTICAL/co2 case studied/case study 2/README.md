# Case Study 2: Online Banking System – Liskov Substitution Principle (LSP)

## Q1 & Q2: Applying LSP & Direction of Preconditions/Postconditions
- **LSP Rule**: Subtypes (`SavingsAccount`, `CurrentAccount`, `FixedDepositAccount`) must honor the behavioral contract of their declared supertype.
- **Contravariance of Preconditions (Weaken or keep equal)**: A subclass cannot require *more* than the parent (e.g., if `BankAccount.withdraw(amount)` allows any `amount <= balance`, `SavingsAccount` cannot suddenly add a new precondition throwing `UnsupportedOperationException` or rejecting valid amounts below a stricter hidden limit unless specified in the parent contract).
- **Covariance of Postconditions (Strengthen or keep equal)**: After `withdraw(amount)`, `newBalance == oldBalance - amount` must hold; a subclass cannot leave the balance unchanged (empty override).

## Q3: Four LSP Violation Signals
1. **Unexpected Exception**: `FixedDepositAccount.withdraw()` throwing `UnsupportedOperationException` when extending a `BankAccount` that promises `withdraw()`.
2. **Empty Overridden Method**: Overriding `withdraw()` with `{ /* do nothing */ }` breaks the postcondition that balance decreases.
3. **Additional Preconditions**: `SavingsAccount` adding a minimum-balance guard (`balance - amount >= 5000`) when parent `BankAccount` only requires `balance >= amount`.
4. **`instanceof` Checks**: Caller writing `if (acc instanceof FixedDepositAccount)` indicates the base abstraction lies about capabilities.
