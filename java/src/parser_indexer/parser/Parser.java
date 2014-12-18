package parser_indexer.parser;

import java.io.BufferedReader;


import parser_indexer.helpers.UnicodeUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;



/**
 * Parse triplets from freebase to objects and generates XML output
 * @author Bc. Krisitna Misikova
 *
 */
public class Parser
{
	// parsed objects stored in Subject class instances
	private ArrayList<Subject> subjects = new ArrayList<Subject>();
	// actually parsed object
	private Subject subject = null;
	
	// XML output file name
	private String output = null;
	
	// counter: saved objects in XML file
	private int objectsInOutput = 0;
	
	// number of parsed object triggering writing to file
	private int saveAfter = 0;
	
	
	public static void main(String[] args) throws Exception
	{
		
		// Parser initialization with output filename and "save after" attribute
		Parser parser = new Parser("data_tmp/freebase-rdf-2014-10-05-00-00_output_EN.xml", 10000);
		
		try
		{
			// start parsing provided freebase dump in gzip format
			parser.parseGzipFile("data_tmp/freebase-rdf-2014-10-05-00-00_names_aliases_types_en_only.gz");
			
		} 
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	
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
	
	
	/**
	 * Parse triplets from freebase dump to Triplet object
	 * @param line
	 * @return Triplet
	 */
	private Triplet parseTripletFromLine(String line)
	{
		/* triplet: 
		 		<http://rdf.freebase.com/ns/g.11vjz1ynm>  <http://rdf.freebase.com/ns/type.object.type> <http://rdf.freebase.com/ns/measurement_unit.dated_percentage>  .
		*/
		
		// split line on >
		String[] line_components = line.split(">");
		
		/* line_components: 
		 		<http://rdf.freebase.com/ns/g.11vjz1ynm
		 		<http://rdf.freebase.com/ns/type.object.type
 				<http://rdf.freebase.com/ns/measurement_unit.dated_percentage
  				.
		*/
		
		// clean components - remove <, remove url, trim spaces and remove " and @en
		String subject = line_components[0].replace("http://rdf.freebase.com/ns/", "").replace("<", "").replace("\"","").trim();;
		String predicate = line_components[1].replace("http://rdf.freebase.com/ns/", "").replace("<", "").replace("\"","").trim();;
		String object = line_components[2].replace("&", "&amp;").replace("http://rdf.freebase.com/ns/", "").replace("<", "").replace("\"","").trim().split("@en")[0];
		
		// subject: g.11vjz1ynm
		// predicate: type.object.type
		// object: measurement_unit.dated_percentage
		
		// <subject> <predicate> <object>
		return new Triplet(subject, predicate, object);
		
	}
	
	/**
	 * provide parsing functionality from freebase dump in gzip format
	 * parsed objects are stored in arraylist
	 * objects can be flushed to file in XML format
	 * @param fileName
	 * @throws Exception
	 */
	public void parseGzipFile(String fileName) throws Exception {
		
		InputStream fileInputStream; 
		InputStream gzipStream;
		Reader decoder;
		BufferedReader buffReader;
		
		fileInputStream = new FileInputStream(fileName);
		gzipStream = new GZIPInputStream(fileInputStream);
		decoder = new InputStreamReader(gzipStream, "utf-8");
		buffReader = new BufferedReader(decoder);
		
		
		String line = "";
		
		// read lines from file and parsing triplets
		while ((line = buffReader.readLine()) != null) {

			// parse triplet from line
			Triplet triplet = parseTripletFromLine(line);
			
			// is tripled relevant to us? if not we ignore him
			if(!triplet.isValidTriplet()) { continue; }
			
			// if subject with ID doesn't exists, we create new subject
			if(subject == null || !subject.hasProperty("id",triplet.getSubjectString())){
				
				// if exist any subject, we move him to arraylist
				if(subject != null) { 
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

	public void parseFile(String fileName) throws Exception 
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
	
	/*
	
	private void writeToFile(Boolean lastSubjects) throws IOException{
		
		if(this.output == null){
			return;
		}
		
		if(lastSubjects || subjects.size() >= this.saveAfter){
			
			System.out.println("writing to file " + this.output + ". Objects: " + this.objectsInOutput);
			
			FileWriterWithEncoding fstream = new FileWriterWithEncoding(this.output, "UTF-8", true);
	        BufferedWriter out = new BufferedWriter(fstream);
			
	        // write opening root tag 
			if(this.objectsInOutput == 0){
				out.append("<objects>\n");
			}
	        
			for(int i = 0; i < subjects.size(); i++){
				
				if(subjects.get(i).hasProperty("name", null)){
					out.append(subjects.get(i).toString());
					this.objectsInOutput++;
				}
				//System.out.println(subjects.get(i).toString());
				
			}		
			
			// write closing root tag
			if(lastSubjects){
				out.append("</objects>\n");
			}			
			
			out.close();
			
			
			//System.out.println("----------------------------------");
		
			subjects.clear();			
		
		}
		
		
		
		
	}
	
	*/
	
	
	/**
	 * function handling writing XML to output file
	 * function is called after each parsed object
	 * @param lastSubjects
	 * @throws Exception
	 */
	private void writeToFile(Boolean lastSubjects) throws Exception{
		
		// if output isn´t defined, do nothing
		if(this.output == null){
			return;
		}
		
		// if is reached last portion of subjects from freebase dump, or required number of object is parsed, write objects to file
		if(lastSubjects || subjects.size() >= this.saveAfter){
			
			System.out.println("writing to file " + this.output + ". Objects: " + this.objectsInOutput);
			
			// append to output file
			FileOutputStream fstream = new FileOutputStream(this.output, true);
	        //BufferedWriter out = new BufferedWriter(fstream);
			
	        // on the beginning, write opening root tag <objects> 
			if(this.objectsInOutput == 0){
				String s = "<objects>\n";
				byte[] out = UnicodeUtil.convert(s.getBytes(), "UTF-8");
				fstream.write(out);
			}
	        
			// write all parsed objects with name attribute
			for(int i = 0; i < subjects.size(); i++){
				
				if(subjects.get(i).hasProperty("name", null)){
					byte[] out = UnicodeUtil.convert(subjects.get(i).toString().getBytes(), "UTF-8");
					fstream.write(out);
					
					this.objectsInOutput++;
				}
				//System.out.println(subjects.get(i).toString());
				
			}		
			
			// write closing root tag </objects> after last parsed objects
			if(lastSubjects){
				String s = "</objects>\n";
				byte[] out = UnicodeUtil.convert(s.getBytes(), "UTF-8");
				fstream.write(out);
			}			
			
			fstream.close();
			
			
			//System.out.println("----------------------------------");
		
			// empty array list with parsed objects
			subjects.clear();			
		
		}
		
		
		
		
	}
	
	
	
}
