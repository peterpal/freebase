import os


class FreebaseObjects:

	freebase = []
	outputFile = None
	i = 0

	def setObjects(self, objects):
		self.freebase = objects



	def addObject(self, object):
		self.freebase.append(object)
		if(len(self.freebase) >= 500000):
			self.writeCurrentObjectsToFile()


	def getParsedObjects(self):
		return self.freebase



	def toConsole(self):
		for f in self.freebase:
			print f.to_JSON()



	def to_JSON(self, filename):
		outputFile = open(filename, 'w')
		i = 0

		for f in self.freebase:
			#i = i+1
			outputFile.write(f.to_JSON())

		outputFile.close()



	def to_String(self, filename):
		outputFile = open(filename, 'w')
		i = 0

		outputFile.write("{\n\t\"freebase\": [");

		for f in self.freebase:
			
			if(i == 0):
				outputFile.write(f.to_String())
			else:
				outputFile.write("," + f.to_String())
			
			i = 1

		outputFile.write("\n\t]\n}");
		outputFile.close()



 	def openFile(self, filename):
 		if filename == None:
 			return 

 		self.outputFile = os.open(filename, os.O_RDWR|os.O_CREAT)
		self.i = 0

		os.write(self.outputFile,"{\n\t\"freebase\": [");



	def writeCurrentObjectsToFile(self):

		if self.outputFile == None:
 			return 

		print "writing to file..." + str(len(self.freebase))
		for f in self.freebase:
			
			if(self.i == 0):
				os.write(self.outputFile, f.to_String())
				self.i = 1
			else:
				os.write(self.outputFile,"," + f.to_String())

		print "writing to file is done"
		print "fsync and free memmory..."
		os.fsync(self.outputFile)
		del self.freebase[:]
		print "fsync and free memmory done"
		

			
 	def closeFile(self):
 		if self.outputFile == None:
 			return 

 		self.writeCurrentObjectsToFile()
 		os.write(self.outputFile,"\n\t]\n}");

		os.close(self.outputFile)	





class Name:

	def __init__(self, name, lang):
		self.name = name
		self.lang = lang



	def getName(self):
		return self.name



	def getLang(self):
		return self.lang



	def to_JSON(self):
		return json.dumps(self, default=lambda o: o.__dict__, sort_keys=False, indent=4, encoding="utf-8", ensure_ascii=True)



	def to_String(self):
		return self.name + " @" + self.lang





class ParsedObject:
	
	_debug = False

	def __init__(self, id):
		self.id = id
		self.names = []
		self.types = []

		if(self._debug): 
			print "_debug: ObjectCreated: " + id



	def addName(self, name, lang):
		self.names.append(Name(name, lang))
		if(self._debug): print "_debug: Adding name: " + name + "@" + lang
		#print "_debug: Adding name: " + name + "@" + lang



	def addType(self, type):
		self.types.append(type)
		if(self._debug):
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
		return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4, encoding="utf-8", ensure_ascii=True)



	def to_String(self):

		i = 0

		na = "\t\t\t\"names\": ["
		for n in self.names:

			if(i == 0):
				na += "\n\t\t\t\t{\n"
			else:
				na += ",\n\t\t\t\t{\n"

			na += "\t\t\t\t\t\"name\": " + "\"" + n.getName() + "\",\n"
			na += "\t\t\t\t\t\"lang\": " + "\"" + n.getLang() + "\"\n"
			na += "\t\t\t\t}"

			i = 1

		na += "\n\t\t\t],"

		i = 0

		ty = "\t\t\t\"types\": ["
		for t in self.types:
			if(i == 0):
				ty += "\n\t\t\t\t" + "\"" + t + "\"";
			else:
				ty += ",\n\t\t\t\t" + "\"" + t + "\"";

			i = 1
		ty += "\n\t\t\t]"			

		return "\n\t\t{\n\t\t\t\"id\": \"" + self.id + "\",\n" + na + "\n" + ty + "\n\t\t}"



# --------------------------------------------------------------

class Parser:

	_debug = 0
	_print_lines = 100000

	fb = None

	def parseFile(self, filename, outputFilename):
		
		parsedObject = None;
		lastId = ""
		i = 0

		f = open(filename, 'r')

		line = f.readline()
		
		self.fb = FreebaseObjects()

		self.fb.openFile(outputFilename)

		while(line != ""):
		
			i += 1 

			if(self._print_lines > 0):
				if(i % self._print_lines == 0):
					print i / 100000


			parsedLine = self.parseLine(line)
			if(parsedLine != None):

				if(parsedLine[0] != lastId):
					# reading a new object

					# do not swap in first object iteration
					if(parsedObject != None):
						if(len(parsedObject.getNames()) > 0):					
							self.fb.addObject(parsedObject)

					parsedObject = ParsedObject(parsedLine[0])
					lastId = parsedLine[0]

			
				# reading the same object (last)
				if(parsedLine[1] == "type.object.name"):
					nameLang = parsedLine[2].split("\"@")

					if(len(nameLang) > 1):
						parsedObject.addName(nameLang[0][2:].replace("\"","").strip(), nameLang[1].split(' ')[0])

				if(parsedLine[1] == "type.object.type"):
					parsedObject.addType(parsedLine[2])

			line = f.readline()

		# sublit last parsed object to freebase object
		self.fb.addObject(parsedObject);

		# close freebase input file
		f.close()

		# write remaining objects to file
		self.fb.writeCurrentObjectsToFile()

		self.fb.closeFile()
		return 0


	def getParsedObjects(self):
		return self.fb.getParsedObjects()



	def parseLine(self,line):

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

		if(self._debug): print "_debug: Parsed line: " + line_array[0] + " " + line_array[1] + " " + line_array[2] 

		return line_array


