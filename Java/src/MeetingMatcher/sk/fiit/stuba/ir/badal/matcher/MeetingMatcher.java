package sk.fiit.stuba.ir.badal.matcher;


import sk.fiit.stuba.ir.badal.model.Person;
import sk.fiit.stuba.ir.badal.model.PersonCollection;

/**
 * Created by delve on 11/10/14.
 */
public class MeetingMatcher {


    /**
     * Checks if two persons could have met
     *
     * @param collection
     * @return Result
     */
    public static Result couldHaveMet(PersonCollection collection) {
        Result result = new Result();
        result.setCode(Result.CODE_NOT_FOUND);
        result.setMessage(Result.MESSAGE_NOT_FOUND);
        if (collection.getSize() != 2) {
            return result;
        }
        Object[] keys = collection.getIds().toArray();
        Person p1 = collection.getPerson((String)keys[0]);
        Person p2 = collection.getPerson((String)keys[1]);
        result.setFirst(p1).setSecond(p2);
        result.setCode(Result.CODE_NOT_MET);
        result.setMessage(Result.MESSAGE_NO);
        if (MeetingMatcher.couldHaveMetForSure(p1, p2)) {
            result.setMessage(Result.MESSAGE_YES);
            result.setCode(Result.CODE_MET);
        }

        return result;
    }

    private static boolean couldHaveMetForSure(Person p1, Person p2) {
        if (p1.getDateOfBirth().after(p2.getDateOfDeath())) {
            return false;
        }

        if (p1.getDateOfDeath().before(p2.getDateOfBirth())) {
            return false;
        }
        return true;
    }
}
