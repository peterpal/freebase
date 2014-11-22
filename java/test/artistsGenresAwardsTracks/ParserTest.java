/**
 * 
 */
package test.artistsGenresAwardsTracks;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import src.artistGenresAwardsTracks.utils.Parser;

/**
 * @author stefanlinner
 *
 */
public class ParserTest {

	/**
	 * Test method for {@link src.artistGenresAwardsTracks.utils.Parser#parseDump(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testParseDump() {
		String sampleDumpRDFPath = new String("./data/sample_artists_awards_tracks_data.txt");
		String sampleOutputFilePath = new String("./data/sample_artists_awards_tracks_parsed.txt");
		String testsampleOutputFilePath = new String("./data/sample_artists_awards_tracks_parsed_test.txt");
		
		Parser.parseDump(sampleDumpRDFPath, testsampleOutputFilePath);
		
		byte[] encoded;
		try {
			
			encoded = Files.readAllBytes(Paths.get(sampleOutputFilePath));
			String sampleOutput = new String(encoded, "UTF-8");
			encoded = Files.readAllBytes(Paths.get(testsampleOutputFilePath));
			String testsampleOutput = new String(encoded, "UTF-8");
			
			assertEquals("Parsed '"+ sampleDumpRDFPath +"' must be equal to '"+ sampleOutputFilePath +"'",
					sampleOutput, testsampleOutput);
			
		} catch (IOException e) {
			e.printStackTrace();
			fail("Catched Exception");
		}
			
	}

}
