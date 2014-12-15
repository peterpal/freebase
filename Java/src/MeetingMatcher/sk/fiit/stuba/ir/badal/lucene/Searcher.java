package sk.fiit.stuba.ir.badal.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;

import java.io.IOException;

/**
 * Created by delve on 30/11/14.
 */
public class Searcher {
	private IndexReader reader   = null;
	private Analyzer    analyzer = null;
	private Directory   dir      = null;

	public void prepareSearcher(Directory dir, Analyzer analyzer) throws IOException {
		this.dir = dir;
		if (this.reader == null) {
			this.reader = DirectoryReader.open(this.dir);
		}
		this.analyzer = analyzer;
	}

	public Document findDocumentById(String id) throws IOException, ParseException {
		IndexSearcher searcher = new IndexSearcher(this.reader);
		Query query = new QueryParser(Indexer.KEY_ID, this.analyzer).parse(id);
		TopScoreDocCollector collector = TopScoreDocCollector.create(1, true);
		searcher.search(query, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		if (hits.length == 0) {
			return null;
		}

		int docId = hits[0].doc;
		return searcher.doc(docId);
	}

	public void closeReader() throws IOException {
		this.reader.close();
	}

	public Document findDocumentByName(String name) throws ParseException, IOException {
		IndexSearcher searcher = new IndexSearcher(this.reader);
		Query query = new QueryParser(Indexer.KEY_NAME, this.analyzer).parse(name);
		TopScoreDocCollector collector = TopScoreDocCollector.create(1, true);
		searcher.search(query, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		if (hits.length == 0) {
			return null;
		}

		int docId = hits[0].doc;
		return searcher.doc(docId);
	}
}
