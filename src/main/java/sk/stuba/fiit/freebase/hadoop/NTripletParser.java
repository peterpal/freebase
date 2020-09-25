package sk.stuba.fiit.freebase.hadoop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.apache.commons.lang.StringUtils;

public class NTripletParser {
	private Pattern pattern;
	
	private Map<String, Pattern> predicatesMap;
	
	public NTripletParser(Map<String, Pattern> predicatesMap) {
		this.predicatesMap = predicatesMap;
		pattern = getPattern(predicatesMap.keySet());
	}
	
	private static Pattern getPattern(Set<String> predicates) {
		return Pattern.compile("<http:\\/\\/rdf\\.freebase\\.com\\/ns\\/m\\.([^>]+)>\\s+"
								+ "<http:\\/\\/rdf\\.freebase\\.com\\/ns\\/("+StringUtils.join(predicates, "|")+")>"
								+ "(.+)\\.");
	}

	public Triplet parseLine(String line) {
		Matcher m = null;
		m = pattern.matcher(line);
		if (!m.matches())
			return null;
		Pattern objP = predicatesMap.get(m.group(2));
		if (objP == null)
			return null;
		
		Matcher objM = objP.matcher(m.group(3).trim());
		if(!objM.matches())
			return null;

		return new Triplet(m.group(1), m.group(2), objM.group(1));
	}

	private Topic mergeTriplets(String id, List<Triplet> triplets) {
		Topic topic = new Topic(id);
		for (Triplet triplet : triplets) {
			topic.addObject(triplet.getPredicat().toString(), triplet.getObject().toString());
		}

		return topic;
	}

	public List<Topic> parseGzFile(File file) throws IOException {
		FileInputStream fileIS = new FileInputStream(file);
		GZIPInputStream gzipStream = new GZIPInputStream(fileIS);
		BufferedReader br = new BufferedReader(
				new InputStreamReader(gzipStream, "UTF-8"));

		String line = null;

		Triplet lastTriplet = null;
		Triplet newTriplet = null;
		List<Topic> topics = new ArrayList<>();
		List<Triplet> triplets = new ArrayList<>();

		while ((line = br.readLine()) != null) {
			newTriplet = parseLine(line);
			if (newTriplet != null) {
				if (lastTriplet != null && !lastTriplet.getSubject().equals(newTriplet.getSubject())) {
					if (!triplets.isEmpty()) {
						topics.add(mergeTriplets(lastTriplet.getSubject().toString(), triplets));
						triplets.clear();
					}
					lastTriplet = newTriplet;
				}
				triplets.add(newTriplet);
				lastTriplet = newTriplet;
			}
		}

		if (!triplets.isEmpty()) {
			topics.add(mergeTriplets(lastTriplet.getSubject().toString(), triplets));
			triplets.clear();
		}

		br.close();
		return topics;
	}
	
	public static void main(String[] args) throws IOException {
		
		File file = new File("data/freebase_test.gz");
		Map<String, Pattern> predicatesMap = new HashMap<>();
		predicatesMap.put("type.object.name", Pattern.compile("\"([^>]+)\"@en"));
		predicatesMap.put("type.object.type", Pattern.compile("<http:\\/\\/rdf\\.freebase\\.com\\/ns\\/([^>]+)>"));
		predicatesMap.put("common.topic.alias", Pattern.compile("\"([^>]+)\"@en"));
		

		NTripletParser ntp = new NTripletParser(predicatesMap);
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10; i++)
			ntp.parseGzFile(file);
		
		System.out.println("New: "+(System.currentTimeMillis()-start)/1000.0);
		

		ntp = new NTripletParser(predicatesMap);
		start = System.currentTimeMillis();
		for (int i = 0; i < 10; i++)
			ntp.parseGzFile(file);
		
		System.out.println("Old: "+(System.currentTimeMillis()-start)/1000.0);
	}

}

class NTripletParserOld {
	private static List<Pattern> patterns = getListOfPatterns();

	private static List<Pattern> getListOfPatterns() {
		return Arrays.asList(
				Pattern.compile("<http:\\/\\/rdf\\.freebase\\.com\\/ns\\/m\\.([^>]+)>\\s+"
								+ "<http:\\/\\/rdf\\.freebase\\.com\\/ns\\/(type.object.type)>\\s+"
								+ "<http:\\/\\/rdf\\.freebase\\.com\\/ns\\/([^>]+)>\\s+\\."),
				Pattern.compile("<http:\\/\\/rdf\\.freebase\\.com\\/ns\\/m\\.([^>]+)>\\s+"
						+ "<http:\\/\\/rdf\\.freebase\\.com\\/ns\\/(type.object.name)>\\s+"
						+ "\"([^>]+)\"@en\\s+\\."),
				Pattern.compile("<http:\\/\\/rdf\\.freebase\\.com\\/ns\\/m\\.([^>]+)>\\s+"
						+ "<http:\\/\\/rdf\\.freebase\\.com\\/ns\\/(common.topic.alias)>\\s+"
						+ "\"([^>]+)\"@en\\s+\\.")
		);
	}

	public Triplet parseLine(String line) {
		Matcher m = null;
		for(Pattern pattern : patterns) {
			m = pattern.matcher(line);
			if(m.matches())
				break;
		}
		if (!m.matches())
			return null;
		

		return new Triplet(m.group(1), m.group(2), m.group(3));
	}

	public Topic mergeTriplets(String id, List<Triplet> triplets) {
		Topic topic = new Topic(id);
		for (Triplet triplet : triplets) {
			topic.addObject(triplet.getPredicat().toString(), triplet.getObject().toString());
		}

		return topic;
	}

	public List<Topic> parseGzFile(File file) throws IOException {
		FileInputStream fileIS = new FileInputStream(file);
		GZIPInputStream gzipStream = new GZIPInputStream(fileIS);
		BufferedReader br = new BufferedReader(
				new InputStreamReader(gzipStream, "UTF-8"));

		String line = null;

		Triplet lastTriplet = null;
		Triplet newTriplet = null;
		List<Topic> topics = new ArrayList<>();
		List<Triplet> triplets = new ArrayList<>();

		while ((line = br.readLine()) != null) {
			newTriplet = parseLine(line);
			if (newTriplet != null) {
				if (lastTriplet != null && !lastTriplet.getSubject().equals(newTriplet.getSubject())) {
					if (!triplets.isEmpty()) {
						topics.add(mergeTriplets(lastTriplet.getSubject().toString(), triplets));
						triplets.clear();
					}
					lastTriplet = newTriplet;
				}
				triplets.add(newTriplet);
				lastTriplet = newTriplet;
			}
		}

		if (!triplets.isEmpty()) {
			topics.add(mergeTriplets(lastTriplet.getSubject().toString(), triplets));
			triplets.clear();
		}

		br.close();
		return topics;
	}

}
