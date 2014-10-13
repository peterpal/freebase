# -*- coding: utf-8 -*-
#
#------------------------------------------------------------------------------------------------------------
#
# 	Vzhladavanie informacii, 2014/15
#
# 	Zadanie projektu:
# 		Parsovanie typov s nazvami v roznych jazykoch - obmedzenie na 1 alebo N definovanych jazykov.
# 
# 	Autor:
#		Lukáš Gregorovič, 64341
#
# -----------------------------------------------------------------------------------------------------------

import string
import json
import jsonpickle
import unittest


_debug = 0
_print_lines = 100000

class FreebaseObjects:

	freebase = []

	def setObjects(self, objects):
		self.freebase = objects

	def toConsole(self):
		for f in self.freebase:
			print f.to_JSON()

	def to_JSON(self):
		return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4)

class Name:

	def __init__(self, name, lang):
		self.name = name
		self.lang = lang

	def getName(self):
		return self.name

	def getLang(self):
		return self.lang

	def to_JSON(self):
		return json.dumps(self, default=lambda o: o.__dict__, sort_keys=False, indent=4)
	



class ParsedObject:
	
	def __init__(self, id):
		self.id = id
		self.names = []
		self.types = []

		if(_debug): 
			print "_debug: ObjectCreated: " + id


	def addName(self, name, lang):
		self.names.append(Name(name, lang))
		if(_debug): print "_debug: Adding name: " + name + "@" + lang

	def addType(self, type):
		self.types.append(type)
		if(_debug):
			print "_debug: Adding type: " + type

	def getId(self):
		return self.id

	def getNames(self):
		return self.names

	def getTypes(self):
		return self.types

	def hasName(self, name, lang):
		for n in self.names:
			if(name == n.getName()):
				if(lang == "en"):
					return True

		return False

	def hasType(self, type):
		if type in self.types:
			return True

		return False

	def to_JSON(self):
		return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4)



# --------------------------------------------------------------


def parseFile(filename):

	parsedObjects = []
	lastId = ""
	i = 0

	f = open(filename, 'r')

	line = f.readline()
	

	while(line != ""):
	
		if(_print_lines > 0):
			i += 1
			if(i % _print_lines == 0):
				print i

		
		parsedLine = parseLine(line)
		if(parsedLine != None):

			if(parsedLine[0] != lastId):
				# reading a new object

				#todo: write old objects to file (batch)
				parsedObjects.append(ParsedObject(parsedLine[0]))
				lastId = parsedLine[0]

		
			# reading the same object (last)
			if(parsedLine[1] == "type.object.name"):
				nameLang = parsedLine[2].split("@")

				if(len(nameLang) > 1):
					parsedObjects[-1].addName(nameLang[0].split('"')[1], nameLang[1].split(" ")[0])

			if(parsedLine[1] == "type.object.type"):
				parsedObjects[-1].addType(parsedLine[2])

		line = f.readline()

	f.close()
	return parsedObjects


def parseLine(line):

	# split line triplets
	line_array = line.split(">")

	# remove empty element at the end of array
	line_array = line_array[0:3]

	# remove unnecessary string: 
	# "<http://rdf.freebase.com/ns/m.0fw3zk3" => "m.0fw3zk3"
	line_array[0] = line_array[0].split("ns/")[1]
	
	if(line_array[1].find("ns/") > 0):
		line_array[1] = line_array[1].split("ns/")[1]
	
		if(line_array[1] == "type.object.type"):
			line_array[2] = line_array[2].split("ns/")[1]

	else:
		return None

	if(_debug): print "_debug: Parsed line: " + line_array[0] + " " + line_array[1] + " " + line_array[2] 

	return line_array




# --------------------------------------------------------------


filename = "data/sample_freebase-rdf-2014-09-28-00-00"
#filename = "data/tmp/names_types_2"

parsedObjects = parseFile(filename)
freebase = FreebaseObjects()
freebase.setObjects(parsedObjects)

f = open("data/tmp/output", 'w')
f.write(freebase.to_JSON())

f.close()


#unittest
class TestUM(unittest.TestCase):

	def setUp(self):
		self.parsedObjects = parseFile(filename)


	def test_number_of_parsed_objects(self):
		self.assertEqual(len(self.parsedObjects),2)   


	# first object  

	def test_first_object_has_id(self):
		self.assertEqual(self.parsedObjects[0].getId(), "m.0fw3zk3")  

	def test_first_object_has_name(self):
		self.assertTrue(self.parsedObjects[0].hasName("HOLE IN THE WALL", "en"))
		self.assertFalse(self.parsedObjects[0].hasName("HOLE IN THE WALL", "sk"))

	def test_first_object_has_N_names(self):
		self.assertEqual(len(self.parsedObjects[0].getNames()),1)

	def test_first_object_has_N_types(self):
		self.assertEqual(len(self.parsedObjects[0].getTypes()),5)

	def test_first_object_has_types(self):
		self.assertTrue(self.parsedObjects[0].hasType("music.album"))
		self.assertTrue(self.parsedObjects[0].hasType("base.type_ontology.inanimate"))
		self.assertTrue(self.parsedObjects[0].hasType("common.topic"))
		self.assertTrue(self.parsedObjects[0].hasType("base.type_ontology.abstract"))
		self.assertTrue(self.parsedObjects[0].hasType("base.type_ontology.non_agent"))


	# second object

	def test_first_object_has_id(self):
		self.assertEqual(self.parsedObjects[1].getId(), "m.0fw49kg")  

	def test_second_object_has_name(self):
		self.assertTrue(self.parsedObjects[1].hasName("Kind of Blue", "en"))

	def test_second_object_has_N_names(self):
		self.assertEqual(len(self.parsedObjects[1].getNames()),1)

	def test_second_object_has_N_types(self):
		self.assertEqual(len(self.parsedObjects[1].getTypes()),5)

	def test_second_object_has_types(self):
		self.assertTrue(self.parsedObjects[1].hasType("common.topic"))
		self.assertTrue(self.parsedObjects[1].hasType("base.type_ontology.non_agent"))
		self.assertTrue(self.parsedObjects[1].hasType("music.recording"))
		self.assertTrue(self.parsedObjects[1].hasType("music.single"))
		self.assertTrue(self.parsedObjects[1].hasType("base.type_ontology.abstract"))


if __name__ == '__main__':
    unittest.main()




