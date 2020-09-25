Freebase to AVRO extractor
---
These hadoop map reduce jobs extracts *id*, *type.object.name*, *common.topic.alias* and dereferenced *type name* (via *type.object.type*) from every object in freebase dump (including non MID objects). It extracts only english localized values.

### Input
[Freebase triples](https://developers.google.com/freebase/data#freebase-rdf-dumps)

    <http://rdf.freebase.com/ns/m.01dyhm>    <http://rdf.freebase.com/ns/type.object.name>    "Firefox"@en    .
    <http://rdf.freebase.com/ns/m.01dyhm>    <http://rdf.freebase.com/ns/common.topic.alias>   "Mozilla Firebird"@en    .
    <http://rdf.freebase.com/ns/m.01dyhm>    <http://rdf.freebase.com/ns/type.object.type>    <http://rdf.freebase.com/ns/common.topic>    .

### Output
Avro files with schema:
    
    {
    "namespace": "sk.stuba.fiit.freebase",
	    "type": "record",
	    "name": "FreebaseObject",
	    "fields":
	    [
		    {"name": "id", "type": "string"},
		    {"name": "name", "type": "string"},
		    {"name": "alias",  "type": [{"type": "array", "items": "string"}, "null"]},
		    {"name": "type",  "type": [{"type": "array", "items": "string"}, "null"]}
	    ]
    }

### Dependencies
* Python 2.x
* Hadoop
* [avro-json](https://github.com/jwills/avro-json) for avro/json output/input formatting with schema.

### Usage
1. Edit variables in *extract.sh* and *stats.sh*
  * streaming - path to your hadoop-streaming.jar
  * avro_schema_url - url to freebase_object.avsc - included with source code, but needed to be accesible to nodes (can be hdfs://)
  * freebase_dump_path - HDFS path to freebase triples
2. Run *extract.sh*
3. Optionally run *stats.sh* to get basic statistics

### Statistics
Last two jobs generates basic staitstics about freebase objects:
* Object count
* MID count
* Max aliases count and object id
* Average aliases
* Max types count and object id
* Average types
* Type usage

### Map reduce jobs
1. Filters and converts freebase triples to intermediate result `object_id%relation\tvalue`. Relation can be *title*, *alias* or *type_id* (in later jobs *type* and *used_by*)
2. Inverts type relation from *object_id has type_id* to *type_id is used_by object_id* (`type_id%used_by\tobject_id`)
3. Dereferences type ids to type titles. Reducer requires input to be sorted (so title of type preceeds references) and correctly partitioned (so records for single type are present on single node). Outputs *object_id has type name* (`object_id%type\ttype_name`)
4. Merges and converts information about single object to JSON, that is converted to AVRO by output formatter (from avro-utils)
5. Calculates numerical statistics, reducer directly outputs them
6. Calculates type usage
