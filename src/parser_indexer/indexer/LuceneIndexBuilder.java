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



//class for creating index 
public class LuceneIndexBuilder 
{
	private StandardAnalyzer analyzer;
	private Directory index;
	private IndexWriterConfig config;
	private IndexWriter indexWriter;

	
	@SuppressWarnings("deprecation")
	public LuceneIndexBuilder(String indexName) throws IOException{
		
		this.analyzer = new StandardAnalyzer();
		
		this.index = new SimpleFSDirectory(new File(indexName));
		
		this.config = new IndexWriterConfig(null, this.analyzer);
		
		this.indexWriter = new IndexWriter(index, config);
		
		//Directory index = new RAMDirectory();

	}
	
	
	// adds document to index (called from CustomHandler)
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

	public void close() {
		try {
			this.indexWriter.close();
		} 
		
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
