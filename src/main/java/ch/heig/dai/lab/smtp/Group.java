/**
 * @Authors Bouzourène Ryad & Christen Anthony
 * @Date    2024-12-07
 */

package ch.heig.dai.lab.smtp;

import java.util.List;

/**
 * Represents a group of emails with a sender, a list of receivers, and a unique
 * group identifier. The class provides methods to access the
 * sender, receivers, and a formatted string representation of the group.
 */
public class Group {
    // ------------------------------------------------------------------------------
    // Attributes
    // ------------------------------------------------------------------------------
    private final String sender;
    private final List<String> receivers;
    private final int id;

    private static int counter = 0;

    // ------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------
    /**
     * Constructs a Group object with the specified sender and receivers.
     * Assigns a unique identifier to the group.
     *
     * @param sender    the email address of the sender for this group.
     * @param receivers a list of email addresses representing the receivers of the group.
     */
    public Group(String sender, List<String> receivers) {
        this.sender = sender;
        this.receivers = receivers;
        this.id = counter++;
    }

    // ------------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------------
    /**
     * Gets the email address of the sender in this group.
     *
     * @return the sender's email address.
     */
    public String getSender() {
        return sender;
    }

    /**
     * Gets the list of email addresses of the receivers in this group.
     *
     * @return a list of receivers' email addresses.
     */
    public List<String> getReceivers() {
        return receivers;
    }

    /**
     * Returns a string representation of the group, including the group ID,
     * sender, and receivers.
     *
     * @return a formatted string representing the group details.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Group: ").append(id).append("\n");
        builder.append("\tSender: ").append(sender).append("\n");
        builder.append("\tReceivers:\n");

        for (String receiver : receivers) {
            builder.append("\t\t").append(receiver).append("\n");
        }

        return builder.toString().stripTrailing();
    }
}
