/**
 * @Authors Bouzourène Ryad & Christen Anthony
 * @Date    2024-12-07
 */

package ch.heig.dai.lab.smtp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * The ConfigLoader class is responsible for loading and parsing configuration
 * values from files. It provides methods to retrieve victims email addresses,
 * messages, SMTP server settings, and other configuration parameters.
 */
public class ConfigLoader {
    // ------------------------------------------------------------------------------
    // Attributes
    // ------------------------------------------------------------------------------
    private final Map<String, String> configValues;

    private static final String CONFIG_FOLDER = "config";
    private static final String CONFIG_FILE = "config.txt";
    private static final String CONFIG_FILE_PATH = CONFIG_FOLDER + "/" + CONFIG_FILE;
    private enum CONFIG_KEYS  {
        messagesFileName,
        victimsFileName,
        messagesSeparator,
        messagesEncoding,
        nbGroups,
        smtpServerAddress,
        smtpServerPort
    }

    // ------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------
    /**
     * Default constructor. Initializes the configuration values by reading the
     * configuration file.
     *
     * @throws IOException if an error occurs while reading the configuration file.
     * @throws IllegalArgumentException if a required configuration key is missing or
     *                                  invalid.
     */
    public ConfigLoader() throws IOException, IllegalArgumentException {
        this.configValues = loadConfig();
        validateConfig();
    }

    // ------------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------------
    /**
     * Loads configuration values from the configuration file.
     *
     * @return a Map containing key-value pairs of configuration settings.
     * @throws IOException if an error occurs while reading the configuration file.
     * @throws IllegalArgumentException if the configuration file contains invalid lines.
     */
    private static Map<String, String> loadConfig() throws IOException, IllegalArgumentException {
        Map<String, String> config = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split("=", 2);

                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    config.put(key, value);
                } else {
                    throw new IllegalArgumentException("Invalid line in " + CONFIG_FILE_PATH +" file: " + line);
                }
            }
        }

        return config;
    }

    /**
     * Validates that all required configuration keys are present and contain valid values.
     *
     * @throws IllegalArgumentException if a required configuration key is missing or invalid.
     */
    private void validateConfig() throws IllegalArgumentException {
        for (var key : CONFIG_KEYS.values()) {
            if (!configValues.containsKey(key.toString()) || configValues.get(key.toString()).isBlank()) {
                throw new IllegalArgumentException("Missing or empty configuration key: " + key);
            }
        }

        try {
            Integer.parseInt(configValues.get(CONFIG_KEYS.nbGroups.toString()));
            Integer.parseInt(configValues.get(CONFIG_KEYS.smtpServerPort.toString()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric value in configuration: " + e.getMessage());
        }

        try {
            Charset.forName(configValues.get(CONFIG_KEYS.messagesEncoding.toString()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid encoding in configuration: " + configValues.get(CONFIG_KEYS.messagesEncoding.toString()));
        }
    }

    /**
     * Retrieves the list of the victims email addresses from the victims file.
     *
     * @return a list of valid email addresses.
     * @throws IOException if an error occurs while reading the victims file.
     * @throws IllegalArgumentException if an invalid email address is found.
     */
    public List<String> getVictims() throws IOException, IllegalArgumentException {
        List<String> emailAddresses = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(getVictimsFilePath(), getEncoding()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!Email.isValid(line)) {
                    throw new IllegalArgumentException("Invalid email address: " + line);
                }
                emailAddresses.add(line);
            }
        }

        return emailAddresses;
    }

    /**
     * Retrieves the list of messages from the messages file. Messages are separated
     * by a predefined separator.
     *
     * @return a list of Message objects containing the subject and body.
     * @throws IOException if an error occurs while reading the messages file.
     */
    public List<Message> getMessages() throws IOException {
        List<Message> messages = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(getMessagesFilePath(), getEncoding()))) {
            String line;
            String subject = null;
            StringBuilder body = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith(getSeparator())) {
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

    /**
     * Gets the file path to the messages file based on the configuration values.
     *
     * @return the file path to the messages file.
     */
    private String getMessagesFilePath() {
        return CONFIG_FOLDER + "/" + configValues.get(CONFIG_KEYS.messagesFileName.toString());
    }

    /**
     * Gets the file path to the victims file based on the configuration values.
     *
     * @return the file path to the victims file.
     */
    private String getVictimsFilePath() {
        return CONFIG_FOLDER + "/" + configValues.get(CONFIG_KEYS.victimsFileName.toString());
    }

    /**
     * Retrieves the separator used to distinguish different messages in the messages file.
     *
     * @return the separator.
     */
    private String getSeparator() {
        return configValues.get(CONFIG_KEYS.messagesSeparator.toString());
    }

    /**
     * Retrieves the encoding used for the messages and victims files.
     *
     * @return the corresponding Charset.
     */
    public Charset getEncoding() {
        return Charset.forName(configValues.get(CONFIG_KEYS.messagesEncoding.toString()));
    }

    /**
     * Retrieves the number of groups specified in the configuration.
     *
     * @return the number of groups.
     * @throws NumberFormatException if the configuration value is not a valid integer.
     */
    public int getNbGroups() throws NumberFormatException {
        return Integer.parseInt(configValues.get(CONFIG_KEYS.nbGroups.toString()));
    }

    /**
     * Retrieves the SMTP server address from the configuration.
     *
     * @return the SMTP server address as a string.
     */
    public String getSmtpServerAddress() {
        return configValues.get(CONFIG_KEYS.smtpServerAddress.toString());
    }

    /**
     * Retrieves the SMTP server port from the configuration.
     *
     * @return the SMTP server port as an integer.
     * @throws NumberFormatException if the configuration value is not a valid integer.
     */
    public int getSmtpServerPort() throws NumberFormatException {
        return Integer.parseInt(configValues.get(CONFIG_KEYS.smtpServerPort.toString()));
    }
}
