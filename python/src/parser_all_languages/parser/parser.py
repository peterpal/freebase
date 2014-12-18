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
#import jsonpickle


from classes import Name, ParsedObject, FreebaseObjects, Parser

# --------------------------------------------------------------


filename = "data/sample_freebase-rdf-2014-09-28-00-00"
#filename = "data/tmp/names_types_2"
#filename = "D:/freebase_grep/names_types_2_all"
#filename = "/media/mint/ProBookHDD/freebase_names_types_utf-8_all"
outputFilename = "data/tmp/freebase_output_sample"

parser = Parser()
parser.parseFile(filename,outputFilename)
