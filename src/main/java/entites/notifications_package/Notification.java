package entites.notifications_package;


import java.util.Date;
import java.util.UUID;

public abstract class Notification {
    private final String notificationID;
    private final Date createDate;
    private String content;
    private boolean isSent;

    public Notification(String content) {

        this.notificationID = "NOTIF-" + UUID.randomUUID().toString().substring(0, 8);
        this.createDate = new Date();
        this.content = content.trim();
        this.isSent = false;
    }

    public abstract boolean send();

    // Getters
    public String getNotificationID() { return notificationID; }
    public Date getCreateDate() { return createDate; }
    public String getContent() { return content; }
    public boolean isSent() { return isSent; }

    protected void markAsSent() {
        this.isSent = true;
    }

    public void updateContent(String newContent) {
       
        if (!isSent) {
            this.content = newContent.trim();
        }
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s", 
            notificationID, 
            createDate, 
            content.length() > 50 ? content.substring(0, 50) + "..." : content);
    }
}