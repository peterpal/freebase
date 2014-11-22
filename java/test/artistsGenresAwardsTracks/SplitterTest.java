/**
 * 
 */
package test.artistsGenresAwardsTracks;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;

import org.junit.Test;

import src.artistGenresAwardsTracks.utils.Parser;
import src.artistGenresAwardsTracks.utils.Splitter;

/**
 * @author stefanlinner
 *
 */
public class SplitterTest {

	/**
	 * Test method for {@link src.artistGenresAwardsTracks.utils.Splitter#splitParsedDump(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testSplitParsedDump() {
		
		String sampleParsedDumpFilePath = "./data/artists_genres_awards_tracks/test_files/sample_parsed_true.txt";
		String outputDirPath = "./data/artists_genres_awards_tracks/test_files/splitter_test/";

		Splitter.splitParsedDump(sampleParsedDumpFilePath, outputDirPath);
		
		Vector<String> splitFileNames = new Vector<String>(); 
		splitFileNames.add("artist_ID-genre_ID.txt");
		splitFileNames.add("artist_ID-track_ID.txt");
		splitFileNames.add("awarded_artist_ID.txt");
		splitFileNames.add("object_ID-name.txt");
		
		byte[] encoded;
		try {
			for (String fileName : splitFileNames) {
				
				encoded = Files.readAllBytes(Paths.get("./data/artists_genres_awards_tracks/test_files/splitter_true/" + fileName));
				String trueFile = new String(encoded, "UTF-8");
				encoded = Files.readAllBytes(Paths.get("./data/artists_genres_awards_tracks/test_files/splitter_test/" + fileName));
				String testFile = new String(encoded, "UTF-8");
				
				assertEquals("Split test file '"+ fileName +"' is not equal to ground truth file",
						trueFile, testFile);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			fail("Catched Exception");
		}
		
	}

}
