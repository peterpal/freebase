package parser_indexer.indexer;

import java.io.IOException;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class Indexer {

	public static void main(String[] args) throws SAXException, IOException
	{
		
		// input for indexing
		String xmlFileName = "data_tmp/freebase-rdf-2014-10-05-00-00_output_EN_escaped.xml";
		
		// index name
		String indexName = "data_tmp/lucene_index_freebase_ENj";
		
		
		// initialise Lucene indexer
		LuceneIndexBuilder luceneIndexBuilder = new LuceneIndexBuilder(indexName);
		
		// initialise SAX parser (reference to lucene -> creating and adding new documents directly to index)
		SaxReader saxReader = new SaxReader(luceneIndexBuilder);
		
		try {
			saxReader.readFromXml(xmlFileName);
		}
		
		catch (Exception e){
			System.out.println(e.getMessage());
		}
		
		
		
		luceneIndexBuilder.close();
		
		
	}
	
}
