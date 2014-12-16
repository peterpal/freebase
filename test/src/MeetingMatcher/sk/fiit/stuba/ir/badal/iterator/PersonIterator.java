package sk.fiit.stuba.ir.badal.iterator;

import sk.fiit.stuba.ir.badal.model.Person;
import sk.fiit.stuba.ir.badal.model.PersonCollection;

import java.util.Iterator;

/**
 * @author Matej Bádal <matejbadal@gmail.com>
 */
public class PersonIterator implements Iterator<Person> {
	private PersonCollection collection = null;
	private String           currentId  = null;
	private Iterator         idIterator = null;

	public PersonIterator(PersonCollection collection) {
		this.collection = collection;
		this.idIterator = this.collection.getIds().iterator();

	}

	@Override
	public boolean hasNext() {
		return this.idIterator.hasNext();
	}

	@Override
	public Person next() {
		this.currentId = (String)this.idIterator.next();
		return this.collection.getPerson(this.currentId);
	}

	@Override
	public void remove() {
		return;
	}
}
