package parser_indexer.helpers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Function to repair parser XML output file (java bug)
 * @author Bc. Kristina Misikova
 *
 */
public class EscapeAmpInXML {

	
		public static void main(String[] args) throws IOException{
		
		String fileName = "data_tmp/freebase-rdf-2014-10-05-00-00_output_EN.xml";
		String fileNameEscaped = "data_tmp/freebase-rdf-2014-10-05-00-00_output_EN_escaped.xml";
		
		
		InputStream fileInputStream = new FileInputStream(fileName);
		BufferedReader buffReader = new BufferedReader(new InputStreamReader(fileInputStream));
		
		FileWriter fstream = new FileWriter(fileNameEscaped, true);
	    BufferedWriter out = new BufferedWriter(fstream);
		
		
		String line = "";
	
		// read lines from file and parsing triplets
		while ((line = buffReader.readLine()) != null) {
			
			// only printable characters
			line = line.replaceAll("\\P{Print}", "");
			// replace "&" and add new line after each tag
			out.append(line.replaceAll("&", "&amp;").replace(">", ">\n"));
			
		}
		
		out.close();
		buffReader.close();
		
	}
	
	
	
	
    
	
	
	
		
	
	
}
