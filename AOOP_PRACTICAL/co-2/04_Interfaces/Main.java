// Interface
interface PaymentMethod {
    void pay(Money amount);
}

// Money class
class Money {
    private double amount;
    private String currency;

    public Money(double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}

// UPI Payment implementation
class UPIPayment implements PaymentMethod {
    @Override
    public void pay(Money amount) {
        System.out.println("Paid using UPI: " + amount);
    }
}

// Card Payment implementation
class CardPayment implements PaymentMethod {
    @Override
    public void pay(Money amount) {
        System.out.println("Paid using Card: " + amount);
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        PaymentMethod payment = new UPIPayment();
        payment.pay(new Money(5000, "INR"));

        payment = new CardPayment();
        payment.pay(new Money(5000, "INR"));
    }
}
