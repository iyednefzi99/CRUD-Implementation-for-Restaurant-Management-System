package entites.accounts_package;

public class Account {
    private String username;
    private String password;
    private String email;
    private AccountStatus status;

    public Account(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = AccountStatus.ACTIVE;
    }

    // Getters and settersç
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }

    public boolean resetPassword(String oldPassword, String newPassword) {
        if (this.status != AccountStatus.ACTIVE) return false;
        if (!this.password.equals(oldPassword)) return false;
        if (newPassword == null || newPassword.trim().isEmpty()) return false;
        setPassword(newPassword);
        return true;
    }

    public boolean forceResetPassword(String newPassword) {
        if (this.status != AccountStatus.ACTIVE) return false;
        if (newPassword == null || newPassword.trim().isEmpty()) return false;
        setPassword(newPassword);
        return true;
    }
}