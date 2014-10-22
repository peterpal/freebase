package sk.fiit.stuba.ir.badal.input;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by delve on 11/10/14.
 */
public class Parser {

    private static String PATTER_ID = "^<http://rdf\\.freebase\\.com\\/ns\\/m\\.";
    private static String PATTER_DATE = "\"([0-9-]+)";

    public static String extractDate(String line) {
        String date = Parser.parseDate(line);

        return date;
    }

    public static boolean containsId(String line, String id) {
        Pattern pattern = Pattern.compile(Parser.PATTER_ID + id + ">");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            if (matcher.start() == 0) {
                return true;
            }
        }

        return false;
    }

    private static String parseDate(String line) {
        Pattern pattern = Pattern.compile(Parser.PATTER_DATE);
        Matcher matcher = pattern.matcher(line);
        if (!matcher.find()) {
            return null;
        }

        return matcher.group(1);
    }



}
