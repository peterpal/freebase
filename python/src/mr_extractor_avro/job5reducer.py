__author__ = 'Ondrej Galbavy'

import sys

# 5. job reducer - statistics aggregation
# object_id\ttypes_count\taliases_count\tobject_type
# ->
# see below
# statistics are aggregated only for MID objects

count_obj = 0
count_mid = 0
max_aliases = 0
max_aliases_id = None
avg_aliases = 0.0
max_types = 0
max_types_id = None
avg_types = 0.0

for line in sys.stdin:
	id, types, aliases, id_type = line.split("\t", 3)

	try:
		types = int(types)
		aliases = int(aliases)
	except ValueError:
		continue

	count_obj += 1
	if id_type.strip() == 'mid':
		count_mid += 1
		avg_aliases += aliases
		avg_types += types
		if (aliases > max_aliases):
			max_aliases = aliases
			max_aliases_id = id
		if (types > max_types):
			max_types = types
			max_types_id = id

avg_aliases /= count_mid
avg_types /= count_mid

print "Object count: " + str(count_obj)
print "m.id count: " + str(count_mid)
print "Max aliases: " + str(max_aliases)
print "Max aliases id: " + str(max_aliases_id)
print "Average aliases: " + str(avg_aliases)
print "Max types: " + str(max_types)
print "Max types id: " + str(max_types_id)
print "Average types: " + str(avg_types)
