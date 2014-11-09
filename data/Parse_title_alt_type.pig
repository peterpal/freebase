REGISTER json-simple-1.1.1.jar
REGISTER avro-1.7.4.jar
REGISTER piggybank.jar


STOCK_A = LOAD 'SD.txt' using PigStorage('>') AS (subject:chararray, predicate:chararray, object:chararray);

RESULT = FILTER STOCK_A BY ((predicate matches '.*alias.*') AND (object matches '.*@en.*')) OR (predicate matches '.*type.object.type.*') OR ((predicate matches '.*type.object.name.*') AND (object matches '.*@en.*'));

RESULT_S = FOREACH RESULT GENERATE  
SUBSTRING(subject,INDEXOF(subject,'ns/',0)+3,(int)SIZE(subject)) AS subject;
dataID = GROUP RESULT_S by subject;
dataID1 = FOREACH dataID GENERATE group as subject;


RESULT_A = FOREACH RESULT GENERATE 
SUBSTRING(subject,INDEXOF(subject,'ns/',0)+3,(int)SIZE(subject)) AS subject, 
SUBSTRING(predicate,INDEXOF(predicate,'\u002E',38)+1,(int)SIZE(predicate))AS predicate, 
SUBSTRING(REPLACE(object,'@en.*',''),INDEXOF(object,'ns/',0)+3,(int)SIZE(REPLACE(object,'@en.*','')))AS object;

ALIAS = FILTER RESULT_A BY (predicate matches '.*alias.*');
AlIAS_F = FOREACH ALIAS GENERATE subject,object AS alias;

dataA = GROUP AlIAS_F by subject;

dataA1 = FOREACH dataA{
                       alias_column = FOREACH AlIAS_F generate alias;
                       GENERATE group as subject, alias_column as alias;
}

TYPE = FILTER RESULT_A BY (predicate matches '.*type.*');
TYPE_F = FOREACH TYPE GENERATE subject,object AS type;

dataT = GROUP TYPE_F by subject;

dataT1 = FOREACH dataT{
                       type_column = FOREACH TYPE_F generate type;
                       GENERATE group as subject, type_column as type;
}

TITLE = FILTER RESULT_A BY (predicate matches '.*name.*');
TITLE_F = FOREACH TITLE GENERATE subject,object AS name;

dataR = GROUP TITLE_F by subject;

dataR1 = FOREACH dataR{
                       name_column = FOREACH TITLE_F generate name;
                       GENERATE group as subject, name_column as name;
}
ALLDATA0 = join dataID1 by subject FULL, dataA1 by subject;
ALLDATA1 = join ALLDATA0 by dataID1::subject FULL, dataT1 by subject;
ALLDATA2 = join ALLDATA1 by dataID1::subject FULL, dataR1 by subject;

DESCRIBE ALLDATA2;
DUMP ALLDATA2;


FINAL = FOREACH ALLDATA2 GENERATE 
ALLDATA1::ALLDATA0::dataID1::subject AS ID,
ALLDATA1::ALLDATA0::dataA1::alias AS alias,
ALLDATA1::dataT1::type AS type,
dataR1::name AS name;

DESCRIBE FINAL;
DUMP FINAL;

STORE FINAL  INTO 'VI/FB_FIN33' 
USING org.apache.pig.piggybank.storage.avro.AvroStorage(
'{"schema": {
		"type":"record",
        "name":"Freebase",
        "fields": [
        	{"name":"ID","type":"string"},
            {"name":"alias","type": [{"type":"array", "items":"string"},"null"]},
        	{"name":"type","type": [{"type":"array", "items":"string"},"null"]},
            {"name":"name","type": [{"type":"array", "items":"string"},"null"]}
        		]
             }
     }'
);
