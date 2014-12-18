import unittest

execfile("../../src/parser_all_languages/parser/classes.py")

filename = "../../../data/sample_freebase-rdf-2014-09-28-00-00"


"""
author: Bc. Lukáš Gregorovič

UnitTest contains examples for testing parsed data.

Input file contains triplets for 2 entities

We are testing 
- if entity ID is correct, 
- if entity has correct number of names
- if entity has specific name and language,
- if entity has correct number of types
- if entity has specific types (or not) 

Test uses two entities (objects) from freebase:
- m.0fw3zk3
- m.0fw49kg

For testing parser functionality on other entries is necessery update both: input file and the unit test content according that input file

"""

#unittest
class TestUM(unittest.TestCase):

	parsedObjects = None

	def setUp(self):
		# before each test do initialissation:

		# initialise parser
		parser = Parser()
		# parse input file, None -> results stay in memmory
		parser.parseFile(filename, None)

		# get parsed objects
		self.parsedObjects = parser.getParsedObjects()

	
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




