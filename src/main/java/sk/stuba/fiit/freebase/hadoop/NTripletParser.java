package sk.stuba.fiit.freebase.hadoop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class NTripletParser {
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
