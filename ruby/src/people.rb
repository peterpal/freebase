require 'java'
require 'result'
require 'person'
# You either use the next line by require the JAR file, or you pass
# the -r flag to JRuby as follows:
# jruby -r /path/to/lucene-core-3.0.1.jar inmemory.rb
# require "lucene-core-3.0.1.jar"

java_import org.apache.lucene.analysis.standard.StandardAnalyzer
java_import org.apache.lucene.document.Document
java_import org.apache.lucene.document.Field
java_import org.apache.lucene.index.IndexWriter
java_import org.apache.lucene.queryParser.ParseException
java_import org.apache.lucene.queryParser.QueryParser
java_import org.apache.lucene.store.RAMDirectory
java_import org.apache.lucene.util.Version

java_import org.apache.lucene.search.IndexSearcher
java_import org.apache.lucene.search.TopScoreDocCollector

class People

  def create_document(id, name, date_of_birth, date_of_death)
    doc = Document.new
    doc.add Field.new("id", id, Field::Store::YES, Field::Index::ANALYZED)
    doc.add Field.new("name", name, Field::Store::YES, Field::Index::ANALYZED)
    doc.add Field.new("date_of_birth", date_of_birth, Field::Store::YES, Field::Index::NO)
    doc.add Field.new("date_of_death", date_of_death, Field::Store::YES, Field::Index::NO)
    doc
  end

  def create_index
    idx     = RAMDirectory.new
    writer  = IndexWriter.new(idx, StandardAnalyzer.new(Version::LUCENE_30), IndexWriter::MaxFieldLength::LIMITED)
    File.open('../../data/data.txt', 'r') do |f1|
      while line = f1.gets
        line = line.strip.split(";")
        writer.add_document(create_document(line[0], line[1], line[2], line[3]))
      end
    end

    writer.optimize
    writer.close
    idx
  end

  def search(searcher, query_string, some_array)
    parser = QueryParser.new(Version::LUCENE_30, "name", StandardAnalyzer.new(Version::LUCENE_30))
    query = parser.parse(query_string)

    hits_per_page = 5

    collector = TopScoreDocCollector.create(1 * hits_per_page, false)
    searcher.search(query, collector)

    # Notice how this differs from the Java version: JRuby automagically translates
    # underscore_case_methods into CamelCaseMethods, but scoreDocs is not a method:
    # it's a field. That's why we have to use CamelCase here, otherwise JRuby would
    # complain that score_docs is an undefined method.
    hits = collector.top_docs.scoreDocs

    hit_count = collector.get_total_hits

    File.open('..//..//data//result.txt', 'a') do |f1|
      if hit_count.zero?
        f1.puts "No matching documents."
      else
        f1.puts "%d total matching documents" % hit_count

        f1.puts "Hits for %s were found in quotes by:" % query_string

        hits.each_with_index do |score_doc, i|
          doc_id = score_doc.doc
          doc_score = score_doc.score

          f1.puts "doc_id: %s \t score: %s" % [doc_id, doc_score]

          doc = searcher.doc(doc_id)
          f1.puts "%d. %s" % [i, doc.get("name")]
          f1.puts "Date_of_birth: %s" % doc.get("date_of_birth")
          f1.puts "Date_of_death: %s" % doc.get("date_of_death")
          f1.puts

          p = Person.new
          p.name = doc.get("name")
          p.date_of_birth = doc.get("date_of_birth")
          p.date_of_death = doc.get("date_of_death")

          r = Result.new
          r.score = doc_score
          r.person = p
          some_array.push(r)
        end
      end
    end
  end

  def find_overlap (person1, person2)
    line = person1.get_person.get_date_of_birth

    numbs = line.strip.split("-")
    year1 = numbs[0]
    mounth1 = numbs[1]
    day1 = numbs[2]

    line = person1.get_person.get_date_of_death

    numbs = line.strip.split("-")
    year12 = numbs[0]
    mounth12 = numbs[1]
    day12 = numbs[2]

    line = person2.get_person.get_date_of_birth

    numbs = line.strip.split("-")
    year2 = numbs[0]
    mounth2 = numbs[1]
    day2 = numbs[2]

    line = person2.get_person.get_date_of_death

    numbs = line.strip.split("-")
    year22 = numbs[0]
    mounth22 = numbs[1]
    day22 = numbs[2]

    File.open('..//..//data//result.txt', 'a') do |f1|
      f1.puts "\nOsoby:\n"
      f1.puts"#{person1.get_score};#{person1.get_person.get_name};#{person1.get_person.get_date_of_birth};#{person1.get_person.get_date_of_death}\n"
      f1.puts"#{person2.get_score};#{person2.get_person.get_name};#{person2.get_person.get_date_of_birth};#{person2.get_person.get_date_of_death}\n"
      if ((year1 < year2 && year2 < year12) || (year2 < year1 && year1 < year22))
        f1.puts "Mohli sa stretnut\n"
      elsif ((year1 < year22 && year22 < year12) || (year2 < year12 && year12 < year22))
        f1.puts "Mohli sa stretnut\n"
      elsif (year12 < year2 || year22 < year1)
        f1.puts "Nemohli sa stretnut\n"
      elsif (year1 == year22)
        if (mounth1 != nil && mounth22 != nil)
          if (mounth1 < mounth22 || ((mounth1 == mounth22) && (day1 != nil && day22 != nil) && (day1 <= day22)))
            f1.puts "Mohli sa stretnut\n"
          elsif (mounth1 == mounth22 && day1 == nil && day22 == nil)
            f1.puts "Rovnaky rok a mesiac narodenia a umrtia osob, ale chybaju dni\n"
          else
            f1.puts "Nemohli sa stretnut\n"
          end
        else
          f1.puts "Rovnaky rok narodenia a umrtia osob, ale chybaju dni a mesiace\n"
        end
      elsif (year2 == year12)
        if (mounth2 != nil && mounth12 != nil)
          if (mounth2 < mounth12 || ((mounth2 == mounth12) && (day2 != nil && day12 != nil) && (day2 <= day12)))
            f1.puts "Mohli sa stretnut\n"
          elsif (mounth2 == mounth12 && day2 == nil && day12 == nil)
            f1.puts "Rovnaky rok a mesiac narodenia a umrtia osob, ale chybaju dni\n"
          else
            f1.puts "Nemohli sa stretnut\n"
          end
        else
          f1.puts "Rovnaky rok narodenia a umrtia osob, ale chybaju dni a mesiace\n"
        end
      end
    end
  end

  def run_people(name1, name2)
    index = create_index
    searcher = IndexSearcher.new(index)

    people_based_on_name1 = Array.new
    people_based_on_name2 = Array.new

    search(searcher, name1, people_based_on_name1)
    search(searcher, name2, people_based_on_name2)
    searcher.close

    if (people_based_on_name1.length != 0 && people_based_on_name2.length != 0)
      people_based_on_name1.each do |person1|
        people_based_on_name2.each do |person2|
          find_overlap(person1, person2)
        end
      end
    else
      puts "Nenasli sa ziadne data, ktore mozno porovnat\n"
    end
  end
end