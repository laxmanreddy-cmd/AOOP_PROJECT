import java.time.Instant;
import java.util.Objects;
 
enum UserStatus {
    ACTIVE, SUSPENDED, DELETED
}
 
class UserAccount {
    private final String userId;
    private EmailAddress email;
    private EmailVerification verification;
    private UserStatus status;
 
    public UserAccount(String userId, EmailAddress email,
                       EmailVerification verification) {
        this.userId = Objects.requireNonNull(userId);
        this.email = Objects.requireNonNull(email);
        this.verification = Objects.requireNonNull(verification);
        this.status = UserStatus.ACTIVE;
    }
 
    public void verifyEmail(String token, Instant now) {
        if (!(verification instanceof PendingVerification)) {
            throw new IllegalStateException(
                    "Email is not waiting for verification");
        }
 
        PendingVerification pending =
                (PendingVerification) verification;
 
        if (!pending.matches(token, now)) {
            throw new IllegalArgumentException(
                    "Invalid or expired token");
        }
 
        verification = new Verified(now);
    }
 
    public void suspend() {
        if (status == UserStatus.DELETED) {
            throw new IllegalStateException("Account is deleted");
        }
        status = UserStatus.SUSPENDED;
    }
 
    public void delete() {
        status = UserStatus.DELETED;
    }
 
    public boolean canLogin() {
        return status == UserStatus.ACTIVE
                && verification instanceof Verified;
    }
}
