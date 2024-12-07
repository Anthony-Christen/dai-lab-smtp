package ch.heig.dai.lab.smtp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ConfigLoader {
    private final String emailAddressesFilePath;
    private final String messagesFilePath;
    private final Charset encoding;

    private static final String MESSAGE_SEPARATOR = "$---"; // Messages delimiter
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\" +
            ".[a-zA-Z]{2,}$";

    public ConfigLoader(String emailAddressesFilePath, String messagesFilePath, Charset encoding) {
        this.emailAddressesFilePath = emailAddressesFilePath;
        this.messagesFilePath = messagesFilePath;
        this.encoding = encoding;
    }

    public List<String> loadEmailAddresses() throws IOException {
        List<String> emailAddresses = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(emailAddressesFilePath, encoding))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!isEmailAddressValid(line)) {
                    throw new IllegalArgumentException("Invalid email address: " + line);
                }
                emailAddresses.add(line);
            }
        }

        return emailAddresses;
    }

    public List<Message> loadMessages() throws IOException {
        List<Message> messages = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(messagesFilePath, encoding))) {
            String line;
            String subject = null;
            StringBuilder body = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith(MESSAGE_SEPARATOR)) {
                    if (subject != null) {
                        messages.add(new Message(subject, body.toString().stripTrailing()));
                    }
                    subject = null;
                    body = new StringBuilder();
                    continue;
                }

                if (subject == null) {
                    subject = line;
                } else {
                    body.append(line).append("\n");
                }
            }

            messages.add(new Message(subject, body.toString().stripTrailing()));
        }

        return messages;
    }

    private static boolean isEmailAddressValid(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }
}
