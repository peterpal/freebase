package sk.fiit.stuba.ir.badal.main;

import sk.fiit.stuba.ir.badal.data.Person;
import sk.fiit.stuba.ir.badal.data.PersonCollection;
import sk.fiit.stuba.ir.badal.exception.InputFileNotSetException;
import sk.fiit.stuba.ir.badal.file.InputReader;
import sk.fiit.stuba.ir.badal.input.Parser;
import sk.fiit.stuba.ir.badal.matcher.MeetingMatcher;
import sk.fiit.stuba.ir.badal.matcher.Result;

import java.io.IOException;

/**
 * Created by delve on 11/10/14.
 */
public class Controller {
    private String firstId      = null;
    private String secondId     = null;
    private Person firstPerson  = null;
    private Person secondPerson = null;
    private MeetingMatcher matcher = null;

    public Controller(String firtsId, String secondId) {
        this.firstId = firtsId;
        this.secondId = secondId;
        this.firstPerson = new Person("First-Person");
        this.secondPerson = new Person("Second-Person");
    }

    public Result setUpModels() {
        this.parseDateOfBirths();
        this.parseDateOfDeath();
        /*System.out.println(this.firstPerson);
        System.out.println(this.secondPerson);*/
        PersonCollection collection = new PersonCollection(this.firstPerson, this.secondPerson);
        matcher = new MeetingMatcher(collection);

        //System.out.println(matcher.couldHaveMet());
        return matcher.couldHaveMet();

    }

    private void parseDateOfBirths() {
        InputReader inReader = new InputReader("sample_input_freebase_date-of-births.txt");
        String[] output = this.parseDate(inReader);
        this.firstPerson.setDateOfBirth(output[0]);
        this.secondPerson.setDateOfBirth(output[1]);
    }

    private void parseDateOfDeath() {
        InputReader inReader = new InputReader("sample_input_freebase_date-of-deaths.txt");
        String[] output = this.parseDate(inReader);
        this.firstPerson.setDateOfDeath(output[0]);
        this.secondPerson.setDateOfDeath(output[1]);
    }

    private String[] parseDate(InputReader inReader) {
        String line = null;
        String[] output = {
                null,
                null
        };
        try {
            while ((line = inReader.readLine()) != null) {
                if (!Parser.containsId(line, this.firstId) && !Parser.containsId(line, this.secondId)) {
                    continue;
                }


                if (Parser.containsId(line, this.firstId)) {
                    output[0] = Parser.extractDate(line);
                } else if (Parser.containsId(line, this.secondId)) {
                    output[1] = Parser.extractDate(line);
                }
            }
        } catch (InputFileNotSetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }


}
