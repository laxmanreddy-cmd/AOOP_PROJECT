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
        System.out.println("UPI payment: " + amount);
    }
}

// Card Payment implementation
class CardPayment implements PaymentMethod {
    @Override
    public void pay(Money amount) {
        System.out.println("Card payment: " + amount);
    }
}

// Booking Service
class BookingService {
    private final PaymentMethod paymentMethod;

    // Constructor Injection
    public BookingService(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void confirmBooking(Money amount) {
        paymentMethod.pay(amount);
        System.out.println("Booking confirmed");
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        // Creating UPI payment object
        PaymentMethod payment = new UPIPayment();
        // Injecting payment object into BookingService
        BookingService service = new BookingService(payment);
        // Confirm booking
        service.confirmBooking(new Money(10000, "INR"));
    }
}
