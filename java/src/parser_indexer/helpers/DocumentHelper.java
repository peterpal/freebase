package parser_indexer.helpers;

import org.apache.lucene.document.Document;


/**
 * Helper functions for Lucene Document object
 * @author Bc. Kristina Misikova
 *
 */
public class DocumentHelper {
	
	/**
	 * To string method for Lucene Document
	 * @param doc
	 * @return
	 */
	public static String docToString(Document doc)
	{
		String stringDoc = "";
		
		stringDoc += "Object: " + doc.get("id") + "\n{\n";
		stringDoc += "  Name: " + doc.get("name") + "\n";
		stringDoc += "  Aliases:\n";
		
		String[] aliases = doc.getValues("alias");
		for(int i = 0; i < aliases.length; i++){
			stringDoc += "    " + aliases[i] + "\n";
		}
		if(aliases.length == 0){
			stringDoc += "    - \n";
		}
		
		stringDoc += "  Types:\n";
		
		String[] types = doc.getValues("type");
		for(int i = 0; i < types.length; i++){
			stringDoc += "    " + types[i] + "\n";
		}
		if(types.length == 0){
			stringDoc += "    - \n";
		}
		
		stringDoc += "}\n";
		
		return stringDoc;
	}

}
