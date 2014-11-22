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

public class IndexEngine {

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
	
	public static void indexGenres(){
		String indexDirPath = "./data/artists_genres_awards_tracks/index-genres/";
		String inputFile = "./data/artists_genres_awards_tracks/parsed_files/artist_ID-genre_ID.txt";
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
	
	public static void main(String[] args) {
		/*indexNames();
		indexAwards();
		indexGenres();*/
		indexTracks();
		
		//needs to be run after SearchEngine.extractGenreNames();
		//indexGenreNames();
	}

}
