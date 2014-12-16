package sk.fiit.stuba.ir.badal.input;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Matej Bádal <matejbadal@gmail.com>
 */
public class Parser {

    private static String PATTER_ID             = "^<http://rdf\\.freebase\\.com/ns/m\\.([a-zA-Z0-9\\-\\_]+)>";
    private static String PATTERN_NAME          = "type.object.name>\\s+\"(.+)\"@en";
    private static String PATTERN_DATE_OF_BIRTH = "date_of_birth>\\s+\"([0-9\\-]+)\"";
    private static String PATTERN_DATE_OF_DEATH = "date_of_death>\\s+\"([0-9\\-]+)\"";

    public static String parseId(String line) {
        Pattern pattern = Pattern.compile(Parser.PATTER_ID);
        Matcher matcher = pattern.matcher(line);
        if (!matcher.find()) {
            return null;
        }

        return matcher.group(1);
    }


    public static String parseName(String line) {
        Pattern pattern = Pattern.compile(Parser.PATTERN_NAME);
        Matcher matcher = pattern.matcher(line);
        if (!matcher.find()) {
            return null;
        }

        return matcher.group(1);
    }

    public static String parseDateOfBirth(String line) {
        Pattern pattern = Pattern.compile(Parser.PATTERN_DATE_OF_BIRTH);
        Matcher matcher = pattern.matcher(line);
        if (!matcher.find()) {
            return null;
        }

        return matcher.group(1);
    }

    public static String parseDateOfDeath(String line) {
        Pattern pattern = Pattern.compile(Parser.PATTERN_DATE_OF_DEATH);
        Matcher matcher = pattern.matcher(line);
        if (!matcher.find()) {
            return null;
        }

        return matcher.group(1);
    }

}
