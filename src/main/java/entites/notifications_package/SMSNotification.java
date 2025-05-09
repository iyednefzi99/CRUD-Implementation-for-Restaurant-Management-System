package entites.notifications_package;

public class SMSNotification extends Notification {
    private final String phoneNumber;

    public SMSNotification(String content, String phoneNumber) {
        super(content);
       
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean send() {
        System.out.println("Sending SMS to " + phoneNumber + ": " + getContent());
        markAsSent();
        return true;
    }

    public String getPhoneNumber() { return phoneNumber; }
}