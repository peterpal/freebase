package sk.fiit.stuba.ir.badal.model;

import sk.fiit.stuba.ir.badal.iterator.PersonIterator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Matej Bádal <matejbadal@gmail.com>
 */
public class PersonCollection implements Iterable<Person> {
    private Map<String, Person> map = null;

    public PersonCollection() {
        this.map = new HashMap<String, Person>();
    }

    public PersonCollection addPerson(Person p) {
        this.map.put(p.getId(), p);
        return this;
    }

    public Set<String> getIds() {
        return this.map.keySet();
    }

    public Person getPerson(String id) {
        return this.map.get(id);
    }

    public int getSize() {
        return this.map.size();
    }

    public PersonCollection setPerson(Person person) {
        this.map.put(person.getId(), person);
        return this;
    }

    @Override
    public Iterator<Person> iterator() {
        return new PersonIterator(this);
    }

}
