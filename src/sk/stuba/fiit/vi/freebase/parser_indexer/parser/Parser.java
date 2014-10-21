package sk.stuba.fiit.vi.freebase.parser_indexer.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Parser
{

	private ArrayList<Subject> subjects = new ArrayList<Subject>();
	private Subject subject = null;
	
	private String output = null;
	private int objectsInOutput = 0;
	private int saveAfter = 0;
	
	
	// all subjects are in memory
	public Parser()
	{
		
	}
	
	
	
	// output to file, subjects are flush
	public Parser(String output, int saveAfter)
	{
		
		this.output = output;
		this.saveAfter  = saveAfter;
		
	}
	
	
	
	public ArrayList<Subject> getSubjects()
	{
		
		return this.subjects;
		
	}
	
	
	
	private Triplet parseTripletFromLine(String line)
	{
		
		// split line on >
		String[] line_components = line.split(">");
		
		// clean components - remove <, remove url, trim spaces and remove " and @en
		for(int i = 0; i < 3; i++)
		{
			line_components[i] = line_components[i].replace("http://rdf.freebase.com/ns/", "").replace("<", "").replace("\"","").trim().split("@")[0];
			//System.out.println(line_components[i]);
		}
		
		String subject = line_components[0];
		String predicate = line_components[1];
		String object = line_components[2];
		
		// <subject> <predicate> <object>
		return new Triplet(subject, predicate, object);
		
	}
	
	

	public void parseFile(String fileName) throws IOException 
	{
		
		InputStream    fileInputStream;
		BufferedReader buffReader;
		String line = "";

		fileInputStream = new FileInputStream(fileName);
		buffReader = new BufferedReader(new InputStreamReader(fileInputStream));
		
		// read lines from file and parsing triplets
		while ((line = buffReader.readLine()) != null)
		{

			// parse triplet from line
			Triplet triplet = parseTripletFromLine(line);
			
			// is tripled relevant to us? if not we ignore him
			if(!triplet.isValidTriplet()) 
			{ 
				continue; 
			}
			
			// if subject with ID doesn't exists, we create new subject
			if(subject == null || !subject.hasProperty("id",triplet.getSubjectString()))
			{
				
				// if exist any subject, we move him to arraylist
				if(subject != null)
				{ 
					subjects.add(subject);
					
					this.writeToFile(false);					
					//System.out.println(subject.toString());
				}
				
				// new Subject with ID
				subject = new Subject(triplet.getSubjectString());
			}
			
			// we add property to current subject
			subject.addProperty(triplet);
			
		}
		
		// add last subject to list
		subjects.add(subject);

		buffReader.close();
		
		this.writeToFile(true);
		
	}
	
	private void writeToFile(Boolean lastSubjects) throws IOException
	{
			
		if(this.output == null)
		{
			return;
		}
		
		if(lastSubjects || subjects.size() >= this.saveAfter)
		{
			
			FileWriter fstream = new FileWriter(this.output, true);
	        BufferedWriter out = new BufferedWriter(fstream);
			
	        // write opening root tag 
			if(this.objectsInOutput == 0)
			{
				out.append("<objects>\n");
			}
	        
			for(int i = 0; i < subjects.size(); i++)
			{
				
				out.append(subjects.get(i).toString());
				//System.out.println(subjects.get(i).toString());
				
			}
			
			this.objectsInOutput += subjects.size();
			
			// write closing root tag
			if(lastSubjects)
			{
				out.append("</objects>\n");
			}			
			
			out.close();
			
			
			//System.out.println("----------------------------------");
		
			subjects.clear();			
		
		}
		
		
		
		
	}
	
	
}
