package ch.heig.dai.lab.smtp;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class EmailPrankApp {
    public static void main(String[] args) {
        /*
        if (args.length != 1) {
            System.err.println("Usage: java EmailPrankApp <number_of_groups>");
            return;
        }

        int numberOfGroups = Integer.parseInt(args[0]);
         */

        try {
            ConfigLoader configLoader = new ConfigLoader(
                    "./config/emails.txt",
                    "./config/messages.txt",
                    StandardCharsets.UTF_8
            );

            List<Message> messages = configLoader.loadMessages();

            List<String> emails = configLoader.loadEmailAddresses();

            GroupGenerator groupGenerator = new GroupGenerator(emails, 3);
            List<Group> groups = groupGenerator.generateGroups();

            for (Group group : groups) {
                System.out.println(group);
            }

            for (Message message : messages) {
                System.out.println(message);
                System.out.println();
            }

            for (Group group : groups) {
                Email email = new Email(group.getSender(), group.getReceivers(),
                        messages.get(0));

                System.out.println(email);
                System.out.println();
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}