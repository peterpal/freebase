require '../../ruby/src/person'
require 'date'
require 'zlib'

class Parser
  @hash_person

  def path_birth= path_birth
    @path_birth = path_birth
  end

  def path_death= path_death
    @path_death = path_death
  end

  def path_names= path_names
    @path_names = path_names
  end

def get_hash_person
  return @hash_person
end

def Parse
# If you want to get statistics, uncomment this rows:
#  File.open('../../data/statistics.txt', 'a') do |f1|
#    f1.puts "Program started at: "
#    f1.puts Time.now
#    f1.puts "******\n"
#  end

  @hash_person = Hash.new

  processed = 0
  skipped = 0
  line_counter = 0

  # Dates of birth are processed first.
  File.open(@path_birth, 'r') do |f1|
    while line = f1.gets
    #def raise_and_rescue
      begin
        line_counter = line_counter + 1
        p = Person.new
        id = line.match(/(m\.[[:alnum:]_]+)/)[1]  # id like m.sth is returned
        date = line.match(/"([[:digit:]-]+)"/)[1] # date like 2014-05-15 between signes "" is returned
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

  # If you want to get statistics, uncomment this rows:
  #File.open('../../data/statistics.txt', 'a') do |f1|
  #  f1.puts "\nFile Date_of_birth processed:\n"
  #  f1.puts Time.now
  #  f1.puts "Numbers (lines, processed, skipped)"
  #  f1.puts line_counter
  #  f1.puts processed
  #  f1.puts skipped
  #  f1.puts "******\n"
  #end

  processed = 0
  skipped = 0
  line_counter = 0
  missing = 0

  # Dates of death are processed and mapped to people with date of birth, others are skipped.
  File.open(@path_death, 'r') do |f1|
    while line = f1.gets
      #def raise_and_rescue
        begin
          line_counter = line_counter + 1
          id = line.match(/(m\.[[:alnum:]_]+)/)[1]  # id like m.sth is returned
          date = line.match(/"([[:digit:]-]+)"/)[1] # date like 2014-05-15 between signes "" is returned

          if (@hash_person[id] != nil)
            @hash_person[id].date_of_death = date
          else
            missing = missing + 1
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

  # If you want to get statistics, uncomment this rows:
  #File.open('../../data/statistics.txt', 'a') do |f1|
  #  f1.puts "\nFile Date_of_death processed:\n"
  #  f1.puts Time.now
  #  f1.puts "Numbers (lines, processed, skipped, missing)"
  #  f1.puts line_counter
  #  f1.puts processed
  #  f1.puts skipped
  #  f1.puts missing
  #  f1.puts "******\n"
  #end

  processed = 0
  skipped = 0
  missing = 0
  line_counter = 0

  # Names are processed and mapped to people with date of birth and date of death, others are skipped.
  File.open(@path_names, 'r') do |f1|
    while line = f1.gets
      #def raise_and_rescue
        begin
          line_counter = line_counter + 1
          id = line.match(/(m\.[[:alnum:]_]+)/)[1]  # id like m.sth is returned
          name = line.match(/"([[:print:]]+)"/)[1]  # name like Peter Crowle between signes "" is returned

          if (line_counter % 100000 == 0)
            puts line_counter.inspect
          end
          if (@hash_person[id] != nil)
            @hash_person[id].name = name
          else
            missing = missing + 1
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

  # If you want to get statistics, uncomment this rows:
  #File.open('../../data/statistics.txt', 'a') do |f1|
  #  f1.puts "\nFile Names:\n"
  #  f1.puts Time.now
  #  f1.puts "Numbers (lines, processed, skipped, missing)"
  #  f1.puts line_counter
  #  f1.puts processed
  #  f1.puts skipped
  #  f1.puts missing
  #  f1.puts "******\n"
  #end

  line_counter = 0
  missing = 0

  # Common data for people are created by this code:
  File.open('../../data/sample_output_people.txt', 'w') do |f2|
    @hash_person.each do |k,v|
      line_counter = line_counter+1
      if (line_counter % 100000 == 0)
        puts"Id=#{@hash_person[k].get_id};Name=#{@hash_person[k].get_name};DateOfBirth=#{@hash_person[k].get_date_of_birth};DateOfDeath=#{@hash_person[k].get_date_of_death}\n"
      end
      # If data are not null, they are connected and stored to file data.txt
      if (@hash_person[k].get_id != nil && @hash_person[k].get_name != nil && @hash_person[k].get_date_of_birth != nil && @hash_person[k].get_date_of_death != nil)
        f2.puts "#{@hash_person[k].get_id};#{@hash_person[k].get_name};#{@hash_person[k].get_date_of_birth};#{@hash_person[k].get_date_of_death}\n"
      else
        missing = missing + 1
      end
    end
  end

  # If you want to get statistics, uncomment this rows:
  #File.open('../../data/statistics.txt', 'a') do |f1|
  #  f1.puts "\nFile data.txt created:\n"
  #  f1.puts Time.now
  #  f1.puts "Numbers (lines, missing)"
  #  f1.puts line_counter
  #  f1.puts missing
  #  f1.puts "******\n"
  #end
end
end
