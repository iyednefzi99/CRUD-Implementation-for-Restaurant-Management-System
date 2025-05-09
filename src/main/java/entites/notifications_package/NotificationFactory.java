package entites.notifications_package;


public class NotificationFactory {
    public static Notification createSMS(String content, String phoneNumber) {
        return new SMSNotification(content, phoneNumber);
    }

    public static Notification createEmail(String subject, String content, String email) {
        return new EmailNotification(subject, content, email);
    }
}