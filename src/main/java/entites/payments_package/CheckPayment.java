package entites.payments_package;


public class CheckPayment extends Payment {
    private final String bankName;
    private final String checkNumber;

    public CheckPayment(double amount, String bankName, String checkNumber) {
        super(amount);
        this.bankName = bankName;
        this.checkNumber = checkNumber;
    }

    @Override
    public boolean processPayment() {
        System.out.println("Processing check payment...");
        this.isCompleted = true;
        return true;
    }

    // Getters
    public String getBankName() { return bankName; }
    public String getCheckNumber() { return checkNumber; }
}
