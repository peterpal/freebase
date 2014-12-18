package parser_indexer.indexer;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;



/**
 * 
 * @author Bc. Krisitna Misikova
 *
 * Container class for index builder
 * - opens new index by provided name on filesystem
 * - provide method to Lucene Documents to index
 * - provide method to close index
 * 
 */
public class LuceneIndexBuilder 
{
	private StandardAnalyzer analyzer;
	private Directory index;
	private IndexWriterConfig config;
	private IndexWriter indexWriter;

	
	public LuceneIndexBuilder(String indexName) throws IOException{
		
		// document analyzer for indexing documents
		this.analyzer = new StandardAnalyzer();
		
		// Index on filesystem (directory)
		this.index = new SimpleFSDirectory(new File(indexName));
		
		// config wrapper for indexWriter object
		this.config = new IndexWriterConfig(null, this.analyzer);
		
		
		this.indexWriter = new IndexWriter(index, config);
		
		//Directory index = new RAMDirectory();

	}
	
	/**
	 * Append document to index
	 * 
	 * @param doc
	 */
	public void appendDocument(Document doc)
	{
		
		try {
			this.indexWriter.addDocument(doc);
			//System.out.println("-> added to index.");
			//System.out.println("-> " + doc.toString());
			
		} 
		
		catch (IOException e) {
			System.out.println("IO Exception: Error while adding document to index. LuceneIndexBuilder.java -> appandDocument(Document doc)");
			e.printStackTrace();
		}
	}

	
	/**
	 *  Close index for his integrity
	 */
	public void close() {
		try {
			this.indexWriter.close();
		} 
		
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
