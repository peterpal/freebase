"""
author: Bc. Lukáš Gregorovič

Data classs for parsed freebase objects

"""
class FreebaseObject:

	object_id = ""
	# names inserted by lang as key
	names = {}
	# list of all types 
	types = []


	# initialise object data from JSON record (parsed file)
	def init_from_json(self, jsonRecord):

		self.object_id = ""
		# names inserted by lang as key
		self.names = {}
		# list of all types 
		self.types = []

		# set object ID		
		self.object_id = jsonRecord["id"]

		# append object names with langs
		for n in jsonRecord["names"]:
			self.names[n["lang"]] = n["name"]

		# set object types
		self.types = jsonRecord["types"]


	# getters
	def get_names(self):
		return self.names
	def get_id(self):
		return self.object_id


	# serialise object for storing index (storing object tree in indexer application)
	def serialise(self):
		# empty list
		obj = []
		
		# append all attributes
		obj.append(self.object_id)
		obj.append(self.names)
		obj.append(self.types)

		# return filled list
		return obj


	# reinitialise object from serialised state (creating object tree in search application)
	def deserialise(self, data):

		self.object_id = ""
		# names inserted by lang as key
		self.names = {}
		# list of all types 
		self.types = []
		
		# data contais 0: id (str), 1: names (dict), 2: types (list) 

		# set ID
		self.object_id = data[0]
		# set types
		self.types = data[2]

		# for each lang set name in dict
		langs = data[1].keys()
		for lng in langs:
			try:
				self.names[lng] = data[1][lng]
			except:
				continue
			
	

	# creating string representation of object
	def to_string(self):
		s = "Object: " + self.object_id
		s += "\nNames:"

		for n in self.names.keys():
			try:
				s += "\n\t" + n + ": " + str(self.names[n])
			except:
				continue

		s += "\nTypes:"
		for t in self.types:
			s += "\n\t" + t		

		s += "\n"

		return s


	# creating string representation of object
	def __str__(self):
		s = "Object: " + self.object_id
		s += "\nNames:"

		for n in self.names.keys():
			s += "\n\t" + n + ": " + self.names[n]

		s += "\nTypes:"
		for t in self.types:
			s += "\n\t" + t		

		return s	

