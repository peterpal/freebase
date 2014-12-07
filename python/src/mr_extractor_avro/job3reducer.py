__author__ = 'Ondrej Galbavy'

import sys

# 3. job reducer - dereference typa names
# type_id%used_by\tobject_id
# type_id%title\tvalue
# ->
# object_id%type\ttype_title

def parse_line(line):
    ckey, value = line.split("\t", 1)
    key, rel = ckey.split("%", 1)
    return key.strip(), rel.strip(), value.strip()

current_id = None
current_title = None

for line in sys.stdin:
    key, rel, value = parse_line(line)

    if key != current_id:
        current_id = key
        current_title = None

    if rel == 'title':
        current_title = value

    if rel == 'used_by' and current_title != None:
        print(value + "%type\t" + current_title)
