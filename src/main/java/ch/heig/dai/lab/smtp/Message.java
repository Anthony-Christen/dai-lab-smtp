/**
 * @Authors Bouzourène Ryad & Christen Anthony
 * @Date    2024-12-07
 */

package ch.heig.dai.lab.smtp;

/**
 * Represents a message containing a subject and a body. Provides methods
 * to access the subject and body of the message, as well as a formatted string
 * representation of the message.
 */
public class Message {
    // ------------------------------------------------------------------------------
    // Attributes
    // ------------------------------------------------------------------------------
    private final String subject;
    private final String body;

    // ------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------
    /**
     * Constructs a Message object with the specified subject and body.
     *
     * @param subject the subject of the email message.
     * @param body    the body of the email message.
     */
    Message(String subject, String body) {
        this.subject = subject;
        this.body = body;
    }

    // ------------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------------
    /**
     * Gets the subject of the message.
     *
     * @return the subject of the message.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Gets the body of the message.
     *
     * @return the body of the message.
     */
    public String getBody() {
        return body;
    }

    /**
     * Returns a string representation of the message, including the subject
     * and body formatted for display.
     *
     * @return a formatted string representation of the message.
     */
    @Override
    public String toString() {
       return "[SUBJECT] " + subject + "\n" +
              "[BODY] " + body;
    }
}
