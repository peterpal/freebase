package parser_indexer.parser;


/**
 * object containing parsed data from single line - triplet of freebase dump 
 * @author Bc. Krisitna Misikova
 *
 */
public class Triplet 
{

	private String subject;
	private String predicate;
	private String object;
		
	public Triplet(String sub, String pred, String obj)
	{
	
		this.subject = sub;
		this.predicate = pred;
		this.object = obj;
		
		
		
	}
	
	/**
	 * validation if this is triplet object with content we want (required only if we parse unfiltered freebase)
	 * @return
	 */
	public Boolean isValidTriplet()
	{
		// evaluate if triplet is what we need
		if(this.predicate.contains("/"))
		{
			return false;
		}
		
		if(!this.object.contains("@en") && !this.predicate.contains(".type")){
			//return false;
		}
		
		// we want only triplets with predicates type, name, alias
		if((!this.predicate.contains(".type") && !this.predicate.contains(".name") && !this.predicate.contains(".alias")))
		{
			return false;
		}	
	
		return true;
		
	}

	
	public String getSubjectString() 
	{
		return this.subject;
	}
	
	public String getPredicateString() 
	{
		return this.predicate;
	}
	
	public String getObjectString() 
	{
		return this.object;
	}
	
}
