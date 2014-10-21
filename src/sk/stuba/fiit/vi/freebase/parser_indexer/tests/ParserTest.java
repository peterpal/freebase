package sk.stuba.fiit.vi.freebase.parser_indexer.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import sk.stuba.fiit.vi.freebase.parser_indexer.parser.Parser;
import sk.stuba.fiit.vi.freebase.parser_indexer.parser.Subject;

public class ParserTest
{

	private Parser parser = null;
	
	
	// simple test initialisation
	public ParserTest()
	{
		
		this.parser = new Parser();
		
		try
		{
			parser.parseFile("sample_freebase-rdf-2014-09-21-00-0_input");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	

	
	
	@Test
	public void numberOfResultsTest()
	{
		
		ArrayList<Subject> parseResult = parser.getSubjects();
		
		assertEquals(17, parseResult.size());		
		
	}
	
	
	
	@Test
	public void numberOfResultsWithNameTest()
	{
		
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
	
	
	
	@Test
	public void firstSubjectIsCompleteTest()
	{
		
		ArrayList<Subject> parseResult = parser.getSubjects();
		
		Subject subject = parseResult.get(0);
		
		assertTrue(subject.hasProperty("id", "m.011fdsb9"));
		assertTrue(subject.hasProperty("type", "film.performance"));
		
		assertFalse(subject.hasProperty("name", null));
		assertFalse(subject.hasProperty("alias", null));
		
	}
	
	
	
	@Test
	public void secondSubjectIsCompleteTest()
	{
		
		ArrayList<Subject> parseResult = parser.getSubjects();
		
		Subject subject = parseResult.get(1);
		
		assertTrue(subject.hasProperty("id", "m.011fdvnf"));
		assertTrue(subject.hasProperty("name", "Good Friday nominal occurrence (Valais, 2007)"));
		
		assertTrue(subject.hasProperty("type", "base.schemastaging.holiday_occurrence"));
		assertTrue(subject.hasProperty("type", "common.topic"));
		
		assertFalse(subject.hasProperty("alias", null));
		
	}																							

	
	
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
