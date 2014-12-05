package parser_indexer.indexer;



import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;


// class for handling sax events
public class CustomHandler extends DefaultHandler
{

	private Document actDocument = null;
    private LuceneIndexBuilder luceneIndexBuilder;
    
    public CustomHandler(LuceneIndexBuilder luceneIndexBuilder)
    {
    	super();
    	
    	// store reference to indexer
    	this.luceneIndexBuilder = luceneIndexBuilder;
	}
    
    // SAX events handlers
        
    @ Override
    public void fatalError(org.xml.sax.SAXParseException arg0){
    	System.out.println("Fatal error");
    }
    
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
 	
 	// custom functions for processing data
 	
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
    
    private void handleType(Attributes atts)
    {
    	// append type to object
		String type = atts.getValue("object_ref");
		actDocument.add(new StringField("type", type, Field.Store.YES));
    	
    }
    
    private void handleAlias(Attributes atts)
    {
    	// append alias to object
		String alias = atts.getValue("value");
		actDocument.add(new TextField("alias", alias, Field.Store.YES));
		
    }
	
}
