package parser_indexer.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import parser_indexer.parser.Parser;
import parser_indexer.parser.Subject;


/**
 * 
 * @author Bc. Krisitna Misikova
 * 
 * Parse provided sample file to check corret functionality of parser.
 * This is template test - it can be modified to parse any file and any object. 
 * 
 */
public class ParserTest
{

	private Parser parser = null;
	
	
	// simple test initialization
	public ParserTest() throws Exception
	{
		
		// instantiate Parser object, no params = parsed data will be stored in memory
		this.parser = new Parser();
		
		try
		{
			// Input file for parser - required format: triplets (https://developers.google.com/freebase/data)
			parser.parseFile("../../../../data/sample_freebase-rdf-2014-09-21-00-0_input");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	

	
	
	@Test
	public void numberOfResultsTest()
	{
		// check if number of parsed objects is correct
		ArrayList<Subject> parseResult = parser.getSubjects();
		
		assertEquals(17, parseResult.size());		
		
	}
	
	
	
	@Test
	public void numberOfResultsWithNameTest()
	{
		// check if number of parsed objects that contains name attribute is correct
		ArrayList<Subject> parseResult = parser.getSubjects();
		
		int count = 0;
		for(int i = 0; i < parseResult.size(); i++)
		{
			if(parseResult.get(i).hasProperty("name", null))
			{
				count++;
			}
		}
		
		assertEquals(8, count);		
		
	}
	
	
	
	@Test
	public void numberOfResultsWithAnyAliasTest()
	{
		// check if number of parsed objects with one or more aliases attributes is correct
		ArrayList<Subject> parseResult = parser.getSubjects();
		
		int count = 0;
		for(int i = 0; i < parseResult.size(); i++)
		{
			if(parseResult.get(i).hasProperty("alias", null))
			{
				count++;
			}
		}
		
		assertEquals(3, count);		
		
	}
	
	
	
	/**
	 *  Test first parsed object and his attributes
	 */
	@Test
	public void firstSubjectIsCompleteTest()
	{
		ArrayList<Subject> parseResult = parser.getSubjects();
		
		// first parsed subject
		Subject subject = parseResult.get(0);
		
		// check if object has desired attributes with correct values
		assertTrue(subject.hasProperty("id", "m.011fdsb9"));
		assertTrue(subject.hasProperty("type", "film.performance"));
		
		// check if name and alias doesn´t exist
		assertFalse(subject.hasProperty("name", null));
		assertFalse(subject.hasProperty("alias", null));
		
	}
	
	
	/**
	 *  Test second parsed object and his attributes
	 */
	@Test
	public void secondSubjectIsCompleteTest()
	{
		
		ArrayList<Subject> parseResult = parser.getSubjects();
		
		Subject subject = parseResult.get(1);
		
		// check if object has desired attributes with correct values
		assertTrue(subject.hasProperty("id", "m.011fdvnf"));
		assertTrue(subject.hasProperty("name", "Good Friday nominal occurrence (Valais, 2007)"));
		assertTrue(subject.hasProperty("type", "base.schemastaging.holiday_occurrence"));
		assertTrue(subject.hasProperty("type", "common.topic"));
		
		// check if alias doesn´t exist
		assertFalse(subject.hasProperty("alias", null));
		
	}																							

	
	/**
	 *  Test third parsed object and his attributes
	 */
	@Test
	public void thirdSubjectIsCompleteTest()
	{
				
		ArrayList<Subject> parseResult = parser.getSubjects();
		
		Subject subject = parseResult.get(2);
		
		assertTrue(subject.hasProperty("id", "m.011ff4v1"));
		assertTrue(subject.hasProperty("name", "Werner Riedl"));
		
		assertTrue(subject.hasProperty("type", "base.type_ontology.physically_instantiable"));
		assertTrue(subject.hasProperty("type", "common.topic"));
		assertTrue(subject.hasProperty("type", "base.type_ontology.animate"));
		assertTrue(subject.hasProperty("type", "base.type_ontology.agent"));
		assertTrue(subject.hasProperty("type", "music.group_member"));
		assertTrue(subject.hasProperty("type", "people.person"));
		
		assertFalse(subject.hasProperty("type", "film.performance"));
		assertFalse(subject.hasProperty("alias", null));
	}
	
	
	/**
	 *  Test fourth parsed object and his attributes
	 */
	@Test
	public void fourthSubjectIsCompleteTest()
	{
				
		ArrayList<Subject> parseResult = parser.getSubjects();
		
		Subject subject = parseResult.get(3);
		
		assertTrue(subject.hasProperty("id", "m.011ff5wg"));
		assertTrue(subject.hasProperty("type", "film.performance"));
		
		assertFalse(subject.hasProperty("name", null));
		assertFalse(subject.hasProperty("alias", null));
		
	}

}
