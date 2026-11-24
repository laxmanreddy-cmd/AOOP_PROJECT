package casestudy2;

// Q4: Redesigned Hierarchy separating read/deposit accounts from withdrawable accounts
interface Account {
    String getAccountNumber();
    double getBalance();
    void deposit(double amount);
}

interface WithdrawableAccount extends Account {
    double getMaxWithdrawableAmount();
    void withdraw(double amount);
}

abstract class BaseAccount implements Account {
    private final String accountNumber;
    protected double balance;

    protected BaseAccount(String accountNumber, double initialBalance) {
        if (initialBalance < 0) throw new IllegalArgumentException("Negative initial balance");
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    @Override
    public String getAccountNumber() { return accountNumber; }

    @Override
    public double getBalance() { return balance; }

    @Override
    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit must be positive");
        balance += amount;
    }
}

class SavingsAccount extends BaseAccount implements WithdrawableAccount {
    private final double minBalance;

    public SavingsAccount(String accNo, double initialBalance, double minBalance) {
        super(accNo, initialBalance);
        this.minBalance = minBalance;
    }

    @Override
    public double getMaxWithdrawableAmount() {
        return Math.max(0, balance - minBalance);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0 || amount > getMaxWithdrawableAmount()) {
            throw new IllegalArgumentException("Exceeds withdrawable limit");
        }
        balance -= amount;
    }
}

class CurrentAccount extends BaseAccount implements WithdrawableAccount {
    private final double overdraftLimit;

    public CurrentAccount(String accNo, double initialBalance, double overdraftLimit) {
        super(accNo, initialBalance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public double getMaxWithdrawableAmount() {
        return balance + overdraftLimit;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0 || amount > getMaxWithdrawableAmount()) {
            throw new IllegalArgumentException("Exceeds overdraft limit");
        }
        balance -= amount;
    }
}

// FixedDepositAccount implements Account (NOT WithdrawableAccount) - no broken contract!
class FixedDepositAccount extends BaseAccount {
    private final int tenureMonths;

    public FixedDepositAccount(String accNo, double initialBalance, int tenureMonths) {
        super(accNo, initialBalance);
        this.tenureMonths = tenureMonths;
    }

    public double calculateMaturityAmount(double annualRate) {
        return balance * Math.pow(1 + annualRate / 100.0, tenureMonths / 12.0);
    }
}

// Q5: Substitutability Test Cases
public class LSPRedesignedBanking {
    public static void verifyWithdrawableSubstitutability(WithdrawableAccount account) {
        double before = account.getBalance();
        double max = account.getMaxWithdrawableAmount();
        if (max >= 500) {
            account.withdraw(500);
            assert account.getBalance() == before - 500 : "Postcondition failed";
            System.out.println("[PASS] Substitutability verified for " + account.getClass().getSimpleName()
                + " | New Balance: " + account.getBalance());
        }
    }

    public static void main(String[] args) {
        WithdrawableAccount savings = new SavingsAccount("SAV-01", 10000, 2000);
        WithdrawableAccount current = new CurrentAccount("CUR-01", 1000, 5000);
        Account fd = new FixedDepositAccount("FD-01", 50000, 12);

        verifyWithdrawableSubstitutability(savings);
        verifyWithdrawableSubstitutability(current);

        fd.deposit(5000);
        System.out.println("[PASS] FixedDepositAccount safely substituted as Account | Balance: " + fd.getBalance());
    }
}
