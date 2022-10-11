package duke.main;

import duke.exception.DukeException;

import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class
import java.time.format.DateTimeParseException;

/**
 * Utility Class managing LocalDateTime inputs and outputs
 */
public class DateTime {
    private static DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("MMM d yyyy HHmm");

    /**
     * Parses the date given into a LocalDateTime.
     * @param date String input by user.
     * @return LocalDateTime parsed from the input.
     * @throws DukeException If the string input by the user is not in the valid format.
     */
    public static LocalDateTime parseDate(String date) throws DukeException {
        try {
            LocalDateTime parsedDate = LocalDateTime.parse(date, inputFormat);
            return parsedDate;
        } catch (DateTimeParseException e) {
            throw new DukeException("Hmm... your date and time has to be in YYYY-MM-DD HHmm format!");
        }
    }

    /**
     * Prints String of given LocalDateTime.
     * @param date Input LocalDateTime.
     * @return String from the input LocalDateTime in its output format.
     */
    public static String printDate(LocalDateTime date) {
        return date.format(outputFormat);
    }

    /**
     * Changes format of LocalDateTime into the input format.
     * @param date LocalDateTime to be changed back to input format.
     * @return String from the input LocalDateTime in its input format.
     */
    public static String changeFormat(LocalDateTime date) {
        return date.format(inputFormat);
    }
}