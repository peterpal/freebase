__author__ = 'Ondrej Galbavy'

import sys

# 2. job - invert references
# object_id%type_id\tvalue
# ->
# type_id%used_by\tobject_id

def parse_line(line):
	ckey, value = line.split("\t", 1)
	key, rel = ckey.split("%", 1)
	return key.strip(), rel.strip(), value.strip()

for line in sys.stdin:
	key, rel, value = parse_line(line)

	if rel == 'type_id':
		print(value + "%used_by\t" + key)
