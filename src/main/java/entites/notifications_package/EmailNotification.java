package entites.notifications_package;


public class EmailNotification extends Notification {
    private final String emailAddress;
    private String subject;

    public EmailNotification(String subject, String content, String emailAddress) {
        super(content);
       
        this.emailAddress = emailAddress;
        this.subject = subject != null ? subject : "Notification";
    }

    @Override
    public boolean send() {
        System.out.println("Sending email to " + emailAddress);
        System.out.println("Subject: " + subject);
        System.out.println("Content: " + getContent());
        markAsSent();
        return true;
    }

    public String getEmailAddress() { return emailAddress; }
    public String getSubject() { return subject; }

    public void setSubject(String subject) {
        this.subject = subject != null ? subject : "";
    }
}
