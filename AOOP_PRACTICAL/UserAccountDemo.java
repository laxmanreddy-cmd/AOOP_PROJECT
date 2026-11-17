import java.security.SecureRandom;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;

// Value Object: normalized email address.
final class EmailAddress {
    private final String value;

    private EmailAddress(String value) {
        this.value = value;
    }

    public static EmailAddress of(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Email is required");
        }

        String normalized = input.trim().toLowerCase(Locale.ROOT);
        String pattern = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$";

        if (!normalized.matches(pattern)) {
            throw new IllegalArgumentException("Invalid email address");
        }
        return new EmailAddress(normalized);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof EmailAddress
            && value.equals(((EmailAddress) other).value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

// Value Object: stores a phone number in a consistent international-style form.
final class PhoneNumber {
    private final String value;

    private PhoneNumber(String value) {
        this.value = value;
    }

    public static PhoneNumber of(String input) {
        String digits = input == null ? "" : input.replaceAll("[^0-9]", "");
        if (digits.length() < 10 || digits.length() > 15) {
            throw new IllegalArgumentException(
                "Phone number must contain 10 to 15 digits"
            );
        }
        return new PhoneNumber("+" + digits);
    }

    public String value() {
        return value;
    }
}

// Value Object: all required address fields must be present.
final class PostalAddress {
    private final String street;
    private final String city;
    private final String postalCode;
    private final String country;

    public PostalAddress(String street, String city,
                         String postalCode, String country) {
        if (blank(street) || blank(city)
                || blank(postalCode) || blank(country)) {
            throw new IllegalArgumentException(
                "All postal address fields are required"
            );
        }

        this.street = street.trim();
        this.city = city.trim();
        this.postalCode = postalCode.trim();
        this.country = country.trim();
    }

    private boolean blank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public String summary() {
        return street + ", " + city + ", " + postalCode + ", " + country;
    }
}

interface EmailVerification {
    boolean isVerified();
}

// A pending email has a one-time token and an expiry time.
final class PendingVerification implements EmailVerification {
    private final String token;
    private final Instant expiresAt;
    private boolean used;

    public PendingVerification(String token, Instant expiresAt) {
        this.token = Objects.requireNonNull(token);
        this.expiresAt = Objects.requireNonNull(expiresAt);
        this.used = false;
    }

    public boolean verify(String suppliedToken, Instant now) {
        if (used || suppliedToken == null
                || !token.equals(suppliedToken)
                || !now.isBefore(expiresAt)) {
            return false;
        }
        used = true;
        return true;
    }

    @Override
    public boolean isVerified() {
        return false;
    }
}

final class Verified implements EmailVerification {
    private final Instant verifiedAt;

    public Verified(Instant verifiedAt) {
        this.verifiedAt = Objects.requireNonNull(verifiedAt);
    }

    public Instant getVerifiedAt() {
        return verifiedAt;
    }

    @Override
    public boolean isVerified() {
        return true;
    }
}

enum UserStatus {
    ACTIVE, SUSPENDED, DELETED
}

final class UserAccount {
    private final String userId;
    private EmailAddress email;
    private EmailVerification verification;
    private UserStatus status;
    private PhoneNumber phone;
    private PostalAddress address;

    public UserAccount(String userId, EmailAddress email,
                       EmailVerification verification) {
        this.userId = Objects.requireNonNull(userId);
        this.email = Objects.requireNonNull(email);
        this.verification = Objects.requireNonNull(verification);
        this.status = UserStatus.ACTIVE;
    }

    public void addContactDetails(PhoneNumber phone, PostalAddress address) {
        this.phone = phone;
        this.address = address;
    }

    public boolean verifyEmail(String suppliedToken, Instant now) {
        if (!(verification instanceof PendingVerification)) {
            return false;
        }

        PendingVerification pending = (PendingVerification) verification;
        if (!pending.verify(suppliedToken, now)) {
            return false;
        }

        verification = new Verified(now);
        return true;
    }

    public void changeEmail(EmailAddress newEmail,
                            PendingVerification newVerification) {
        if (status == UserStatus.DELETED) {
            throw new IllegalStateException("Deleted account cannot be changed");
        }
        email = Objects.requireNonNull(newEmail);
        verification = Objects.requireNonNull(newVerification);
    }

    public void suspend() {
        if (status == UserStatus.DELETED) {
            throw new IllegalStateException("Deleted account cannot be changed");
        }
        status = UserStatus.SUSPENDED;
    }

    public void delete() {
        status = UserStatus.DELETED;
    }

    public boolean canLogin() {
        return status == UserStatus.ACTIVE && verification.isVerified();
    }

    public String getEmail() {
        return email.value();
    }

    public String getStatus() {
        return status.name();
    }

    public String getPhone() {
        return phone == null ? "Not provided" : phone.value();
    }

    public String getAddress() {
        return address == null ? "Not provided" : address.summary();
    }
}

public class UserAccountDemo {
    private static String generateToken() {
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    public static void main(String[] args) {
        Instant now = Instant.now();
        String token = generateToken();

        EmailAddress email = EmailAddress.of("  Laxman.Reddy@Example.com ");
        PendingVerification pending =
            new PendingVerification(token, now.plusSeconds(600));

        UserAccount user = new UserAccount("U1001", email, pending);
        user.addContactDetails(
            PhoneNumber.of("+91 98765 43210"),
            new PostalAddress("Main Road", "Hyderabad", "500090", "India")
        );

        System.out.println("Normalized email: " + user.getEmail());
        System.out.println("Phone: " + user.getPhone());
        System.out.println("Address: " + user.getAddress());
        System.out.println("Can login before verification: " + user.canLogin());

        boolean verified = user.verifyEmail(token, now);
        System.out.println("Email verification successful: " + verified);
        System.out.println("Can login after verification: " + user.canLogin());
        System.out.println("Reused token accepted: " + pending.verify(token, now));

        PendingVerification expired =
            new PendingVerification("expired-demo-token", now.minusSeconds(1));
        System.out.println("Expired token accepted: "
            + expired.verify("expired-demo-token", now));

        user.suspend();
        System.out.println("Status after suspension: " + user.getStatus());
        System.out.println("Can login after suspension: " + user.canLogin());

        EmailAddress sameEmail = EmailAddress.of("laxman.reddy@example.com");
        System.out.println("Emails equal after normalization: " + email.equals(sameEmail));
    }
}
