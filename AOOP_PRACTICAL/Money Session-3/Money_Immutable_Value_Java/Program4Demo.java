// Java 17 or later: record automatically provides value-based equals/hashCode.
record MoneyRecord(long minor, String currency) {
    MoneyRecord {
        if (currency == null || currency.length() != 3) {
            throw new IllegalArgumentException("Currency must be a 3-letter code");
        }
    }

    MoneyRecord add(MoneyRecord other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        return new MoneyRecord(Math.addExact(minor, other.minor), currency);
    }
}

// Entity: identity is the account ID; its balance can change.
final class Account {
    private final String id;
    private MoneyRecord balance;

    Account(String id, MoneyRecord opening) {
        this.id = id;
        this.balance = opening;
    }

    void deposit(MoneyRecord amount) {
        balance = balance.add(amount);
    }

    MoneyRecord balance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Account other && other.id.equals(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

public class Program4Demo {
    public static void main(String[] args) {
        MoneyRecord m1 = new MoneyRecord(500, "INR");
        MoneyRecord m2 = new MoneyRecord(500, "INR");

        System.out.println("m1 == m2      : " + (m1 == m2));
        System.out.println("m1.equals(m2) : " + m1.equals(m2));

        Account a1 = new Account("AC-1", m1);
        Account a2 = new Account("AC-1", new MoneyRecord(9999, "INR"));
        System.out.println("same id, different balance -> equals: " + a1.equals(a2));

        a1.deposit(new MoneyRecord(250, "INR"));
        System.out.println("account balance: " + a1.balance());
        System.out.println("original value : " + m1);
    }
}
