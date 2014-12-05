package parser_indexer.indexer;

import java.io.FileReader;
import java.io.IOException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

// wrapper for sax 
public class SaxReader
{

	private XMLReader xmlReader;
	
	private String filename;
	
	
	public SaxReader(LuceneIndexBuilder luceneIndexBuilder) throws SAXException 
	{
	    
		// my custom handler for SAX events (startElement event)
		CustomHandler saxEventsHandler = new CustomHandler(luceneIndexBuilder);
		
		// XML reader with custom handler
		this.xmlReader = XMLReaderFactory.createXMLReader();
	    xmlReader.setContentHandler(saxEventsHandler);
	    	    
	}
	
	
	public void readFromXml(String filename) throws IOException, SAXException
	{
		
		// read XML file
		FileReader r = new FileReader(filename);
		
		// parse XML file
	    this.xmlReader.parse(new InputSource(r));
		
			
	}
	
}