package sk.fiit.stuba.ir.badal.model;


import java.util.Date;

/**
 * @author Matej Bádal <matejbadal@gmail.com>
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
