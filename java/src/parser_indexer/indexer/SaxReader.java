package parser_indexer.indexer;

import java.io.FileReader;
import java.io.IOException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;


/**
 * Wrapper for SAX, rovide parsing from XML file (streaming)
 * @author Bc. Krisitna Misikova
 *
 */
public class SaxReader
{

	private XMLReader xmlReader;
	
	private String filename;
	
	/**
	 * Prepare SAX Reader, initialize handler with reference to luceneIndexBuilder
	 * @param luceneIndexBuilder
	 * @throws SAXException
	 */
	public SaxReader(LuceneIndexBuilder luceneIndexBuilder) throws SAXException 
	{
	    
		// my custom handler for SAX events (startElement event)
		CustomHandler saxEventsHandler = new CustomHandler(luceneIndexBuilder);
		
		// XML reader with custom handler
		this.xmlReader = XMLReaderFactory.createXMLReader();
	    xmlReader.setContentHandler(saxEventsHandler);
	    	    
	}
	
	/**
	 * Parse objects from XML file
	 * @param filename
	 * @throws IOException
	 * @throws SAXException
	 */
	public void readFromXml(String filename) throws IOException, SAXException
	{
		
		// read XML file
		FileReader r = new FileReader(filename);
		
		// parse XML file
	    this.xmlReader.parse(new InputSource(r));
		
			
	}
	
}