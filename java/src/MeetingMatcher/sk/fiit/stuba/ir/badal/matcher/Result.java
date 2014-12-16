package sk.fiit.stuba.ir.badal.matcher;

import sk.fiit.stuba.ir.badal.model.Person;

/**
 * @author Matej Bádal <matejbadal@gmail.com>
 */
public class Result {

	public static int CODE_MET       = 100;
	public static int CODE_NOT_MET   = 200;
	public static int CODE_NOT_FOUND = 400;

	public static final String MESSAGE_YES = "Yes, persons could have met";
	public static final String MESSAGE_NO = "No, persons could not have met";
	public static final String MESSAGE_NOT_FOUND = "One or both persons were not found";

	private Person first  = null;
	private Person second = null;

	private String message;
	private int    code;

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String toString() {

		return first + "\n" + second + "\n[" + this.message + "]";
	}

	public int getCode() {
		return this.code;
	}

	public Result setFirst(Person first) {
		this.first = first;
		return this;
	}

	public Result setSecond(Person second) {
		this.second = second;
		return this;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
