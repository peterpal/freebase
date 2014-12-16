package sk.fiit.stuba.ir.badal.lucene.service;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import sk.fiit.stuba.ir.badal.lucene.Indexer;
import sk.fiit.stuba.ir.badal.lucene.Searcher;
import sk.fiit.stuba.ir.badal.model.Person;
import sk.fiit.stuba.ir.badal.model.PersonCollection;

import java.io.*;

/**
 * @author Matej Bádal <matejbadal@gmail.com>
 */
public class LuceneService {
	public static final String PATH_INDEX = "./index";

	private        Directory         dir      = null;
	private        Analyzer          analyzer = null;
	private        IndexWriterConfig config   = null;
	private        Indexer           indexer  = null;
	private        Searcher          searcher = null;
	private static LuceneService     self     = null;

	private LuceneService() throws IOException {
		this.analyzer = new StandardAnalyzer();
		this.indexer = new Indexer();
		this.openDirectory();
	}

	public static LuceneService getInstance() throws IOException {
		if (LuceneService.self == null) {
			LuceneService.self = new LuceneService();
		}

		return LuceneService.self;
	}

	public void openDirectory() throws IOException {
		this.dir = FSDirectory.open(new File(LuceneService.PATH_INDEX));
	}


	public void openWriter() throws IOException {
		this.config = new IndexWriterConfig(Version.LATEST, this.analyzer);
		this.config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		this.indexer.openWriter(this.dir, this.config);
	}

	public void addPerson(Person person) {
		try {
			this.indexer.addPerson(person);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
	}

	public void closeWriter() throws IOException {
		this.indexer.closeWriter();
	}

	public void resetSearcher() throws IOException {
		this.searcher = new Searcher();
		this.searcher.prepareSearcher(this.dir, this.analyzer);
	}

	public void clearIndexDir() {
		File dir = new File(LuceneService.PATH_INDEX);
		for (File file : dir.listFiles()) {
			file.delete();
		}
	}

	public PersonCollection findMatchByNames(String name1, String name2) throws IOException, ParseException,
			java.text.ParseException, NullPointerException {
		this.resetSearcher();
		Document doc1 = this.searcher.findDocumentByName(name1);
		Document doc2 = this.searcher.findDocumentByName(name2);
		PersonCollection collection = new PersonCollection();
		collection.addPerson(LuceneService.transformDocIntoPerson(doc1))
				  .addPerson(LuceneService.transformDocIntoPerson(doc2));

		return collection;
	}

	public PersonCollection findMatchByIds(String id1, String id2) throws IOException, ParseException,
			java.text.ParseException, NullPointerException {
		this.resetSearcher();
		Document doc1 = this.searcher.findDocumentById(id1);
		Document doc2 = this.searcher.findDocumentById(id2);
		//System.out.println(doc1 + " " + doc2);
		PersonCollection collection = new PersonCollection();
		collection.addPerson(LuceneService.transformDocIntoPerson(doc1))
				  .addPerson(LuceneService.transformDocIntoPerson(doc2));

		return collection;
	}

	private static Person transformDocIntoPerson(Document doc) throws java.text.ParseException, NullPointerException {
		Person p = new Person();
		p.setId(doc.get(Indexer.KEY_ID)).setName(doc.get(Indexer.KEY_NAME).replaceAll("_", " "))
		 .setDateOfBirth(DateTools.stringToDate(doc.get(Indexer.KEY_BIRTH)))
		 .setDateOfDeath(DateTools.stringToDate(doc.get(Indexer.KEY_DEATH)));
		return p;
	}
}