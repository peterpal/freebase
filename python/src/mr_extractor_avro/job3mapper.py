__author__ = 'Ondrej Galbavy'

import sys

# 3. job mapper - filter records
# 1. and 2. job output
# ->
# type_id%used_by\tobject_id
# type_id%title\tvalue

def parse_line(line):
    ckey, value = line.split("\t", 1)
    key, rel = ckey.split("%", 1)
    return key.strip(), rel.strip(), value.strip()

for line in sys.stdin:
    key, rel, value = parse_line(line)

    if rel == 'used_by' or rel == 'title':
        print(line.strip())
