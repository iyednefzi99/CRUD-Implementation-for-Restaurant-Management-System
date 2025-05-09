package entites.payments_package;

import java.util.Date;
import java.util.UUID;

public class Invoice {
    private final String invoiceID;
    private final Date creationDate;
    private double amount;
    private double tip;
    private double tax;
    private boolean isPaid;
    private Payment payment;

    public Invoice(double amount, double taxRate) {
        this.invoiceID = "INV-" + UUID.randomUUID().toString().substring(0, 8);
        this.creationDate = new Date();
        this.amount = amount;
        this.tax = amount * taxRate;
        this.tip = 0;
        this.isPaid = false;
    }

    public void applyTip(double tipAmount) {
        if (tipAmount < 0) throw new IllegalArgumentException("Tip cannot be negative");
        this.tip = tipAmount;
    }

    public void applyPercentageTip(double percentage) {
        if (percentage < 0) throw new IllegalArgumentException("Tip percentage cannot be negative");
        this.tip = amount * (percentage / 100);
    }

    public double getTotalAmount() {
        return amount + tax + tip;
    }

    public void markAsPaid(Payment payment) {
        if (payment == null) throw new IllegalArgumentException("Payment cannot be null");
        this.payment = payment;
        this.isPaid = true;
    }

    // Getters
    public String getInvoiceID() { return invoiceID; }
    public double getAmount() { return amount; }
    public double getTip() { return tip; }
    public double getTax() { return tax; }
    public boolean isPaid() { return isPaid; }
    public Payment getPayment() { return payment; }
    public Date getCreationDate() { return creationDate; }

    public void printCheck() {
        System.out.println("=== Restaurant Check ===");
        System.out.printf("Subtotal: $%.2f\n", amount);
        System.out.printf("Tax: $%.2f\n", tax);
        System.out.printf("Tip: $%.2f\n", tip);
        System.out.printf("Total: $%.2f\n", getTotalAmount());
        System.out.println("========================");
    }
}