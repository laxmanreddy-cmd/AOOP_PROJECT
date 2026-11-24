package casestudy2;

// Violating Hierarchy
class BadBankAccount {
    protected double balance;

    public BadBankAccount(double balance) {
        this.balance = balance;
    }

    public void withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            throw new IllegalArgumentException("Invalid withdrawal");
        }
        balance -= amount;
    }

    public double getBalance() { return balance; }
}

// Violation 1: Strengthened precondition
class BadSavingsAccount extends BadBankAccount {
    public BadSavingsAccount(double balance) { super(balance); }

    @Override
    public void withdraw(double amount) {
        if (balance - amount < 5000) { // Strengthened precondition!
            throw new IllegalStateException("Minimum balance 5000 required");
        }
        super.withdraw(amount);
    }
}

// Violation 2: Unexpected exception
class BadFixedDepositAccount extends BadBankAccount {
    public BadFixedDepositAccount(double balance) { super(balance); }

    @Override
    public void withdraw(double amount) {
        throw new UnsupportedOperationException("Cannot withdraw from FD before maturity!");
    }
}

public class LSPViolationDemo {
    public static void processWithdrawal(BadBankAccount account, double amount) {
        account.withdraw(amount); // Fails at runtime when substituted!
    }

    public static void main(String[] args) {
        BadBankAccount fd = new BadFixedDepositAccount(10000);
        try {
            processWithdrawal(fd, 1000);
        } catch (UnsupportedOperationException e) {
            System.out.println("LSP Violation Caught: " + e.getMessage());
        }
    }
}
