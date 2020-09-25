__author__ = 'Ondrej Galbavy'

import sys

# 4. job mapper - filter
# 1. and 3. job output
# ->
# object_id%title\tvalue
# object_id%alias\tvalue
# object_id%type\ttype_title

def parse_line(line):
    ckey, value = line.split("\t", 1)
    key, rel = ckey.split("%", 1)
    return key.strip(), rel.strip(), value.strip()

current_id = None

for line in sys.stdin:
    key, rel, value = parse_line(line)

    if rel != 'type_id':
        print line.strip()
