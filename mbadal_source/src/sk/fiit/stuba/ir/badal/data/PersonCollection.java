package sk.fiit.stuba.ir.badal.data;

/**
 * Created by delve on 11/10/14.
 */
public class PersonCollection {
    private Person[] collection = null;

    public PersonCollection(Person p1, Person p2) {
        this.collection = new Person[]{
                p1,
                p2
        };
    }


    public Person getFirst() {
        return this.collection[0];
    }

    public Person getSecond() {
        return this.collection[1];
    }

    public Person[] getAll() {
        return this.collection;
    }

}
