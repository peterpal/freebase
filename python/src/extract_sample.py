__author__ = 'Ondrej Galbavy'

from extractor import extractor

triples_files = '../data/sample_basic_objects.triples'

f = open(triples_files, 'r')

objects = extractor.Extractor.extract(f)
print objects.__str__()

f.close()
