package sk.fiit.stuba.ir.badal.init;


import org.apache.commons.lang.StringEscapeUtils;
import sk.fiit.stuba.ir.badal.exception.InputFileNotSetException;
import sk.fiit.stuba.ir.badal.file.InputReader;
import sk.fiit.stuba.ir.badal.input.Parser;
import sk.fiit.stuba.ir.badal.lucene.service.LuceneService;
import sk.fiit.stuba.ir.badal.model.Person;
import sk.fiit.stuba.ir.badal.model.PersonCollection;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Matej Bádal <matejbadal@gmail.com>
 */
public class InitService {

	private static final String DATA_PATH = "./data/";

	public static final String NAME_BIRTHS      = "sample_input_freebase_date-of-births.txt";
	public static final String NAME_DEATHS      = "sample_input_freebase_date-of-deaths.txt";
	public static final String NAME_NAMES       = "sample_input_freebase_names.txt";
	public static final String NAME_TEST_NAMES  = "sample_input_freebase_names_test.txt";
	public static final String NAME_TEST_DEATHS = "sample_input_freebase_date-of-deaths_test.txt";
	public static final String NAME_TEST_BIRTHS = "sample_input_freebase_date-of-births_test.txt";

	//-----------------------------------
	public static final  String UNIFIED_FILE          = "sample_input_persons.txt";
	//----
	private static       String FORMAT_DATE_FULL      = "yyyy-MM-dd";
	private static       String FORMAT_YEAR_ONLY      = "yyyy";
	private static       String FORMAT_YEAR_AND_MONTH = "yyyy-MM";
	private static final int    MILLIS_IN_SECOND      = 1000;
	private static final int    SECONDS_IN_MINUTE     = 60;
	private static final int    MINUTES_IN_HOUR       = 60;
	private static final int    HOURS_IN_DAY          = 24;
	private static final double DAYS_IN_YEAR          = 365.25;

	private InputReader      reader          = null;
	private PersonCollection collection      = null;
	private long             skippedEntities = 0;
	private long             errors          = 0;
	private long             incomplete      = 0;

	public InitService() throws IOException {
		this.collection = new PersonCollection();
	}

	public void init(Boolean test) {
		try {
			LuceneService service = LuceneService.getInstance();
			service.clearIndexDir();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Parsing births");
		this.fetchBirths(test);
		System.out.println("Parsing names");
		this.fetchNames(test);
		System.out.println("Parsing deaths");
		this.fetchDeaths(test);
		System.out.println("Unifying inputs");
		this.unifyInputs();
		System.out.println(
				"Skipped entities: " + this.skippedEntities + "\nErrors: " + this.errors + "\nIncomplete: " + this
						.incomplete);
	}

	/**
	 * Stores persons from collection into Lucene
	 */
	private void unifyInputs() {
		try {
			LuceneService service = LuceneService.getInstance();
			service.openWriter();
			int index = 0;
			for (Person person : this.collection) {
				//System.out.println(++index);
				index++;
				if (index % 10000 == 0) {
					System.out.println(index);
				}
				if (!person.isComplete()) {
					this.incomplete++;
					continue;
				}

				service.addPerson(person);
			}
			service.closeWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Fetches birth dates from file and adds them to collection
	 *
	 * @param test
	 */
	private void fetchBirths(Boolean test) {
		try {
			if (test) {
				this.reader = new InputReader(InitService.NAME_TEST_BIRTHS);
			} else {
				this.reader = new InputReader(InitService.NAME_BIRTHS);
			}
			String line = null;
			int i = 1;
			while ((line = this.reader.readLine()) != null) {
				String id = Parser.parseId(line);
				String birth = Parser.parseDateOfBirth(line);
				//System.out.println(birth);
				if (id == null || birth == null) {
					//this.skippedEntities++;
					continue;
				}
				Date birthDate = null;
				try {
					birthDate = InitService.convertStringDateToTime(birth);
					long maxDeathTime = birthDate.getTime() + InitService.getYearTime();
					Date maxDeathDate = new Date();
					maxDeathDate.setTime(maxDeathTime);
					Person p = new Person();
					p.setId(id).setDateOfBirth(birthDate).setDateOfDeath(maxDeathDate);
					this.collection.addPerson(p);
				} catch (ParseException e) {
					//e.printStackTrace();
					this.errors++;
					continue;
				}
			}
		} catch (IOException | InputFileNotSetException e) {
			//e.printStackTrace();
			this.skippedEntities++;
		}
	}

	/**
	 * Fetches names from file and maps them with existing births
	 *
	 * @param test
	 */
	private void fetchNames(Boolean test) {
		try {
			if (test) {
				this.reader.resetReader(InitService.NAME_TEST_NAMES);
			} else {
				this.reader.resetReader(InitService.NAME_NAMES);
			}
			String line = null;
			while ((line = this.reader.readLine()) != null) {
				String id = Parser.parseId(line);
				String name = Parser.parseName(line);
				if (id == null || name == null) {
					//this.skippedEntities++;
					continue;
				}
				Person p = this.collection.getPerson(id);
				if (p == null) {
					//this.errors++;
					//System.err.print("Person date does not exist for ID: " + id + "\n");
					continue;
				}
				if (InitService.hasUnicode(name)) {
					name = StringEscapeUtils.unescapeJava(name);
				}
				p.setName(name);
				this.collection.setPerson(p);
			}
		} catch (IOException | InputFileNotSetException e) {
			//e.printStackTrace();
			this.errors++;
		}
	}

	/**
	 * Fetches deaths and adds them to existing collection
	 *
	 * @param test
	 */
	private void fetchDeaths(Boolean test) {
		try {
			if (test) {
				this.reader.resetReader(InitService.NAME_TEST_DEATHS);
			} else {
				this.reader.resetReader(InitService.NAME_DEATHS);
			}
			String line = null;
			while ((line = this.reader.readLine()) != null) {
				String id = Parser.parseId(line);
				String death = Parser.parseDateOfDeath(line);
				if (id == null || death == null) {
					//this.skippedEntities++;
					continue;
				}
				Person p = this.collection.getPerson(id);
				if (p == null) {
					//this.errors++;
					//System.err.print("Person date does not exist for ID: " + id + "\n");
					continue;
				}
				try {
					p.setDateOfDeath(InitService.convertStringDateToTime(death));
					this.collection.setPerson(p);
				} catch (ParseException e) {
					//e.printStackTrace();
					this.errors++;
				}
			}
		} catch (IOException | InputFileNotSetException e) {
			//e.printStackTrace();
			this.skippedEntities++;
		}
	}

	public PersonCollection getCollection() {
		return this.collection;
	}

	private static long getYearTime() {
		Double milis =
				InitService.MILLIS_IN_SECOND * InitService.SECONDS_IN_MINUTE * InitService.MINUTES_IN_HOUR *
						InitService.HOURS_IN_DAY * InitService.DAYS_IN_YEAR * 100;
		return milis.longValue();
	}

	private static Date convertStringDateToTime(String date) throws ParseException {
		DateFormat formatter = null;
		if (date.length() == 4) {
			formatter = new SimpleDateFormat(InitService.FORMAT_YEAR_ONLY);
		} else if (date.length() == 7) {
			formatter = new SimpleDateFormat(InitService.FORMAT_YEAR_AND_MONTH);
		} else {
			formatter = new SimpleDateFormat(InitService.FORMAT_DATE_FULL);
		}
		Date dateObject = formatter.parse(date);
		return dateObject;
	}

	private static boolean hasUnicode(String string) {
		Pattern pattern = Pattern.compile("\\\\u[0-9]+");
		Matcher matcher = pattern.matcher(string);
		if (!matcher.find()) {
			return false;
		}

		return true;
	}

}
