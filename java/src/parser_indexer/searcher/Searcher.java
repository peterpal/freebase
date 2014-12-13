package parser_indexer.searcher;

import java.io.File;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import parser_indexer.helpers.DocumentHelper;


/**
 * Prototype of search application, based on example from Lucene (http://lucene.apache.org/)
 * @author Bc. Krisitna Misikova
 *
 */
public class Searcher {

	public static void main(String[] args)
	{
		
		String indexName = "data_tmp/lucene_index_freebase_EN";
		
		
		try {
			
			
			StandardAnalyzer analyzer = new StandardAnalyzer();
			
			Directory index = new SimpleFSDirectory(new File(indexName));
			//Directory index = new RAMDirectory(dir, null);
						

			//	Text to search
			String querystr = "Bruce";
			
			//	The \"title\" arg specifies the default field to use when no field is explicitly specified in the query
			Query q1 = new QueryParser("name", analyzer).parse(querystr);
			Query q2 = new QueryParser("alias", analyzer).parse(querystr);
			
			
			// Searching code
			int hitsPerPage = 100;
		    IndexReader reader = DirectoryReader.open(index);
		    IndexSearcher searcher = new IndexSearcher(reader);
		    TopScoreDocCollector collector1 = TopScoreDocCollector.create(hitsPerPage, true);
		    TopScoreDocCollector collector2 = TopScoreDocCollector.create(hitsPerPage, true);
		    
		    searcher.search(q1, collector1);
		    searcher.search(q2, collector2);
		    
		    ScoreDoc[] hits1 = collector1.topDocs().scoreDocs;
		    ScoreDoc[] hits2 = collector2.topDocs().scoreDocs;
		    
		    //	Code to display the results of search
		    System.out.println("Found " + hits1.length + " hits.");
		    for(int i=0;i<hits1.length;++i) 
		    {
		      int docId = hits1[i].doc;
		      Document d = searcher.doc(docId);
		      System.out.println((i + 1) + ".\n" + DocumentHelper.docToString(d));
		    }
		    
		    System.out.println("---\nFound " + hits2.length + " hits.");
		    for(int i=0;i<hits2.length;++i) 
		    {
		      int docId = hits2[i].doc;
		      Document d = searcher.doc(docId);
		      System.out.println((i + 1) + ".\n" + DocumentHelper.docToString(d));
		    }
		    
		    // reader can only be closed when there is no need to access the documents any more
		    reader.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
	}
	
}
