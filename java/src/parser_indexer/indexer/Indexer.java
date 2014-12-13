package parser_indexer.indexer;

import java.io.IOException;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;


/**
 * Program for creating Lucene index from XML file
 * @author Bc. Krisitna Misikova
 *
 */
public class Indexer {

	public static void main(String[] args) throws SAXException, IOException
	{
		
		// input for indexing
		String xmlFileName = "data_tmp/freebase-rdf-2014-10-05-00-00_output_EN_escaped.xml";
		
		// index name
		String indexName = "data_tmp/lucene_index_freebase_EN";
		
		
		// initialize Lucene indexer
		LuceneIndexBuilder luceneIndexBuilder = new LuceneIndexBuilder(indexName);
		
		// initialize SAX parser (reference to lucene -> creating and adding new documents directly to index)
		SaxReader saxReader = new SaxReader(luceneIndexBuilder);
		
		try {
			// start reading and parsing XML input
			saxReader.readFromXml(xmlFileName);
		}
		
		catch (Exception e){
			System.out.println(e.getMessage());
		}
		
		
		// close index for data integrity
		luceneIndexBuilder.close();
		
		
	}
	
}
