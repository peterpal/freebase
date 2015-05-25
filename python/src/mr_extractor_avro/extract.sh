streaming=/usr/lib/hadoop-mapreduce/hadoop-streaming.jar
avro_schema_url=file:///usr/lib/hue/freebase/freebase_object.avsc
freebase_dump_path=freebase/freebase_head_1M.gz
job1output=freebase/job1output
job2output=freebase/job2output
job3output=freebase/job3output
job4output=freebase/job4output


hdfs dfs -rmr $job1output

hadoop \
jar $streaming \
-D mapred.job.name='Freebase extract - phase 1' \
-input $freebase_dump_path \
-output $job1output \
-mapper "python job1mapper.py" \
-reducer "python job1reducer.py" \
-file job1mapper.py \
-file job1reducer.py


hdfs dfs -rmr $job2output

hadoop \
jar $streaming \
-D mapred.job.name='Freebase extract - phase 2' \
-input $job1output \
-output $job2output \
-mapper "python job2mapper.py" \
-reducer "python job2reducer.py" \
-file job2mapper.py \
-file job2reducer.py


hdfs dfs -rmr $job3output

hadoop \
jar $streaming \
-D mapred.job.name='Freebase extract - phase 3' \
-D mapreduce.map.output.key.field.separator='%' \
-D stream.num.map.output.key.fields=2
-D mapreduce.partition.keypartitioner.options=-k1,1 \
-partitioner org.apache.hadoop.mapred.lib.KeyFieldBasedPartitioner \
-input $job1output \
-input $job2output \
-output $job3output \
-mapper "python job3mapper.py" \
-reducer "python job3reducer.py" \
-file job3mapper.py \
-file job3reducer.py


hdfs dfs -rmr $job4output

hadoop \
jar $streaming \
-libjars avro-json-1.0-SNAPSHOT.jar \
-D mapreduce.job.name='Freebase extract - phase 4' \
-D mapreduce.map.output.key.field.separator='%' \
-D stream.num.map.output.key.fields=2
-D mapreduce.partition.keypartitioner.options=-k1,1 \
-D output.schema.url=$avro_schema_url \
-partitioner org.apache.hadoop.mapred.lib.KeyFieldBasedPartitioner \
-input $job1output \
-input $job3output \
-output $job4output \
-mapper "python job4mapper.py" \
-reducer "python job4reducer.py" \
-outputformat com.cloudera.science.avro.streaming.AvroAsJSONOutputFormat \
-file job4mapper.py \
-file job4reducer.py \
-file freebase_object.avsc \
-file avro-json-1.0-SNAPSHOT.jar
