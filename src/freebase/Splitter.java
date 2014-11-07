/**
 * 
 */
package freebase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author stefanlinner
 *
 */
public class Splitter {

	public static void splitParsedDump(String inputFilePath, String outputDirPath) {
		InputStream fileStream;
		try {
			fileStream = new FileInputStream(inputFilePath);
			Reader decoder = new InputStreamReader(fileStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(decoder);
			
			String pattern1 = new String("^(?<subject>[^\\t]+)\\t(?<predicate>[^\\t]+)\\t(?<object>[^\\t]+)$");
			String pattern2 = new String("^.+\\.(?<type>[\\w]+)$");	//Original: ^.+\.([\w]+)$

			
			Pattern pat1 = Pattern.compile(pattern1);
			Pattern pat2 = Pattern.compile(pattern2);
			
			int lineCounter = 0, milionCounter = 0;
			String readLine, subject = ".", object = ".", predicate = ".", type = ".";
			Matcher match1, match2;
			FileWriter outputFileWriter_NAMES = new FileWriter(outputDirPath + "object_ID-name.txt", false);	//don't append
			FileWriter outputFileWriter_GENRES = new FileWriter(outputDirPath + "artist_ID-genre_ID.txt", false);	//don't append
			FileWriter outputFileWriter_AWARDS = new FileWriter(outputDirPath + "awarded_artist_ID.txt", false);	//don't append
			FileWriter outputFileWriter_TRACKS = new FileWriter(outputDirPath + "artist_ID-track_ID.txt", false);	//don't append
			
			while ( (readLine = bufferedReader.readLine()) != null ){
				lineCounter++;
				
				match1 = pat1.matcher( readLine );
				if (!match1.matches()) throw new Exception("Pattern1 not matched!");
				
				subject = match1.group("subject");
				predicate = match1.group("predicate");
				object = match1.group("object");
				
				match2 = pat2.matcher( predicate );
				if (!match2.matches()) throw new Exception("Pattern2 not matched!");
				
				type = match2.group("type");
				
				if (type.equals("name")){
					outputFileWriter_NAMES.write(subject + "\t" + object + "\n");
				}
				else if (type.equals("genre")){
					outputFileWriter_GENRES.write(subject + "\t" + object + "\n");
				}
				else if (type.equals("awards_won")){
					outputFileWriter_AWARDS.write(subject + "\n");
				}
				else if (type.equals("track")){
					outputFileWriter_TRACKS.write(subject + "\t" + object + "\n");
				}
				else {
					throw new Exception("Matched unknown type!");
				}
				
				if (lineCounter == 1000000){	// 1.000.000
					milionCounter++;
					lineCounter = 0;
					System.out.println(String.format("%d milion(s) records processed...", milionCounter));
				}
			}
			
			outputFileWriter_NAMES.close();	//...and flush to stream
			outputFileWriter_GENRES.close();	//...and flush to stream
			outputFileWriter_AWARDS.close();	//...and flush to stream
			outputFileWriter_TRACKS.close();	//...and flush to stream
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String parsedDumpFilePath = new String ("./data/linner-stefan/parsed_dump.txt");
		
		//output directory path must be with trailing slash
		String splitDir = new String("./data/linner-stefan/");
		splitParsedDump(parsedDumpFilePath, splitDir);
	}

}
