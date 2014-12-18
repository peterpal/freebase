from bintrees import BinaryTree, AVLTree, RBTree
import json

execfile("../indexer/indexEntry.py")
execfile("../indexer/freebaseObject.py")

"""
author: Bc. Lukáš Gregorovič

Index object
- building index
- loading existing index
- querying index to get objects from name in any language

"""
class Index():

	# balanced trees to store index references (name -> ID), each letter of alphabet and digit have one tree
	hash_index_tree = {}
	# tree for storing objects by ID
	objects = AVLTree()

	def __init__(self, index_name):
		if index_name != None:
			# initialise index from folder
			self.load_persistant_index(index_name)
		else:
			# initialise new index
			self.inilialise()



	def inilialise(self):
		hashKeys = ['a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','x','y','z','0','1','2','3','4','5','6','7','8','9','+']
		
		#for each key create separate balanced tree
		for k in hashKeys:
			self.hash_index_tree[k] = AVLTree()


	# append object to index
	def index_object(self, obj):

		# store whole object in objec tree
		self.append_to_objects(obj)

		# store index reference to object for each name and each word in name of object
		object_id = obj.get_id()
		names = obj.get_names()

		# foreach names
		for n in names.keys():
			
			exploded_name = names[n].split(" ")

			# foreach word in name
			for e in exploded_name:
				# appnd to index tree
				self.append_to_index(e, object_id)



	def get_object(self, key):
		return self.objects.get(key)



	def append_to_objects(self, obj):
		self.objects.insert(obj.get_id(), obj)



	def append_to_index(self, key, object_id):
		
		# select desired tree from hash
		tree = self.select_tree(key)

		if tree == None:
			print "Error adding new entry, key was empty string. Obj.ID: " + object_id + "\n"
			return
		
		# update existing object_id's for key
		entry = tree.get(key);
		
		if entry == None:
			# append first value
			entry = str(object_id)
		else:
			# append N-th value
			entry += "," + object_id
		
		# update entry for key
		tree.insert(key, entry)


	# select indexing tree accoriding key first letter
	def select_tree(self, key):
		if(len(key) == 0):
			return None

		# get first letter of key, lowercase
		k = key[0].lower()			

		# select tree
		if k in self.hash_index_tree:
			# if firt letter is list of trees, return tree
			return self.hash_index_tree[k]
		else:
			# otherwise return universal tree
			return self.hash_index_tree["+"]



	def __str__(self):
		return str(self.hash_index_tree)


	# get items generator for index trees by key, or objects tree (k == None)
	def get_items_generator(self, k):
		if k == None:
			return self.objects.items()

		return self.hash_index_tree[k].items()


	# load index from existing index in folder
	def load_persistant_index(self, index_name):
		# load objects
		self.load_objects(index_name)
		# load all index trees
		self.load_index_trees(index_name)


	# load objects to memmory
	def load_objects(self, index_name):
		# initialise empty tree
		self.objects = AVLTree()

		# try open file with objects
		try:
			ins = open(index_name + "/bin_tree_dump_objects.txt", "r")
		except:
			print "Index tree for objects doesnt exist"
			return

		# reading objects from file, one by one
		for line in ins:
			try:
				# load serialized data from file
				x = json.loads(line)

				# create instance of FreebaseObject
				o = FreebaseObject()
				# initialise content with object data from file
				o.deserialise(x)

				# insert loaded freebase object to objects tree
				self.objects.insert(o.get_id(),o)
			except:
				print "something wrong 2"
		
		print "Index tree for objects loaded"


	#load all index trees
	def load_index_trees(self, index_name):
		hashKeys = ['a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','x','y','z','0','1','2','3','4','5','6','7','8','9','+']

		# for each key in hashKeys try load file with data for index tree
		for key in hashKeys:
			# create empty tree
			self.hash_index_tree[key] = AVLTree()

			# try open appropriate file
			try:
				ins = open(index_name + "/bin_tree_dump_" + key + ".txt", "r")
			except:
				print "Index tree for " + key + " doesnt exist"
				continue

			# try reads entries from file (entry = line)
			for line in ins:

				try:
					# deserialise data from file
					x = json.loads(line)
					# get key and value 
					k = x[0]
					v = x[1] 
					
					# insert data to actual index tree
					self.hash_index_tree[key].insert(k,v)
				except:
					print "something wrong"
			
			print "Index tree for " + key + " loaded"

