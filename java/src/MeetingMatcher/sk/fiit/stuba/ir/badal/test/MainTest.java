package sk.fiit.stuba.ir.badal.test;

import junit.framework.Assert;
import org.junit.Test;
import sk.fiit.stuba.ir.badal.init.InitService;
import sk.fiit.stuba.ir.badal.matcher.MeetingMatcher;
import sk.fiit.stuba.ir.badal.matcher.Result;
import sk.fiit.stuba.ir.badal.model.Person;
import sk.fiit.stuba.ir.badal.model.PersonCollection;

import java.io.IOException;

/**
 * @author Matej Bádal <matejbadal@gmail.com>
 */
public class MainTest {

	@Test
	public void parsingById() {
		try {
			InitService init = new InitService();
			init.init(true);
			PersonCollection collection = init.getCollection();
			String miroslavSatan = "04nfrq";
			Assert.assertEquals(true, collection.getPerson(miroslavSatan).isComplete());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testMatching() {
		String miroslavSatan = "04nfrq";
		String peterBondra = "01wd0p";
		InitService init = null;
		try {
			init = new InitService();
			init.init(true);
			PersonCollection collection = init.getCollection();
			Person satan = collection.getPerson(miroslavSatan);
			Person bondra = collection.getPerson(peterBondra);
			PersonCollection compare = new PersonCollection();
			compare.addPerson(satan).addPerson(bondra);
			Result result = MeetingMatcher.couldHaveMet(compare);
			Assert.assertNotNull(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
