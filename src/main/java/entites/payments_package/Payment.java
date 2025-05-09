package entites.payments_package;

import java.util.Date;

public abstract class Payment {
    protected final String paymentID;
    protected final double amount;
    protected final Date creationDate;
    protected boolean isCompleted;

    public Payment(double amount) {
        this.paymentID = "PAY-" + System.currentTimeMillis();
        this.amount = amount;
        this.creationDate = new Date();
        this.isCompleted = false;
    }

    public abstract boolean processPayment();

    // Getters
    public String getPaymentID() { return paymentID; }
    public double getAmount() { return amount; }
    public Date getCreationDate() { return creationDate; }
    public boolean isCompleted() { return isCompleted; }
}
