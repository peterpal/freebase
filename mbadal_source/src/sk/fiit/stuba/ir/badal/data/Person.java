package sk.fiit.stuba.ir.badal.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by delve on 11/10/14.
 */
public class Person {

    private static String FORMAT_DATE_FULL = "yyyy-MM-dd";

    private String name;
    private String dateOfBirth;
    private String dateOfDeath;

    public Person(String name, String dateOfBirth, String dateOfDeath) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        //temporary fix
        if (this.dateOfBirth.length() == 4) {
            this.dateOfBirth = this.dateOfBirth.concat("-01-01");
        }
        this.dateOfDeath = dateOfDeath;
        if (this.dateOfDeath != null && this.dateOfDeath.length() == 4) {
            this.dateOfDeath = this.dateOfDeath.concat("-01-01");
        }
    }

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {

        return this.dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        if (this.dateOfBirth.length() == 4) {
            this.dateOfBirth = this.dateOfBirth.concat("-01-01");
        }
    }

    public String getDateOfDeath() {
        return this.dateOfDeath;
    }

    public void setDateOfDeath(String dateOfDeath) {

        this.dateOfDeath = dateOfDeath;
        if (this.dateOfDeath != null && this.dateOfDeath.length() == 4) {
            this.dateOfDeath = this.dateOfDeath.concat("-01-01");
        }
    }

    public long getDateOfBirthAsTime() {
        DateFormat formatter = new SimpleDateFormat(Person.FORMAT_DATE_FULL);
        Date date = null;
        try {
            date = formatter.parse(this.dateOfBirth);
        } catch (ParseException e) {
            System.out.println("Parse exception");
            System.exit(1);
        }
        return date.getTime();
    }

    public long getDateOfDeathAsTime() {
        if (this.dateOfDeath == null) {
            return System.currentTimeMillis();
        }
        DateFormat formatter = new SimpleDateFormat(Person.FORMAT_DATE_FULL);
        Date date = null;
        try {
            date = formatter.parse(this.dateOfDeath);
        } catch (ParseException e) {
            System.out.println("Parse exception");
            System.exit(1);
        }
        return date.getTime();
    }

    public String toString() {
        return "[" + this.name + ", " + this.dateOfBirth + ", " + this.dateOfDeath + "]";
    }
}
