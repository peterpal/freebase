/**
 * 
 */
package src.artistGenresAwardsTracks.utils;



import java.io.*;
import java.util.regex.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

/**
 * This class provides only one method for parsing original Freebase data dump (see https://www.freebase.com/). 
 * Only necessary tripplets are extracted, in the lighter form and saved to output file.
 * 
 * @author stefanlinner
 *
 */
public class Parser {
	
	/**
	 * Freebase data dump in RDF format is parsed into one output file using regular expressions. Dump is
	 * searched for triples containing only these four predicates: <br />
	 * <br />
	 * 	music.artist.track <br />
	 * 	award.award_winner.awards_won <br />
	 * 	music.artist.genre <br />
	 * 	type.object.name <br />
	 * <br />
	 * Unnecessary URLs and other redundant characters are stripped. 
	 * Output file is wiped before storing any information.
	 * Method outputs also information about every one million of records processed.
	 * One line of output file is in the following form: "subject	predicate	object". These fields are tab-separated.
	 * 
	 * @param freebaseDumpRDFPath path to g-zipped Freebase dump file in RDF format
	 * @param outputFilePath arbitrary text file path
	 */
	public static void parseDump(String freebaseDumpRDFPath, String outputFilePath) {
		InputStream fileStream, gzipStream;
		BufferedReader bufferedGzipReader;
		FileWriter outputFileWriter;
		
		try {
			fileStream = new FileInputStream(freebaseDumpRDFPath);
			
			try {
				gzipStream = new GZIPInputStream(fileStream);
			}
			catch (ZipException e){
				// if freebaseDumpRDFPath isn't gzipped, use gzipStream as fileStream
				gzipStream = fileStream;
			}
			
			Reader decoder = new InputStreamReader(gzipStream, "UTF-8");
			bufferedGzipReader = new BufferedReader(decoder);
			
			String namespacesForID = new String("(?<predicate>"
					+ "(music\\.artist\\.track)"
					+ "|(award\\.award_winner\\.awards_won)"
					+ "|(music\\.artist\\.genre)"
					+ ")");
			String namespacesForName = new String("(?<predicate>type\\.object\\.name)");
			
			//Original regex: ^<http://rdf\.freebase\.com/ns/m\.(?<subject>\w+)>\s+<http://rdf\.freebase\.com/ns/(?<predicate>(music\.artist\.track)|(award\.award_winner\.awards_won)|(music\.artist\.genre))>\s+<http://rdf\.freebase\.com/ns/m\.(?<object>\w+)>\s+\.\s*$
			String patternForID = new String("^<http://rdf\\.freebase\\.com/ns/m\\.(?<subject>\\w+)>\\s+<http://rdf\\.freebase\\.com/ns/"
					+ namespacesForID + ">\\s+<http://rdf\\.freebase\\.com/ns/m\\.(?<object>\\w+)>\\s+\\.\\s*$");
			
			//Original regex: ^<http://rdf\.freebase\.com/ns/m\.(?<subject>\w+)>\s+<http://rdf\.freebase\.com/ns/(?<predicate>type\.object\.name)>\s+"(?<object>.+)"@en\s+\.\s*$
			String patternForName = new String("^<http://rdf\\.freebase\\.com/ns/m\\.(?<subject>\\w+)>\\s+<http://rdf\\.freebase\\.com/ns/"
					+ namespacesForName + ">\\s+\"(?<object>.+)\"@en\\s+\\.\\s*$");
			
			Pattern pat1 = Pattern.compile(patternForID);
			Pattern pat2 = Pattern.compile(patternForName);
			
			int lineCounter = 0, milionCounter = 0;
			String readLine, subject = ".", object = ".", predicate = ".";
			Matcher match1, match2;
			outputFileWriter = new FileWriter (outputFilePath, false);	//don't append

			while ( (readLine = bufferedGzipReader.readLine()) != null ){
				lineCounter++;
				
				match1 = pat1.matcher( readLine );
				if (match1.matches()){
					subject = match1.group("subject");
					predicate = match1.group("predicate");
					object = match1.group("object");
					
					outputFileWriter.write(subject +"\t"+ predicate +"\t" + object + "\n");
				}
				else {
					match2 = pat2.matcher( readLine );
					if (match2.matches()){
						subject = match2.group("subject");
						predicate = match2.group("predicate");
						object = match2.group("object");
						
						outputFileWriter.write(subject +"\t"+ predicate +"\t" + object + "\n");
					}
				}

				if (subject == null || subject.isEmpty()
						|| predicate == null || predicate.isEmpty()
						|| object == null || object.isEmpty()){
					throw new Exception("Matched null or empty string");
				}
				
				if (lineCounter == 1000000){	// 1.000.000
					milionCounter++;
					lineCounter = 0;
					System.out.println(String.format("%d milion(s) of ~2.8 bilion records processed...", milionCounter));
				}
			}
			
			outputFileWriter.close();	//...and flush to stream
			bufferedGzipReader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Main method, which contains input and output file paths and then calls one and only 
	 * method parseDump(...) with these paths as arguments.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		String freebaseDumpRDFPath = new String("/Volumes/SSD_SAMSUNG_840/freebase-rdf-2014-10-12-00-00.gz");
		String parsedDumpFilePath = new String ("./data/artists_genres_awards_tracks/parsed_files/parsed_dump.txt");
		
		parseDump(freebaseDumpRDFPath, parsedDumpFilePath);
	}

}
