package sk.fiit.stuba.ir.badal.lucene;

import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import sk.fiit.stuba.ir.badal.model.Person;

import java.io.*;
import java.text.ParseException;
import java.util.Date;

/**
 * @author Matej Bádal <matejbadal@gmail.com>
 */
public class Indexer {
	public static final String KEY_NAME  = "name";
	public static final String KEY_BIRTH = "dateOfBirth";
	public static final String KEY_DEATH = "dateOfDeath";
	public static final String KEY_ID    = "ID";

	private IndexWriter writer = null;

	public void openWriter(Directory dir, IndexWriterConfig config) throws IOException {
		if (this.writer != null) {
			return;
		}
		this.writer = new IndexWriter(dir, config);
	}

	public void closeWriter() throws IOException {
		if (this.writer != null) {
			this.writer.close();
		}

		this.writer = null;
	}

	public void addPerson(Person p) throws IOException, ParseException {
		Document doc = new Document();
		doc = Indexer.addField(doc, Indexer.KEY_ID, p.getId());
		doc = Indexer.addField(doc, Indexer.KEY_NAME, p.getName().replaceAll(" ", "_"));
		doc = Indexer.addField(doc, Indexer.KEY_BIRTH, p.getDateOfBirth());
		doc = Indexer.addField(doc, Indexer.KEY_DEATH, p.getDateOfDeath());


		this.writer.addDocument(doc);
		this.writer.commit();
	}


	private static Document addField(Document doc, String key, String field) throws IOException {
		doc.add(new TextField(key, field, Field.Store.YES));

		return doc;
	}

	private static Document addField(Document doc, String key, Date field) throws IOException, ParseException {
		String date = DateTools.dateToString(field, DateTools.Resolution.DAY);
		doc.add(new StringField(key, date, Field.Store.YES));
		return doc;
	}
}
