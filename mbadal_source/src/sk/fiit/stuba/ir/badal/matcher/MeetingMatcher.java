package sk.fiit.stuba.ir.badal.matcher;

import sk.fiit.stuba.ir.badal.data.Person;
import sk.fiit.stuba.ir.badal.data.PersonCollection;

/**
 * Created by delve on 11/10/14.
 */
public class MeetingMatcher {
    private PersonCollection collection = null;

    public MeetingMatcher(PersonCollection collection) {
        this.collection = collection;
    }

    public Result couldHaveMet() {
        Person p1 = collection.getFirst();
        Person p2 = collection.getSecond();
        Result result = new Result();
        result.setMessage("No");
        result.setCode(Result.CODE_NOT_MET);
        if (MeetingMatcher.couldHaveMetForSure(p1, p2)) {
            result.setMessage("Yes");
            result.setCode(Result.CODE_MET);
        }

        return result;
    }

    private static boolean couldHaveMetForSure(Person p1, Person p2) {
        if (p1.getDateOfBirthAsTime() > p2.getDateOfDeathAsTime()) {
            return false;
        }

        if (p1.getDateOfDeathAsTime() < p2.getDateOfBirthAsTime()) {
            return false;
        }
        return true;
    }
}
