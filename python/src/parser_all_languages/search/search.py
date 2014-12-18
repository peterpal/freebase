import json
import string

from bintrees import BinaryTree, AVLTree, RBTree
import collections

execfile("../indexer/index.py")


# load existing index to memmory
index = Index("../indexer/index")

# main loop for search
while True:

	# create empty results set
	results = {}

	# reads user input to search
	x = raw_input("Search: ")	

	# check escape command for exiting tool
	if x == "qq":
		break

	# split input by space
	x = x.split(" ")

	# for each word in input
	for i in x:
		# select tree by first letter		
		t = index.select_tree(str(i[0]))
		
		# get record from selected tree key = word from input
		r = t.get(str(i))

		# if number of results for key is 0, print message
		if r == None:
			print "No results for " + i + "\n"
			continue

		# split IDs from result
		rs = r.split(",")

		# count IDs in results set
		for rr in rs:
			if rr in results:
				results[rr] = results[rr] + 1	
			else:
				results[rr] = 1

	# create counter for set
	counter = collections.Counter(results)
	print "------------\n"

	# select most common IDs from results (10 IDs)
	for c in counter.most_common(10):
		# for each ID get object from objects ID
		p = index.get_object(str(c[0]))
		#print object to user
		print p.to_string()
