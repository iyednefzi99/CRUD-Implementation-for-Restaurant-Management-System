package entites.payments_package;


public class CreditCardPayment extends Payment {
    private final String nameOnCard;
    private final String cardNumber;
    private final String expiration;
    private final int cvv;

    public CreditCardPayment(double amount, String nameOnCard, 
                           String cardNumber, String expiration, int cvv) {
        super(amount);
        this.nameOnCard = nameOnCard;
        this.cardNumber = cardNumber;
        this.expiration = expiration;
        this.cvv = cvv;
    }

    @Override
    public boolean processPayment() {
        System.out.println("Processing credit card payment...");
        this.isCompleted = true;
        return true;
    }

    // Getters
    public String getNameOnCard() { return nameOnCard; }
    public String getCardNumber() { return cardNumber; }
    public String getExpiration() { return expiration; }
    public int getCvv() { return cvv; }
}