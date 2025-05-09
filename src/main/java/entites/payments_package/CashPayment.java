package entites.payments_package;


public class CashPayment extends Payment {
    private double cashTendered;

    public CashPayment(double amount, double cashTendered) {
        super(amount);
        this.cashTendered = cashTendered;
    }

    @Override
    public boolean processPayment() {
        if (cashTendered < amount) {
            System.out.println("Insufficient cash tendered");
            return false;
        }
        System.out.printf("Cash payment received. Change: $%.2f\n", (cashTendered - amount));
        this.isCompleted = true;
        return true;
    }

    public double getCashTendered() { return cashTendered; }
    public double getChange() { return cashTendered - amount; }
}