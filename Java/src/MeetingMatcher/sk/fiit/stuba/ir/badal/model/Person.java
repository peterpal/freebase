package sk.fiit.stuba.ir.badal.model;


import java.util.Date;

/**
 * Created by delve on 11/10/14.
 */
public class Person {


	private String id;
	private String name;
	private Date dateOfBirth = null;
	private Date dateOfDeath = null;

	public String getName() {
		return this.name;
	}

	public Person setName(String name) {
		this.name = name;
		return this;
	}

	public Person setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
		return this;
	}

	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public Date getDateOfDeath() {
		return this.dateOfDeath;
	}

	public Person setDateOfDeath(Date dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
		return this;
	}

	/*public long getDateOfBirthAsTime() {
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
	}*/

	public String getId() {
		return id;
	}

	public Person setId(String id) {
		this.id = id;
		return this;
	}

	public String toString() {
		return "[" + this.name + ", " + this.dateOfBirth + ", " + this.dateOfDeath + "]";
	}

	public boolean isComplete() {
		if (this.id != null && this.name != null && this.dateOfBirth != null && this.dateOfDeath != null) {
			return true;
		}
		return false;
	}
}
