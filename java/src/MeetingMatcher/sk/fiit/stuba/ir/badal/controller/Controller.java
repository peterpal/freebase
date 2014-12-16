package sk.fiit.stuba.ir.badal.controller;

import org.apache.lucene.queryparser.classic.ParseException;
import sk.fiit.stuba.ir.badal.init.InitService;
import sk.fiit.stuba.ir.badal.lucene.service.LuceneService;
import sk.fiit.stuba.ir.badal.matcher.MeetingMatcher;
import sk.fiit.stuba.ir.badal.matcher.Result;
import sk.fiit.stuba.ir.badal.model.PersonCollection;

import java.io.IOException;

/**
 * @author Matej Bádal <matejbadal@gmail.com>
 */
public class Controller {
	private        LuceneService lucene = null;
	private static Controller    self   = null;

	public static Controller getInstance() {
		if (Controller.self == null) {
			Controller.self = new Controller();
		}

		return Controller.self;
	}


	private Controller() {
		try {
			this.lucene = LuceneService.getInstance();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void init() {
		try {
			InitService init = new InitService();
			init.init(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Result matchByNames(String name1, String name2) {
		try {
			PersonCollection collection = this.lucene.findMatchByNames(name1, name2);
			return MeetingMatcher.couldHaveMet(collection);
		} catch (IOException | ParseException | java.text.ParseException | NullPointerException e) {
			//e.printStackTrace();
		}

		Result result = new Result();
		result.setCode(Result.CODE_NOT_FOUND);
		result.setMessage("One or both persons were not found");
		return result;
	}

	public Result matchByIds(String name1, String name2) {
		try {
			PersonCollection collection = this.lucene.findMatchByIds(name1, name2);
			return MeetingMatcher.couldHaveMet(collection);
		} catch (IOException | ParseException | java.text.ParseException | NullPointerException e) {
			e.printStackTrace();
		}

		Result result = new Result();
		result.setCode(Result.CODE_NOT_FOUND);
		result.setMessage("One or both persons were not found");
		return result;
	}

}
