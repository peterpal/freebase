# -*- coding: utf-8 -*-

import json

#import ijson
import ijson.backends.python as ijson
import sys
import string
import codecs
import io

from bintrees import BinaryTree, AVLTree, RBTree
from freebaseObject import FreebaseObject
from index import Index


"""
author: Bc. Lukáš Gregorovič

Indexer utility

"""


# helper function - create and initialise FreebaseObject from JSON record
def create_FreebaseOnject_from_rec(rec):
	# init instance
	f = FreebaseObject()
	# load data from rec
	f.init_from_json(rec)

	# return instance
	return f



# create new index with name "index"
index = Index()

# read parsed freebase output (json); change filename according requested input file
f = open('../../../../data/sample_freebase-rdf-2014-09-28-00-00_output')

# get records from JSON file (streaming)
records = ijson.items(f, 'freebase.item')

# counter
x = 0

# traversing records from file
for rec in records:
	
	# index each record
	index.index_object(create_FreebaseOnject_from_rec(rec))

	x += 1

	# print number of indexed object
	if (x % 1000) == 0:
		print x

	# maximum number of indexed object constraint
	if x > 20000:
		break

print "\n---\n"



# store index to filesystem
index_name = "index"

# keys for indexin trees (load balance)
hashKeys = ['a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','x','y','z','0','1','2','3','4','5','6','7','8','9','+']

# sotre each index tree to separate file
for h in hashKeys:
	# create file for tree
	f = open(index_name + '/bin_tree_dump_' + h + '.txt','w')

	# get items stored in tree [(k,v)]
	g = index.get_items_generator(h)

	# for each item, dump it to file
	for j in g:
		f.write(json.dumps(j) + "\n")

	# close file for this tree
	f.close()

# create file for object tree
f = open('index/bin_tree_dump_objects.txt','w')


# serialise and store each item to file
g = index.get_items_generator(None)
for j in g:
	f.write(json.dumps(j[1].serialise()) + "\n")

f.close()

# index files are read to be used by search.py tool



