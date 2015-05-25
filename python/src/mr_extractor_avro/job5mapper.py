__author__ = 'Ondrej Galbavy'

import sys
import json

# 5. job mapper - statistics intermediate result
# {'id': current_id, 'name': '', 'alias': [], 'type': []}
# ->
# object_id\ttypes_count\taliases_count\tobject_type

for line in sys.stdin:
	obj = json.loads(line)

	aliases = len(obj['alias'])
	types = len(obj['type'])
	id_type = 'mid' if (obj['id'][0:2] == 'm.') else 'other'

	print obj['id'] + "\t" + str(types) + "\t" + str(aliases) + "\t" + id_type
