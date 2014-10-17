__author__ = 'Ondrej Galbavy'

import re


class Extractor:
    object_name_re = '^<http://rdf.freebase.com/ns/([a-zA-Z0-9._]+)>    <http://rdf.freebase.com/ns/type.object.name>    "(.+)"@en    .$'
    object_type_re = '^<http://rdf.freebase.com/ns/([a-zA-Z0-9._]+)> +<http://rdf.freebase.com/ns/type.object.type> +<http://rdf.freebase.com/ns/([a-zA-Z0-9._]+)> +.'
    alias_re = '^<http://rdf.freebase.com/ns/([a-zA-Z0-9._]+)> +<http://rdf.freebase.com/ns/common.topic.alias> +"(.+)"@en +.'

    @staticmethod
    def ensure_id(id, objects):
        if id not in objects:
            objects[id] = {'id': id, 'name': '', 'alias': [], 'type': []}

    @staticmethod
    def extract(lines):
        names = {}
        objects = {}

        for line in lines:
            object_name_match = re.search(Extractor.object_name_re, line)
            if object_name_match:
                names[object_name_match.group(1)] = object_name_match.group(2)

            object_type_match = re.search(Extractor.object_type_re, line)
            if object_type_match:
                obj_id = object_type_match.group(1)
                Extractor.ensure_id(obj_id, objects)
                objects[obj_id]['type'].append(object_type_match.group(2))

            alias_match = re.search(Extractor.alias_re, line)
            if alias_match:
                obj_id = alias_match.group(1)
                Extractor.ensure_id(obj_id, objects)
                objects[obj_id]['alias'].append(alias_match.group(2))

        for obj_id in objects:
            name = names[obj_id]
            objects[obj_id]['name'] = name
            objects[obj_id]['type'] = map(lambda id: names[id], objects[obj_id]['type'])

        return objects
