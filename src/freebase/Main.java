/**
 * 
 */
package freebase;

/**
 * @author stefanlinner
 *
 */

import java.io.*;
import java.util.regex.*;
import java.util.zip.GZIPInputStream;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		InputStream fileStream;
		try {
			
			String freebaseDumpRDF = new String("/Volumes/SSD_SAMSUNG_840/freebase-rdf-2014-10-12-00-00.gz");
			
			fileStream = new FileInputStream(freebaseDumpRDF);
			//fileStream = new FileInputStream("./data/sample_artists_awards_tracks_data.txt");
			InputStream gzipStream = new GZIPInputStream(fileStream);
			Reader decoder = new InputStreamReader(gzipStream, "UTF-8");
			BufferedReader bufferedGzipReader = new BufferedReader(decoder);
			
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
			FileWriter outputFileWriter = new FileWriter ("./data/linner-stefan/output.txt", false);	//don't append

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
					System.out.println(String.format("%d milion(s) of 1.9 bilion records processed...", milionCounter));
				}
			}
			
			outputFileWriter.close();	//...and flush to stream
			bufferedGzipReader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
