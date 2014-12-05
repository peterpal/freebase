package parser_indexer.parser;

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
