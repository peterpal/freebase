package parser_indexer.indexer;



import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Handles events raised by SAX, eg. startElement
 * @author Bc. Krisitna Misikova
 *
 */
public class CustomHandler extends DefaultHandler
{

	private Document actDocument = null;
    private LuceneIndexBuilder luceneIndexBuilder;
    
    public CustomHandler(LuceneIndexBuilder luceneIndexBuilder)
    {
    	super();
    	
    	// store reference to indexer for accessing add function
    	this.luceneIndexBuilder = luceneIndexBuilder;
	}
    
 // SAX events handlers
        
    @ Override
    public void fatalError(org.xml.sax.SAXParseException arg0){
    	System.out.println("Fatal error");
    }
    
    /**
     * if SAX parser parse starting element, calls this function
     * we handle this event as we want, depending on starting element name
     */
 	@ Override
    public void startElement (String uri, String name, String qName, Attributes atts)
 	{
     	switch (qName) {
 			case "object":
 				//System.out.println("Object");
 				this.handleObject(atts);
 				break;	
 				
 			case "type":
 				//System.out.println("type");
 				this.handleType(atts);
 				break;			
 				
 			case "alias":
 				//System.out.println("alias");
 				this.handleAlias(atts);
 				break;
 							
 			default:
 				break;
 		
     	}
 	
 	}
 	
 	/**
 	 * new object -> store previous to index and create new instance for next object
 	 * @param atts
 	 */
 	private void handleObject(Attributes atts)
    {    	
    	// store previous object to lucene
		if(actDocument != null){
			luceneIndexBuilder.appendDocument(actDocument);
		}
		
		// create new object
		actDocument = new Document();
		
		// fill basic attributes
		// name (if exists)
		String n = atts.getValue("name");
		if(!n.equals("")){
			actDocument.add(new TextField("name", n, Field.Store.YES));
		}
		
		//id
		String id = atts.getValue("id");
		actDocument.add(new StringField("id", id, Field.Store.YES));
		
    }
    
 	/**
 	 * append type attribute to document
 	 * @param atts
 	 */
    private void handleType(Attributes atts)
    {
    	// append type to object
		String type = atts.getValue("object_ref");
		actDocument.add(new StringField("type", type, Field.Store.YES));
    	
    }
    
    /**
     * append alias attribute to document
     * @param atts
     */
    private void handleAlias(Attributes atts)
    {
    	// append alias to object
		String alias = atts.getValue("value");
		actDocument.add(new TextField("alias", alias, Field.Store.YES));
		
    }
	
}
