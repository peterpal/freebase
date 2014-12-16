/**
 * 
 */
package test.artistsGenresAwardsTracks;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import src.artistGenresAwardsTracks.utils.Parser;

/**
 * In order to complete this test, one sample object needs to be extracted 
 * from RDF dump, parsed and the resulting parsed file needs to contain 
 * at least one line with the necessary predicate. These four predicates are:
 * <br />
 * 	music.artist.track <br />
 * 	award.award_winner.awards_won <br />
 * 	music.artist.genre <br />
 * 	type.object.name <br />
 * <br />
 * One of the easiest way to extract one sample object from GZIP compressed RDF dump
 * is to run command-line utility ZGREP. <br />
 * <br />
 * 
 * For object ID 'm.01vw20h' the command would be: <br />
 *  "zgrep '^<http://rdf\.freebase\.com/ns/m\.01vw20h>.*$' freebase-rdf-2014-10-12-00-00.gz > sample_RDF_object_01vw20h.txt"
 * 
 * <br /><br />
 * Extracting one object, without need to read through long RDF dump,
 * is obviously not possible. One way is to visit this URL: http://rdf.freebase.com/m/02l840,
 * but the output is in turtle serialized form (which isn't the same as long RDF dump).
 * 
 * @author stefanlinner
 *
 */
public class PreprocessorTest {
	String awardedArtistID = "01vw20h";
	
	String sampleDumpRDFPath = 
			"./data/artists_genres_awards_tracks/test_files/sample_RDF_object_"+ awardedArtistID +".txt";
	String testsampleOutputFilePath = 
			"./data/artists_genres_awards_tracks/test_files/sample_object_"+ awardedArtistID +"_parsed.txt";
	
	String pattern1 = "^(?<subject>[^\\t]+)\\t(?<predicate>[^\\t]+)\\t(?<object>[^\\t]+)$";
	String pattern2 = "^.+\\.(?<type>[\\w]+)$";

	public PreprocessorTest() {
		Parser.parseDump(sampleDumpRDFPath, testsampleOutputFilePath);
	}
	
	/**
	 * Test method for {@link src.artistGenresAwardsTracks.Preprocessor#main(java.lang.String[])}.
	 */
	@Test
	public void awardsWonTest() {
		boolean passed = false;
		try {
			
			InputStream fileStream = new FileInputStream(testsampleOutputFilePath);
			Reader decoder = new InputStreamReader(fileStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(decoder);
			String readLine, subject = ".", object = ".", predicate = ".", type = ".";
			Pattern pat1 = Pattern.compile(pattern1);
			Pattern pat2 = Pattern.compile(pattern2);
			Matcher match1, match2;
			
			while ( (readLine = bufferedReader.readLine()) != null ){
				match1 = pat1.matcher( readLine );
				if (!match1.matches()) fail("Pattern1 not matched!");
				
				subject = match1.group("subject");
				predicate = match1.group("predicate");
				object = match1.group("object");
				
				match2 = pat2.matcher( predicate );
				if (!match2.matches()) fail("Pattern2 not matched!");
				
				type = match2.group("type");
				
				if (subject.equals(awardedArtistID) && predicate.equals("award.award_winner.awards_won")){
					
					assertTrue("Predicate 'award.award_winner.awards_won' is correctly parsed", true);
					
					passed = true;
					break;
				}
			}
			
			bufferedReader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (!passed){
			fail("Predicate 'award.award_winner.awards_won' isn't correctly parsed!");	
		}
		
	}
	
	/**
	 * Test method for {@link src.artistGenresAwardsTracks.Preprocessor#main(java.lang.String[])}.
	 */
	@Test
	public void TrackTest() {
		boolean passed = false;
		try {
			
			InputStream fileStream = new FileInputStream(testsampleOutputFilePath);
			Reader decoder = new InputStreamReader(fileStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(decoder);
			String readLine, subject = ".", object = ".", predicate = ".", type = ".";
			Pattern pat1 = Pattern.compile(pattern1);
			Pattern pat2 = Pattern.compile(pattern2);
			Matcher match1, match2;
			
			while ( (readLine = bufferedReader.readLine()) != null ){
				match1 = pat1.matcher( readLine );
				if (!match1.matches()) fail("Pattern1 not matched!");
				
				subject = match1.group("subject");
				predicate = match1.group("predicate");
				object = match1.group("object");
				
				match2 = pat2.matcher( predicate );
				if (!match2.matches()) fail("Pattern2 not matched!");
				
				type = match2.group("type");
				
				if (subject.equals(awardedArtistID) && predicate.equals("music.artist.track")){
					
					assertTrue("Predicate 'music.artist.track' is correctly parsed", true);
					
					passed = true;
					break;
				}
			}
			
			bufferedReader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (!passed){
			fail("Predicate 'music.artist.track' isn't correctly parsed!");	
		}
		
	}
	
	/**
	 * Test method for {@link src.artistGenresAwardsTracks.Preprocessor#main(java.lang.String[])}.
	 */
	@Test
	public void GenreTest() {
		boolean passed = false;
		try {
			
			InputStream fileStream = new FileInputStream(testsampleOutputFilePath);
			Reader decoder = new InputStreamReader(fileStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(decoder);
			String readLine, subject = ".", object = ".", predicate = ".", type = ".";
			Pattern pat1 = Pattern.compile(pattern1);
			Pattern pat2 = Pattern.compile(pattern2);
			Matcher match1, match2;
			
			while ( (readLine = bufferedReader.readLine()) != null ){
				match1 = pat1.matcher( readLine );
				if (!match1.matches()) fail("Pattern1 not matched!");
				
				subject = match1.group("subject");
				predicate = match1.group("predicate");
				object = match1.group("object");
				
				match2 = pat2.matcher( predicate );
				if (!match2.matches()) fail("Pattern2 not matched!");
				
				type = match2.group("type");
				
				if (subject.equals(awardedArtistID) && predicate.equals("music.artist.genre")){
					
					assertTrue("Predicate 'music.artist.genre' is correctly parsed", true);
					
					passed = true;
					break;
				}
			}
			
			bufferedReader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (!passed){
			fail("Predicate 'music.artist.genre' isn't correctly parsed!");	
		}
		
	}
	
	/**
	 * Test method for {@link src.artistGenresAwardsTracks.Preprocessor#main(java.lang.String[])}.
	 */
	@Test
	public void NameTest() {
		boolean passed = false;
		try {
			
			InputStream fileStream = new FileInputStream(testsampleOutputFilePath);
			Reader decoder = new InputStreamReader(fileStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(decoder);
			String readLine, subject = ".", object = ".", predicate = ".", type = ".";
			Pattern pat1 = Pattern.compile(pattern1);
			Pattern pat2 = Pattern.compile(pattern2);
			Matcher match1, match2;
			
			while ( (readLine = bufferedReader.readLine()) != null ){
				match1 = pat1.matcher( readLine );
				if (!match1.matches()) fail("Pattern1 not matched!");
				
				subject = match1.group("subject");
				predicate = match1.group("predicate");
				object = match1.group("object");
				
				match2 = pat2.matcher( predicate );
				if (!match2.matches()) fail("Pattern2 not matched!");
				
				type = match2.group("type");
				
				if (subject.equals(awardedArtistID) && predicate.equals("type.object.name")){
					
					assertTrue("Predicate 'type.object.name' is correctly parsed", true);
					
					passed = true;
					break;
				}
			}
			
			bufferedReader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (!passed){
			fail("Predicate 'type.object.name' isn't correctly parsed!");	
		}
		
	}

}
