__author__ = 'Ondrej Galbavy'

import sys
import json

# 4. job reducer - merge and emmit JSON
# object_id%title\tvalue
# object_id%alias\tvalue
# object_id%type\ttype_title
# ->
# {'id': current_id, 'name': '', 'alias': [], 'type': []}

def parse_line(line):
    ckey, value = line.split("\t", 1)
    key, rel = ckey.split("%", 1)
    return key.strip(), rel.strip(), value.strip()

current_id = None
current_object = None

for line in sys.stdin:
    key, rel, value = parse_line(line)

    if key != current_id:
        if current_object is not None:
            if current_object['name'] != '':
                print json.dumps(current_object)

        current_id = key
        current_object = {'id': current_id, 'name': '', 'alias': [], 'type': []}

    if rel == 'title':
        current_object['name'] = value
    elif rel == 'alias':
        current_object['alias'].append(value)
    elif rel == 'type':
        current_object['type'].append(value)

# last one
if current_object['name'] != '':
    print json.dumps(current_object)
