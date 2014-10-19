package sk.stuba.fiit.freebase.hadoop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;
import org.apache.avro.mapred.AvroCollector;
import org.apache.avro.mapred.AvroJob;
import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapred.AvroReducer;
import org.apache.avro.mapred.AvroValue;
import org.apache.avro.mapred.Pair;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

public class MapToppicTitleAltTypeToAvro extends Configured implements Tool {

	private static final Logger sLogger = Logger
			.getLogger(MapToppicTitleAltTypeToAvro.class);

	public static class Map extends Mapper<LongWritable, Text, AvroKey<CharSequence>, AvroValue<Triplet>> {

		private final static NTripletParser parser = new NTripletParser();

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String line = value.toString();
			Triplet triplet = parser.parseLine(line);
			if(triplet!=null) {
				context.write(new AvroKey<CharSequence>(triplet.getSubject()), new AvroValue<Triplet>(triplet));
			}
		}
	}

	public static class Reduce extends
			AvroReducer<CharSequence, Triplet, Pair<CharSequence, TopicAvro>> {

		@Override
		public void reduce(CharSequence key, Iterable<Triplet> values,
				AvroCollector<Pair<CharSequence, TopicAvro>> collector,
				Reporter reporter) throws IOException {

			List<CharSequence> types = new ArrayList<>();
			List<CharSequence> alts = new ArrayList<>();
			CharSequence title = "";
			for (Triplet triplet : values) {
				if (triplet.getPredicat().toString().equals("common.topic.alias")) {
					alts.add(triplet.getObject().toString());
				} else if (triplet.getPredicat().toString().equals("type.object.name")) {
					title = triplet.getObject().toString();
				} else if (triplet.getPredicat().toString().equals("type.object.type")) {
					types.add(triplet.getObject().toString());
				}
			}
			

			TopicAvro topicAvro = new TopicAvro(title, types, alts);

			collector.collect(new Pair<CharSequence, TopicAvro>(key.toString(),
					topicAvro));
		}
	}

	public int run(String[] args) throws Exception {
		if (args.length != 2) {
			System.err
					.println("Usage: AvroWordCount <input path> <output path>");
			return -1;
		}

		Configuration conf = getConf();
		conf.set("mapred.job.tracker", "192.168.56.103:8021");
		conf.set("fs.default.name", "hdfs://192.168.56.103:8020");
		conf.set("hadoop.tmp.dir", "C:/users/Miroslav/tmp");

		JobConf jobconf = new JobConf(conf);
		AvroJob.setReducerClass(jobconf, Reduce.class);
		AvroJob.setMapOutputSchema(jobconf, Pair.getPairSchema(Schema.create(Type.STRING),
						Triplet.getClassSchema()));
		AvroJob.setOutputSchema(
				jobconf,
				Pair.getPairSchema(Schema.create(Type.STRING),
						TopicAvro.getClassSchema()));

		Job job = Job.getInstance(jobconf);
		job.setJarByClass(MapToppicTitleAltTypeToAvro.class);
		job.setJobName("wordcount");

		job.setMapperClass(Map.class);
		job.setInputFormatClass(TextInputFormat.class);

//		job.setSortComparatorClass(Text.Comparator.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		boolean result = job.waitForCompletion(true);
		return result ? 0 : -1;
	}

	public static void main(String[] args) throws Exception {
		String inPath, outPath;
		if (args.length >= 2) {
			inPath = args[0];
			outPath = args[1];
		} else {
			inPath = "/data/freebase-rdf-2014-09-21-00-00.gz";
			outPath = "/data/out/freebase-rdf-2014-09-21-00-00-"+new Date().getTime();
		}

		int res = ToolRunner.run(new Configuration(),
				new MapToppicTitleAltTypeToAvro(), new String[] { inPath,
						outPath });
		System.exit(res);
	}
}
