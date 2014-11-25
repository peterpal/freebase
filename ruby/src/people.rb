#require 'java'
# You either use the next line by require the JAR file, or you pass
# the -r flag to JRuby as follows:
# jruby -r /path/to/lucene-core-3.0.1.jar inmemory.rb
# require "lucene-core-3.0.1.jar"

#java_import org.apache.lucene.analysis.standard.StandardAnalyzer
#java_import org.apache.lucene.document.Document
#java_import org.apache.lucene.document.Field
#java_import org.apache.lucene.index.IndexWriter
#java_import org.apache.lucene.queryParser.ParseException
#java_import org.apache.lucene.queryParser.QueryParser
#java_import org.apache.lucene.store.RAMDirectory
#java_import org.apache.lucene.util.Version

#java_import org.apache.lucene.search.IndexSearcher
#java_import org.apache.lucene.search.TopScoreDocCollector

#class People

#  def create_document(id, name, date_of_birth, date_of_death)
#    doc = Document.new
#    doc.add Field.new("id", id, Field::Store::YES, Field::Index::ANALYZED)
#    doc.add Field.new("name", name, Field::Store::YES, Field::Index::ANALYZED)
#    doc.add Field.new("date_of_birth", date_of_birth, Field::Store::YES, Field::Index::NO)
#    doc.add Field.new("date_of_death", date_of_death, Field::Store::YES, Field::Index::NO)
#    doc
#  end

#  def create_index
#    idx     = RAMDirectory.new
#    writer  = IndexWriter.new(idx, StandardAnalyzer.new(Version::LUCENE_30), IndexWriter::MaxFieldLength::LIMITED)

#   File.open('../../data/data.txt', 'r') do |f1|
#      while line = f1.gets
#        line = line.strip.split(";")
#        writer.add_document(create_document(line[0], line[1], line[2], line[3]))
#      end
#    end

#    writer.optimize
#    writer.close
#    idx
#  end

#  def search(searcher, query_string)
#    parser = QueryParser.new(Version::LUCENE_30, "name", StandardAnalyzer.new(Version::LUCENE_30))
#    query = parser.parse(query_string)

#    hits_per_page = 10

#    collector = TopScoreDocCollector.create(5 * hits_per_page, false)
#   searcher.search(query, collector)

    # Notice how this differs from the Java version: JRuby automagically translates
    # underscore_case_methods into CamelCaseMethods, but scoreDocs is not a method:
    # it's a field. That's why we have to use CamelCase here, otherwise JRuby would
    # complain that score_docs is an undefined method.
#    hits = collector.top_docs.scoreDocs

#   hit_count = collector.get_total_hits

#    if hit_count.zero?
#      puts "No matching documents."
#    else
#      puts "%d total matching documents" % hit_count

#      puts "Hits for %s were found in quotes by:" % query_string

#      hits.each_with_index do |score_doc, i|
#        doc_id = score_doc.doc
#        doc_score = score_doc.score

#        puts "doc_id: %s \t score: %s" % [doc_id, doc_score]

#        doc = searcher.doc(doc_id)
#        puts "%d. %s" % [i, doc.get("name")]
#        puts "Date_of_birth: %s" % doc.get("date_of_birth")
#        puts "Date_of_death: %s" % doc.get("date_of_death")
#        puts
#      end
#    end
#  end

#  def run_people(name1, name2)
#    index = create_index
#    searcher = IndexSearcher.new(index)

#    search(searcher, name1)
#    search(searcher, name2)
#    searcher.close
#  end
#end