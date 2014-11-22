package src.artistGenresAwardsTracks.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * @author stefanlinner
 *
 */
public class SearchEngine {

	public static void extractGenreNames(){
		String inputFilePath = "./data/artists_genres_awards_tracks/parsed_files/artist_ID-genre_ID.txt";
		String outputFilePath = "./data/artists_genres_awards_tracks/parsed_files/genre_ID-genre_name.txt";
		String indexDirPath = "./data/artists_genres_awards_tracks/index-names/";
		InputStream fileStream;
		
		try {
			fileStream = new FileInputStream(inputFilePath);
			Reader decoder = new InputStreamReader(fileStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(decoder);
			
			IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexDirPath)));
		    IndexSearcher searcher = new IndexSearcher(reader);
		    Analyzer analyzer = new StandardAnalyzer(); 
		    QueryParser parser = new QueryParser("object_ID", analyzer);
		    Query query;
		    TopDocs results;
		    ScoreDoc[] hits;
		    Document doc;
			
			String pattern = "^(?<artistID>[^\\t]+)\\t(?<genreID>[^\\t]+)$";
			Pattern pat = Pattern.compile(pattern);
			Matcher match;
			
			int lineCounter = 0, tenThousand = 0, questionable = 0;
			String readLine, genre_ID = ".", last_genre_ID = ".", genre_name;	
			FileWriter outputFileWriter = new FileWriter(outputFilePath, false);	//don't append
			
			Set<String> uniqueGenres = new HashSet<String>();
			
			while ( (readLine = bufferedReader.readLine()) != null ){
				lineCounter++;
				
				match = pat.matcher( readLine );
				if (!match.matches()) throw new Exception("Pattern not matched!");
				
				genre_ID = match.group("genreID");
				
				//check if set contains genre_ID
				if ( uniqueGenres.add(genre_ID) ){
					
					 query = parser.parse(genre_ID);
					 results = searcher.search(query, 2);
					 hits = results.scoreDocs;
					 
					 if (results.totalHits == 0) {
						 //hits can be zero
						 questionable++;
						 continue;
					 }
					 if (results.totalHits > 1) questionable++;
					 
					 doc = searcher.doc(hits[0].doc);
					 genre_name = doc.get("name");
					 
					 outputFileWriter.write(genre_ID + "\t" + genre_name + "\n");
				}
				
				if (lineCounter == 10000){	// 10.000
					tenThousand++;
					lineCounter = 0;				
					System.out.println(String.format("%d records processed... %d is questionable", tenThousand*10000, questionable));
					questionable = 0;
				}
			}
			
			reader.close();
			outputFileWriter.close();	//...and flush to stream
			bufferedReader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Set<String> searchString(String searchQuery, String documentSearchField, String indexDirPath, int resultsSize, String documentReturnField, boolean analyzed) 
			throws IOException, ParseException{

			IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexDirPath)));
		    IndexSearcher searcher = new IndexSearcher(reader);
		    
		    Analyzer analyzer;
		    if (analyzed){
		    	analyzer = new StandardAnalyzer();
		    }
		    else {
		    	analyzer = new WhitespaceAnalyzer();
		    } 
		    
		    
			QueryParser parser = new QueryParser(documentSearchField, analyzer);
		    Query query = parser.parse(searchQuery);
		    //System.out.println("Searching for: " + query.toString(documentSearchField));
		    
		    TopDocs results = searcher.search(query, resultsSize);
		    //System.out.println("Results found: " + String.valueOf(results.totalHits));

		    Set<String> uniqueResults = new HashSet<String>();
			
		    if (documentReturnField != null){
		    	for (int i = 0; i < results.scoreDocs.length; i++){
					uniqueResults.add(  searcher.doc(results.scoreDocs[i].doc).get(documentReturnField)  );
				}
		    }
		    else if (results.scoreDocs.length > 0){
		    	uniqueResults.add("Document found");
		    }
		    
		    reader.close();
		    
		    return uniqueResults;
	}
	
	/**
	 * Returns vector of strings. First element in vector is the value of searchQuery. Next elements are search results.
	 * 
	 * @param searchQuery
	 * @param documentSearchField
	 * @param indexDirPath
	 * @param resultsSize
	 * @param documentReturnField
	 * @param analyzed
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public static Vector<String> searchStringToVector(String searchQuery, String documentSearchField, String indexDirPath, int resultsSize, String documentReturnField, boolean analyzed) 
			throws IOException, ParseException{

			IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexDirPath)));
		    IndexSearcher searcher = new IndexSearcher(reader);
		    
		    Analyzer analyzer;
		    if (analyzed){
		    	analyzer = new StandardAnalyzer();
		    }
		    else {
		    	analyzer = new WhitespaceAnalyzer();
		    } 
		    
			QueryParser parser = new QueryParser(documentSearchField, analyzer);
		    Query query = parser.parse(searchQuery);
		    //System.out.println("Searching for: " + query.toString(documentSearchField));
		    
		    TopDocs results = searcher.search(query, resultsSize);
		    //System.out.println("Results found: " + String.valueOf(results.totalHits));

		    Vector<String> resultsVector = new Vector<String>();
		    resultsVector.add(searchQuery);
		    
		    if (documentReturnField != null){
		    	for (int i = 0; i < results.scoreDocs.length; i++){
		    		resultsVector.add(  searcher.doc(results.scoreDocs[i].doc).get(documentReturnField)  );
				}
		    }
		    
		    reader.close();
		    
		    return resultsVector;
	}
	
	public static Set<String> searchSet(Set<String> searchSet, String documentSearchField, String indexDirPath, int resultsSize, String documentReturnField, boolean analyzed) 
			throws IOException, ParseException{

			IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexDirPath)));
		    IndexSearcher searcher = new IndexSearcher(reader);
		    
		    Analyzer analyzer;
		    if (analyzed){
		    	analyzer = new StandardAnalyzer();
		    }
		    else {
		    	analyzer = new WhitespaceAnalyzer();
		    } 
		    
		    String searchString = new String();
		    Iterator<String> iter = searchSet.iterator();
		    while (iter.hasNext()){
		    	searchString = searchString + " " + iter.next();
		    }
		    
			QueryParser parser = new QueryParser(documentSearchField, analyzer);
		    Query query = parser.parse(searchString);
		    //System.out.println("Searching for: " + query.toString(documentSearchField));
		    
		    TopDocs results = searcher.search(query, resultsSize);
		    //System.out.println("Results found: " + String.valueOf(results.totalHits));

		    Set<String> uniqueResults = new HashSet<String>();
		    if (documentReturnField != null){
		    	for (int i = 0; i < results.scoreDocs.length; i++){
					uniqueResults.add(  searcher.doc(results.scoreDocs[i].doc).get(documentReturnField)  );
				}
		    }
		    else if (results.scoreDocs.length > 0){
		    	uniqueResults.add("Document found");
		    }

		    reader.close();
		    
		    return uniqueResults;
	}

}
