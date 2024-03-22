package ca.event.solosphere.core.model;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private String isSeen;
    private String time;
    private String messageID;

    public Chat() {

    }

    public Chat(String sender, String receiver, String message, String isSeen, String time, String messageID) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isSeen = isSeen;
        this.time = time;
        this.messageID = messageID;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(String isSeen) {
        this.isSeen = isSeen;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", message='" + message + '\'' +
                ", isSeen=" + isSeen +
                ", time='" + time + '\'' +
                ", messageID='" + messageID + '\'' +
                '}';
    }
}
