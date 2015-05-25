__author__ = 'Ondrej Galbavy'

import sys
import codecs

# 6. job reducer - category usage aggregation
# type_name\t1 - for every type
# ->
# type_name\ttotal_count


UTF8Writer = codecs.getwriter('utf8')
sys.stdout = UTF8Writer(sys.stdout)

current_type = None
current_count = 0

for line in sys.stdin:
	type, count = line.strip().split('\t', 1)

	try:
		count = int(count)
	except ValueError:
		continue

	if type != current_type:
		if current_type is not None:
			print current_type + "\t" + str(current_count)
		current_type = type
		current_count = 0

	current_count += 1

# last one
print current_type + "\t" + str(current_count)
