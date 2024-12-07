package ch.heig.dai.lab.smtp;

import java.util.List;

public class Group {
    private final String sender;
    private final List<String> receivers;
    private final int id;

    private static int counter = 0;

    public Group(String sender, List<String> receivers) {
        this.sender = sender;
        this.receivers = receivers;
        this.id = counter++;
    }

    public String getSender() {
        return sender;
    }

    public List<String> getReceivers() {
        return receivers;
    }

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
