package sk.stuba.fiit.freebase.hadoop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class ExtractTestFragment {
	
	

	public static void main(String[] args) throws IOException {
		
		FileInputStream fileIS = new FileInputStream("data/freebase-rdf-2014-09-21-00-00.gz");
		GZIPInputStream gzipStream = new GZIPInputStream(fileIS);
		BufferedReader br = new BufferedReader(new InputStreamReader(gzipStream));
		
		String line = null;
		File newFile = new File("data/freebase_test.rdf");
		FileWriter fw = new FileWriter(newFile);

		String patternString = "<http://rdf\\.freebase\\.com/ns/m\\.([^>]*)>\\s+<http:\\/\\/rdf\\.freebase\\.com\\/ns\\/([^>]+)>.*";
		Pattern pattern = Pattern.compile(patternString);
		String lastId = null;
		String newId = null;
		int i=0;
		boolean title = true, alias= true, type = true;
		
		StringBuilder stb = new StringBuilder();
		
		while((line = br.readLine()) != null) {
			Matcher m = pattern.matcher(line);
			if(m.matches()) {
				newId = m.group(1);
				
				if(m.group(2).equals("type.object.name"))
					title = true;
				
				if(m.group(2).equals("common.topic.alias"))
					alias = true;
				
				if(m.group(2).equals("type.object.type"))
					type = true;
				
				if(!newId.equals(lastId)) {					
					lastId = newId;
					if(title && alias && type) {
						
						fw.append(stb.toString());
						
						title = alias = type = false;
						
						if(++i == 20)
							break;
					}
					stb = new StringBuilder();
				}
				stb.append(line+"\n");
				
				
			}
		}
		fw.close();
		br.close();
		
	}
}
