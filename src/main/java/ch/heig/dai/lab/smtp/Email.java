package ch.heig.dai.lab.smtp;

import java.util.Arrays;
import java.util.List;

public class Email {
    private final String sender;
    private final List<String> receivers;
    private final Message message;

    public Email(String sender, List<String> receivers, Message message) {
        this.sender = sender;
        this.receivers = receivers;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public List<String> getRecipients() {
        return receivers;
    }

    public String getSubject() {
        return message.getSubject();
    }

    public String getBody() {
        return message.getBody();
    }

    @Override
    public String toString() {
        return "[FROM] " + sender + "\n" +
               "[TO] " + String.join(", ", receivers) + "\n" +
               message;
    }
}
