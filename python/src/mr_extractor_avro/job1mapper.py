__author__ = 'Ondrej Galbavy'

import re
import sys

# 1. job mapper - filter and convert
# freebase triples
# ->
# object_id%title\tvalue
# object_id%alias\tvalue
# object_id%type_id\tvalue

object_name_re = '^<http://rdf.freebase.com/ns/([a-zA-Z0-9._]+)>	<http://rdf.freebase.com/ns/type.object.name>	"(.+)"@en	.'
object_type_re = '^<http://rdf.freebase.com/ns/([a-zA-Z0-9._]+)>	<http://rdf.freebase.com/ns/type.object.type>	<http://rdf.freebase.com/ns/([a-zA-Z0-9._]+)>	.'
alias_re = '^<http://rdf.freebase.com/ns/([a-zA-Z0-9._]+)>	<http://rdf.freebase.com/ns/common.topic.alias>	"(.+)"@en	.'

for line in sys.stdin:
    object_name_match = re.search(object_name_re, line)
    if object_name_match:
        print(object_name_match.group(1) + "%title\t" + object_name_match.group(2))
        continue

    object_type_match = re.search(object_type_re, line)
    if object_type_match:
        print(object_type_match.group(1) + "%type_id\t" + object_type_match.group(2))
        continue

    alias_match = re.search(alias_re, line)
    if alias_match:
        print(alias_match.group(1) + "%alias\t" + alias_match.group(2))
        continue
