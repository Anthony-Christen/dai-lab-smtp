/**
 * @Authors Bouzourène Ryad & Christen Anthony
 * @Date    2024-12-07
 */

package ch.heig.dai.lab.smtp;

import java.util.List;

/**
 * Represents an email with a sender, a list of receivers, and a message.
 * Provides methods to access the sender, receivers, subject, body, and
 * to validate email addresses.
 */
public class Email {
    // ------------------------------------------------------------------------------
    // Attributes
    // ------------------------------------------------------------------------------
    private final String sender;
    private final List<String> receivers;
    private final Message message;

    private final static String REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\" +
            ".[a-zA-Z]{2,}$";

    // ------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------
    /**
     * Constructs an Email object with the specified sender, receivers, and message.
     *
     * @param sender    the email address of the sender.
     * @param receivers a list of email addresses.
     * @param message   the Message object containing the subject and body.
     */
    public Email(String sender, List<String> receivers, Message message) {
        this.sender = sender;
        this.receivers = receivers;
        this.message = message;
    }

    // ------------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------------
    /**
     * Gets the email address of the sender.
     *
     * @return the sender's email address.
     */
    public String getSender() {
        return sender;
    }

    /**
     * Gets the list of email addresses of the receivers.
     *
     * @return a list of receivers email addresses.
     */
    public List<String> getReceivers() { return receivers; }

    /**
     * Gets the subject of the email.
     *
     * @return the subject of the email.
     */
    public String getSubject() {
        return message.getSubject();
    }

    /**
     * Gets the body of the email.
     *
     * @return the body of the email.
     */
    public String getBody() {
        return message.getBody();
    }

    /**
     * Returns a string representation of the email, including the sender,
     * receivers, subject, and body.
     *
     * @return a formatted string representation of the email.
     */
    @Override
    public String toString() {
        return "[FROM] " + sender + "\n" +
               "[TO] " + String.join(", ", receivers) + "\n" +
               message;
    }

    /**
     * Validates if the given email address follows a valid format.
     *
     * @param email the email address to validate.
     * @return true if the email address is valid, false otherwise.
     */
    public static boolean isValid(String email) {
        return email != null && email.matches(REGEX);
    }
}
