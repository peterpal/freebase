from unittest import TestCase
import extractor

__author__ = 'Ondrej Galbavy'


class TestExtractor(TestCase):
    def test_ensure_id_non_exist(self):
        objects = {}
        extractor.Extractor.ensure_id('some_id', objects)
        self.assertIn('some_id', objects)

    def test_ensure_id_exist(self):
        objects = {}
        objects['some_id'] = {'id': id, 'name': 'Do not overwrite me', 'alias': [], 'type': []}
        extractor.Extractor.ensure_id('some_id', objects)
        self.assertEqual(objects['some_id']['name'], 'Do not overwrite me')

    def test_extract(self):
        triples_files = '../../data/sample_basic_objects.triples'
        f = open(triples_files, 'r')
        objects = extractor.Extractor.extract(f)
        f.close()

        # count of found objects
        self.assertEqual(len(objects), 3)

        # ids of objects
        self.assertListEqual(sorted(objects.keys()), sorted(['m.01dyhm', 'm.03x5qm', 'm.04pm6']))

        # exactly this object
        self.assertDictEqual(objects['m.03x5qm'], {'alias': ['Ubuntu Linux'], 'type': ['Software', 'Operating System', 'Topic', 'Brand', 'Speech topic', 'Literature Subject'], 'id': 'm.03x5qm', 'name': 'Ubuntu'})

        # check language filtering
        self.assertTrue(u'\u0e44\u0e1f\u0e23\u0e4c\u0e1f\u0e2d\u0e01\u0e0b\u0e4c' not in objects['m.01dyhm']['alias'])
