package ch.heig.dai.lab.smtp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GroupGenerator {
    private final List<String> emails;
    private final int numberOfGroups;

    public GroupGenerator(List<String> emails, int numberOfGroups) throws IllegalArgumentException {
        if (numberOfGroups < 1) {
            throw new IllegalArgumentException("Number of groups must be at least 1.");
        }

        if (emails.size() < numberOfGroups * 2) {
            throw new IllegalArgumentException("Not enough emails to form groups of at least 2.");
        }

        if (emails.size() > numberOfGroups * 5) {
            throw new IllegalArgumentException("Too many emails to form valid groups of at most 5.");
        }

        this.emails = emails;
        this.numberOfGroups = numberOfGroups;
    }

    public List<Group> generateGroups() {
        Collections.shuffle(emails);

        List<Group> groups = new ArrayList<>();

        int baseGroupSize = emails.size() / numberOfGroups;
        int extraEmails = emails.size() % numberOfGroups;

        int emailIndex = 0;

        for (int i = 0; i < numberOfGroups; i++) {
            int currentGroupSize = baseGroupSize + (i < extraEmails ? 1 : 0);
            List<String> groupEmails = new ArrayList<>();

            for (int j = 0; j < currentGroupSize; j++) {
                groupEmails.add(emails.get(emailIndex++));
            }

            Group group = new Group(groupEmails.get(0), groupEmails.subList(1, groupEmails.size()));
            groups.add(group);
        }

        return groups;
    }
}
