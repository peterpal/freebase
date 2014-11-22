/**
 * 
 */
package src.artistGenresAwardsTracks;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import src.artistGenresAwardsTracks.utils.SearchEngine;

/**
 * Main class for the provided service. It takes only one input argument, containing desired search string (e.g. "jazz reggae*")
 * and creates folder "results" with files, containing found track names. Files are grouped based on track artist.
 * 
 * @author stefanlinner
 *
 */
public class Service {
	//size (total number of records/documents) in index
	public static final int GENRE_NAMES_SIZE = 1522;	
	public static final int GENRES_SIZE = 219181;
	public static final int AWARDS_SIZE = 1;
	
	//limit
	public static final int TRACKS_LIMIT = 100000;
	public static final int NAMES_LIMIT = 1;
	
	public static void printUsage(){
		System.out.println("Usage:");
		System.out.println("\tProgram takes only one input argument, containing desired search string (e.g. \"jazz reggae*\")");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String searchString = null;
		String outputDirPath = "./data/artists_genres_awards_tracks/results/";
		
		if (args.length < 1) {
			printUsage();
			System.exit(1);
		}
		
		searchString = args[0];
		
		Set<String> uniqueResults;
		Iterator<String> iter;
		Iterator<Vector<String>> iterArtist;
		Vector<String> iterVecString;
		Date start = new Date(), end;
		
		//clean or create output directory
		try {
			File outputDir = new File(outputDirPath);
			if (!outputDir.exists()){
				outputDir.mkdir();
			}
			else {
				FileUtils.cleanDirectory(outputDir);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		try {
			//find genre IDs, that are matching input genreName query
			System.out.println("Searching for '"+ searchString +"'...");
			uniqueResults = SearchEngine.searchString(searchString, "genre_name", "./data/artists_genres_awards_tracks/index-genre-names", GENRE_NAMES_SIZE, "genre_ID", true);
			
			if (uniqueResults.isEmpty()){
				System.out.println("0 matching genre names.");
				return;
			}
			System.out.println(uniqueResults.size() + " genres found.");
		
			//find artist IDs, which have assigned previously found genre IDs
			uniqueResults = SearchEngine.searchSet(uniqueResults, "genre_ID", "./data/artists_genres_awards_tracks/index-genres", GENRES_SIZE, "artist_ID", false);
			if (uniqueResults.isEmpty()){
				System.out.println("0 matching artist IDs.");
				return;
			}
			System.out.println(uniqueResults.size() + " artists found.");
		
			//filter-out not awarded artists
			Set<String> awardedArtists = new HashSet<String>(uniqueResults);
			iter = uniqueResults.iterator();
			Set<String> result;
			while (iter.hasNext()){
				searchString = iter.next();
				result = SearchEngine.searchString(searchString, "content", "./data/artists_genres_awards_tracks/index-awards", AWARDS_SIZE, null, false);
				
				if (result.isEmpty()){
					if (!awardedArtists.remove(searchString)){
						throw new Exception("Unable to remove not awarded artist!");
					}
				}
			}
			if (awardedArtists.isEmpty()){
				System.out.println("0 matching awarded artist IDs.");
				return;
			}
			System.out.println(awardedArtists.size() + " artists are awarded:\n");
			
			//Output awarded artists names
			iter = awardedArtists.iterator();
			while (iter.hasNext()){
				searchString = iter.next();
				result = SearchEngine.searchString(searchString, "object_ID", "./data/artists_genres_awards_tracks/index-names", NAMES_LIMIT, "name", false);

				if (result.size() < 1){	//object name not found!
					//System.err.println("Artist name '" + searchString + "' not found!");
					System.out.print("\t" + searchString + "\n");
				}
				else {
					System.out.print("\t" + result.iterator().next() + "\n");
				}
			}
			System.out.println();
			
			//find track IDs
			Vector<Vector<String>> artists_tracks = new Vector<Vector<String>>();
			Vector<String> trackIDs;
			iter = awardedArtists.iterator();
			while (iter.hasNext()){
				searchString = iter.next();
				//artist ID is added as first element
				trackIDs = SearchEngine.searchStringToVector(searchString, "artist_ID", "./data/artists_genres_awards_tracks/index-tracks", TRACKS_LIMIT, "track_ID", false);
				
				if (trackIDs.size() < 2){
					System.err.println("No tracks found for artist ID \"" + searchString + "\"!" );
				}
				else if (trackIDs.size() > TRACKS_LIMIT + 1){
					System.err.println("Tracks limit '"+ TRACKS_LIMIT +"' reached for artist ID \"" + searchString + "\"!" );
				}
				else {
					artists_tracks.add( trackIDs );
				}
			}	
			
			//replace IDs in artists_tracks with names ant output found tracks
			System.out.println("Processing track names...");
			
			iterArtist = artists_tracks.iterator();
			String tempString;
			FileWriter outputFile = null;
			long totalTracks = 0;
			while (iterArtist.hasNext()){
				iterVecString = iterArtist.next();
				for (int i = 0; i < iterVecString.size(); i++){
					searchString = iterVecString.get(i);
					result = SearchEngine.searchString(searchString, "object_ID", "./data/artists_genres_awards_tracks/index-names", NAMES_LIMIT, "name", false);
					
					iter = result.iterator();
					
					if (result.size() < 1){	//object name not found!
						//System.err.println("object name '" + searchString + "' not found!");
						tempString = searchString;	//use object ID instead
					}
					else {
						tempString = result.iterator().next();
						iterVecString.set(i, tempString);
					}
					
					if (i == 0) outputFile = new FileWriter(outputDirPath + iterVecString.get(0) + ".txt", false);	//don't append
					
					outputFile.write(iterVecString.get(0) + " - " + tempString + "\n");
					
					totalTracks++;
				}
	
				totalTracks--; //because first element is artist name, not a track name
				outputFile.close();
			}
			end = new Date();
			System.out.println("\nSEARCH FINISHED");
			System.out.println(totalTracks + " total tracks found in "  + (end.getTime() - start.getTime()) / 1000 + " seconds.");

			outputFile.close();
		
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

}
