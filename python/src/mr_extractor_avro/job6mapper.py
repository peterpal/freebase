__author__ = 'Ondrej Galbavy'

import sys
import json
import codecs

# 6. job mapper - category usage
# {'id': current_id, 'name': '', 'alias': [], 'type': []}
# ->
# type_name\t1 - for every type


UTF8Writer = codecs.getwriter('utf8')
sys.stdout = UTF8Writer(sys.stdout)

for line in sys.stdin:
	obj = json.loads(line)

	for type in obj['type']:
		print type + "\t1"

