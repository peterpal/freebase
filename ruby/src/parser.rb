require '../../ruby/src/person'
require 'date'
require 'zlib'

class Parser
  @hash_person

def get_hash_person
  return @hash_person
end

def Parse

  @hash_person = Hash.new

  File.open('../../data/sample_date_of_birth.gz', 'r') do |f1|
    processed = 0
    skipped = 0
    while line = f1.gets
    #def raise_and_rescue
      begin
        p = Person.new
        id = line.match(/(m\.[[:alnum:]_]+)/)[1]
        date = line.match(/"([[:digit:]-]+)"/)[1]
        p.id = id
        p.date_of_birth = date
        @hash_person[id] = p
        processed = processed+1
      rescue Exception => e
        skipped = skipped+1
        puts e.message
        puts processed
        puts skipped
        puts "***********************"
      end
    #end
    #raise_and_rescue
    end
  end

  processed = 0
  skipped = 0

  File.open('../../data/sample_date_of_death.gz', 'r') do |f1|
    while line = f1.gets
      #def raise_and_rescue
        begin
          id = line.match(/(m\.[[:alnum:]_]+)/)[1]
          date = line.match(/"([[:digit:]-]+)"/)[1]

          if (@hash_person[id] != nil)
            @hash_person[id].date_of_death = date
          end
          processed = processed+1
        rescue Exception => e
          skipped = skipped+1
          puts e.message
          puts processed
          puts skipped
          puts "***********************"
        end
      #end
      #raise_and_rescue
    end
  end

  processed = 0
  skipped = 0

  File.open('../../data/sample_names.gz', 'r') do |f1|
    while line = f1.gets
      #def raise_and_rescue
        begin
          id = line.match(/(m\.[[:alnum:]_]+)/)[1]
          name = line.match(/"([[:print:]]+)"/)[1]

          if (@hash_person[id] != nil)
            @hash_person[id].name = name
          end
          processed = processed+1
        rescue Exception => e
          skipped = skipped+1
          puts e.message
          puts processed
          puts skipped
          puts "***********************"
        end
      #end
      #raise_and_rescue
    end
  end

  counter = 0
  File.open('../../data/data.txt', 'w') do |f2|
    @hash_person.each do |k,v|
      f2.puts "#{@hash_person[k].get_id};#{@hash_person[k].get_name};#{@hash_person[k].get_date_of_birth};#{@hash_person[k].get_date_of_death}\n"
      counter = counter+1
        if (counter % 100000 == 0)
          puts"Id=#{@hash_person[k].get_id};Name=#{@hash_person[k].get_name};DateOfBirth=#{@hash_person[k].get_date_of_birth};DateOfDeath=#{@hash_person[k].get_date_of_death}\n"
        end
      end
    end
end
end
