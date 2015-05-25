streaming=/usr/lib/hadoop-mapreduce/hadoop-streaming.jar
avro_schema_url=file:///usr/lib/hue/freebase/freebase_object.avsc
job4output=freebase/job4output
job5output=freebase/job5output
job6output=freebase/job6output


hdfs dfs -rmr $job5output

hadoop \
jar $streaming \
-libjars avro-json-1.0-SNAPSHOT.jar \
-D mapreduce.job.name='Freebase extract - phase 5' \
-D mapreduce.reduce.tasks=1 \
-D input.schema.url=$avro_schema_url \
-input $job4output \
-output $job5output \
-mapper "python job5mapper.py" \
-reducer "python job5reducer.py" \
-inputformat com.cloudera.science.avro.streaming.AvroAsJSONInputFormat \
-file job5mapper.py \
-file job5reducer.py \
-file avro-json-1.0-SNAPSHOT.jar 


hdfs dfs -rmr $job6output

hadoop \
jar $streaming \
-libjars avro-json-1.0-SNAPSHOT.jar \
-D mapreduce.job.name='Freebase extract - phase 6' \
-D input.schema.url=$avro_schema_url \
-input $job4output \
-output $job6output \
-mapper "python job6mapper.py" \
-reducer "python job6reducer.py" \
-inputformat com.cloudera.science.avro.streaming.AvroAsJSONInputFormat \
-file job6mapper.py \
-file job6reducer.py \
-file avro-json-1.0-SNAPSHOT.jar
