package sk.stuba.fiit.vi.freebase.parser_indexer;
import java.io.IOException;

import sk.stuba.fiit.vi.freebase.parser_indexer.parser.Parser;


public class Program
{

	public static void main(String[] args)
	{
		
		Parser parser = new Parser("data/sample_freebase-rdf-2014-09-21-00-0_output.xml", 5);
		
		try
		{
			parser.parseFile("data/sample_freebase-rdf-2014-09-21-00-0_input");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
}
