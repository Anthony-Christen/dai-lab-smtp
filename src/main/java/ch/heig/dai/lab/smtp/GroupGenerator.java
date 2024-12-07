/**
 * @Authors Bouzourène Ryad & Christen Anthony
 * @Date    2024-12-07
 */

package ch.heig.dai.lab.smtp;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates groups of participants for the prank email application. Each group
 * has a sender and one or more receivers. Ensures that the number of groups and
 * group sizes are valid.
 */
public class GroupGenerator {
    // ------------------------------------------------------------------------------
    // Attributes
    // ------------------------------------------------------------------------------
    private final List<String> emails;
    private final int numberOfGroups;

    // ------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------
    /**
     * Constructs a GroupGenerator object with a list of email addresses and a specified
     * number of groups. Validates that the number of emails is sufficient and reasonable
     * for the number of groups.
     *
     * @param emails a list of email addresses to divide into groups.
     * @param numberOfGroups the number of groups to generate.
     * @throws IllegalArgumentException if the number of groups is less than 1,
     *                                  if there are not enough emails to form groups,
     *                                  or if there are too many emails to form valid groups.
     */
    public GroupGenerator(List<String> emails, int numberOfGroups) throws IllegalArgumentException {
        if (numberOfGroups < 1) {
            throw new IllegalArgumentException("Number of groups must be at least 1.");
        }

        if (emails.size() < numberOfGroups * 2) {
            throw new IllegalArgumentException("Not enough emails to form groups " +
                    "of at least 2 emails.");
        }

        if (emails.size() > numberOfGroups * 5) {
            throw new IllegalArgumentException("Too many emails to form valid " +
                    "groups of at most 5 emails.");
        }

        this.emails = emails;
        this.numberOfGroups = numberOfGroups;
    }

    // ------------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------------
    /**
     * Generates groups of participants. Each group contains one sender and multiple
     * receivers. The groups are generated in a balanced way, ensuring that group
     * sizes are as equal as possible.
     *
     * @return a list of Group objects representing the generated groups.
     */
    public List<Group> generateGroups() {
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
