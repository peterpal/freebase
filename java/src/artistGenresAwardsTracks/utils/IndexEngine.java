package src.artistGenresAwardsTracks.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * This class contains methods for indexing parsed and split files 
 * (see {@link src.artistGenresAwardsTracks.utils.Parser Parser} and {@link src.artistGenresAwardsTracks.utils.Splitter Splitter}).
 * Used index engine is Apache Lucene, version 4.10.2 (http://lucene.apache.org).
 * 
 * @author stefanlinner
 *
 */
public class IndexEngine {

	/**
	 * Creates index from file "object_ID-name.txt" in directory "index-names". 
	 * Every line in this file is modeled as one Apache Lucene document, which is added to the index.
	 * One document contains two fields: <br />
	 * <br />
	 * object_ID - indexed, not analyzed, not stored <br />
	 * name - not indexed, not analyzed, stored <br />
	 * <br />
	 * This way, it's possible to search for specific object_ID and get its assigned name.
	 */
	public static void indexNames(){
		String indexDirPath = "./data/artists_genres_awards_tracks/index-names/";
		String inputFile = "./data/artists_genres_awards_tracks/parsed_files/object_ID-name.txt";
		String pattern = "^(?<objectID>[^\\t]+)\\t(?<name>[^\\t]+)$";
		
		System.out.println("Creating index NAMES...");
		try {
			Date start = new Date();
			
			Analyzer analyzer = new StandardAnalyzer(); 
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);
			iwc.setOpenMode(OpenMode.CREATE);
			Directory dir = FSDirectory.open(new File(indexDirPath));
			IndexWriter writer = new IndexWriter(dir, iwc);

        	FileInputStream fileStream = new FileInputStream(inputFile);
        	Reader decoder = new InputStreamReader(fileStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(decoder);
          
			Pattern pat = Pattern.compile(pattern);
			Matcher match;
			
			Document doc;
			String readLine;
			int lineCounter = 0, milionCounter = 0;
			while ( (readLine = bufferedReader.readLine()) != null ){
				lineCounter++;
				
				match = pat.matcher( readLine );
				if (!match.matches()) throw new Exception("Pattern not matched!");
				
				doc = new Document();		
				doc.add(new StringField("object_ID", match.group("objectID"), Field.Store.NO));
				doc.add(new StoredField("name", match.group("name")));
				writer.addDocument(doc);
				
				if (lineCounter == 1000000){	// 1.000.000
					milionCounter++;
					lineCounter = 0;
					System.out.println(String.format("%d milion(s) records processed...", milionCounter));
				}
			}
			
			bufferedReader.close();
			writer.close();
	        
	        Date end = new Date();
	        System.out.println("Index NAMES created. " + (end.getTime() - start.getTime()) + " total milliseconds\n\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Creates index from file "awarded_artist_ID.txt" in directory "index-awards". 
	 * This file is modeled as one Apache Lucene document, which is added to the index.
	 * Document contains only one field: <br />
	 * <br />
	 * content - indexed, analyzed, not stored <br />
	 * <br />
	 * This way, it's possible to quickly test (by searching) if specific artist_ID is awarded artist.
	 */
	public static void indexAwards(){
		String indexDirPath = "./data/artists_genres_awards_tracks/index-awards/";
		String inputFile = "./data/artists_genres_awards_tracks/parsed_files/awarded_artist_ID.txt";
		
		System.out.println("Creating index AWARDS...");
		try {	
			Date start = new Date();
			
			Analyzer analyzer = new StandardAnalyzer(); 
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);
			iwc.setOpenMode(OpenMode.CREATE);
			Directory dir = FSDirectory.open(new File(indexDirPath));
			IndexWriter writer = new IndexWriter(dir, iwc);
        	
        	FileInputStream fileStream = new FileInputStream(inputFile);
        	Reader decoder = new InputStreamReader(fileStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(decoder);
			
			Document doc = new Document();	
			doc.add(new org.apache.lucene.document.TextField("content", bufferedReader));
			writer.addDocument(doc);
			
			bufferedReader.close();
			writer.close();
	        
	        Date end = new Date();
	        System.out.println("Index AWARDS created. " + (end.getTime() - start.getTime()) + " total milliseconds\n\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates index from file "artist_ID-genre_ID.txt" in directory "index-genres". 
	 * Every line in this file is modeled as one Apache Lucene document, which is added to the index.
	 * One document contains two fields: <br />
	 * <br />
	 * artist_ID - indexed, not analyzed, stored <br />
	 * genre_ID - indexed, not analyzed, stored <br />
	 * <br />
	 * This way, it's possible to search for specific artist_ID and get its assigned genre_ID and vice versa.
	 */
	public static void indexGenres(){
		String indexDirPath = "./data/artists_genres_awards_tracks/index-genres/";
		String inputFile = "./data/artists_genres_awards_tracks/parsed_files/.txt";
		String pattern = "^(?<artistID>[^\\t]+)\\t(?<genreID>[^\\t]+)$";
		
		System.out.println("Creating index GENRES...");
		try {
			Date start = new Date();
			
			Analyzer analyzer = new StandardAnalyzer(); 
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);
			iwc.setOpenMode(OpenMode.CREATE);
			Directory dir = FSDirectory.open(new File(indexDirPath));
			IndexWriter writer = new IndexWriter(dir, iwc);

        	FileInputStream fileStream = new FileInputStream(inputFile);
        	Reader decoder = new InputStreamReader(fileStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(decoder);
          
			Pattern pat = Pattern.compile(pattern);
			Matcher match;
			
			Document doc;
			String readLine;
			int lineCounter = 0, milionCounter = 0;
			while ( (readLine = bufferedReader.readLine()) != null ){
				lineCounter++;
				
				match = pat.matcher( readLine );
				if (!match.matches()) throw new Exception("Pattern not matched!");
				
				doc = new Document();		
				doc.add(new StringField("artist_ID", match.group("artistID"), Field.Store.YES));
				doc.add(new StringField("genre_ID", match.group("genreID"), Field.Store.YES));
				writer.addDocument(doc);
				
				if (lineCounter == 1000000){	// 1.000.000
					milionCounter++;
					lineCounter = 0;
					System.out.println(String.format("%d milion(s) records processed...", milionCounter));
				}
			}
			
			bufferedReader.close();
			writer.close();
	        
	        Date end = new Date();
	        System.out.println("Index GENRES created. " + (end.getTime() - start.getTime()) + " total milliseconds\n\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates index from file "artist_ID-track_ID.txt" in directory "index-tracks". 
	 * Every line in this file is modeled as one Apache Lucene document, which is added to the index.
	 * One document contains two fields: <br />
	 * <br />
	 * artist_ID - indexed, not analyzed, not stored <br />
	 * track_ID - indexed, not analyzed, stored <br />
	 * <br />
	 * This way, it's possible to search for specific artist_ID and get its assigned track_ID.
	 */
	public static void indexTracks(){
		String indexDirPath = "./data/artists_genres_awards_tracks/index-tracks/";
		String inputFile = "./data/artists_genres_awards_tracks/parsed_files/artist_ID-track_ID.txt";
		String pattern = "^(?<artistID>[^\\t]+)\\t(?<trackID>[^\\t]+)$";
		
		System.out.println("Creating index TRACKS...");
		try {
			Date start = new Date();
			
			Analyzer analyzer = new StandardAnalyzer(); 
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);
			iwc.setOpenMode(OpenMode.CREATE);
			Directory dir = FSDirectory.open(new File(indexDirPath));
			IndexWriter writer = new IndexWriter(dir, iwc);

        	FileInputStream fileStream = new FileInputStream(inputFile);
        	Reader decoder = new InputStreamReader(fileStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(decoder);
          
			Pattern pat = Pattern.compile(pattern);
			Matcher match;
			
			Document doc;
			String readLine;
			int lineCounter = 0, milionCounter = 0;
			while ( (readLine = bufferedReader.readLine()) != null ){
				lineCounter++;
				
				match = pat.matcher( readLine );
				if (!match.matches()) throw new Exception("Pattern not matched!");
				
				doc = new Document();		
				doc.add(new StringField("artist_ID", match.group("artistID"), Field.Store.NO));
				doc.add(new StringField("track_ID", match.group("trackID"), Field.Store.YES));
				writer.addDocument(doc);
				
				if (lineCounter == 1000000){	// 1.000.000
					milionCounter++;
					lineCounter = 0;
					System.out.println(String.format("%d milion(s) records processed...", milionCounter));
				}
			}
			
			bufferedReader.close();
			writer.close();
	        
	        Date end = new Date();
	        System.out.println("Index TRACKS created. " + (end.getTime() - start.getTime()) + " total milliseconds\n\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates index from file "genre_ID-genre_name.txt" in directory "index-genre-names". 
	 * Every line in this file is modeled as one Apache Lucene document, which is added to the index.
	 * One document contains two fields: <br />
	 * <br />
	 * genre_ID - indexed, not analyzed, stored <br />
	 * genre_name - indexed, analyzed, stored <br />
	 * <br />
	 * This way, it's possible to search for genre names and get their assigned genre_IDs.
	 * StandardAnalyzer is used for tokenization.<br />
	 * <br />
	 * This method needs to be called after method {@link src.artistGenresAwardsTracks.utils.SearchEngine#extractGenreNames() SearchEngine.extractGenreNames()}
	 */
	public static void indexGenreNames(){
		String indexDirPath = "./data/artists_genres_awards_tracks/index-genre-names/";
		String inputFile = "./data/artists_genres_awards_tracks/parsed_files/genre_ID-genre_name.txt";
		String pattern = "^(?<genreID>[^\\t]+)\\t(?<genreName>[^\\t]+)$";
		
		System.out.println("Creating index GENRE-NAMES...");
		try {
			Date start = new Date();
			
			Analyzer analyzer = new StandardAnalyzer(); 
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);
			iwc.setOpenMode(OpenMode.CREATE);
			Directory dir = FSDirectory.open(new File(indexDirPath));
			IndexWriter writer = new IndexWriter(dir, iwc);

        	FileInputStream fileStream = new FileInputStream(inputFile);
        	Reader decoder = new InputStreamReader(fileStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(decoder);
          
			Pattern pat = Pattern.compile(pattern);
			Matcher match;
			
			Document doc;
			String readLine;
			while ( (readLine = bufferedReader.readLine()) != null ){
				
				match = pat.matcher( readLine );
				if (!match.matches()) throw new Exception("Pattern not matched!");
				
				doc = new Document();		
				doc.add(new StringField("genre_ID", match.group("genreID"), Field.Store.YES));
				doc.add(new TextField("genre_name", match.group("genreName"), Field.Store.YES));
				writer.addDocument(doc);
			}
			
			bufferedReader.close();
			writer.close();
	        
	        Date end = new Date();
	        System.out.println("Index GENRE-NAMES created. " + (end.getTime() - start.getTime()) + " total milliseconds\n\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Main method for calling individual index methods, mainly for debugging reasons.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		/*indexNames();
		indexAwards();
		indexGenres();*/
		indexTracks();
		
		//needs to be run after SearchEngine.extractGenreNames();
		//indexGenreNames();
	}

}
