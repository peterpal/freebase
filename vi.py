import string

_debug = 0;

class Name:

	def __init__(self, name, lang):
		self.name = name
		self.lang = lang



class ParsedObject:
	
	def __init__(self, id):
		self.id = id
		self.names = []
		self.types = []

		if(_debug): print "_debug: ObjectCreated: " + id

	def addName(self, name, lang):
		self.names.append(Name(name, lang))
		if(_debug): print "_debug: Adding name: " + name + "@" + lang

	def addType(self, type):
		self.types.append(type)
		if(_debug): print "_debug: Adding type: " + type



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



def parseFile(filename):

	parsedObjects = []
	lastId = ""
	i = 0

	f = open(filename, 'r')

	line = f.readline()
	

	while(line != ""):
	
		i += 1
		if(i % 100000 == 0):
			print i
		
		parsedLine = parseLine(line)
		if(parsedLine != None):

			if(parsedLine[0] != lastId):
				# reading a new object

				parsedObjects.append(ParsedObject(parsedLine[0]))
				lastId = parsedLine[0]

		
			# reading the same object (last)
			
			if(parsedLine[1] == "type.object.name"):
				nameLang = parsedLine[2].split("@")

				if(len(nameLang) > 1):
					parsedObjects[-1].addName(nameLang[0], nameLang[1])

			if(parsedLine[1] == "type.object.type"):
				parsedObjects[-1].addType(parsedLine[2])

		line = f.readline()

	f.close()

	return parsedObjects
		



#filename = "data/sample_freebase-rdf-2014-09-28-00-00"
filename = "data/names_types_2"
objects = parseFile(filename)

f = open("data/output", 'w')

f.write("Parsing file: " + filename)
f.write("Number of parsed objects: " + str(len(objects)))

f.close()

var = raw_input("Please enter something: ")