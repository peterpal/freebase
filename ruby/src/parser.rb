require '../code/person'
require 'date'

class Parser
  @hash_person

def get_hash_person
  return @hash_person
end

def Parse

  @hash_person = Hash.new

  p = Person.new

  File.open('sample-date_of_birth.txt', 'r') do |f1|
    while line = f1.gets
      id = line.match(/(m\.[[:alnum:]_]+)/)[1]
      #id = line.match(/m\.([[:alnum:]_]+)/)
      date = line.match(/"([[:digit:]-]+)"/)[1]
      p.id = id
      puts date.inspect
      p.date_of_birth = date
      @hash_person[id] = p
      #@hashBirth[id] = date
      #@hashId[id] = id
    end
  end

  File.open('sample-date_of_death.txt', 'r') do |f1|
    while line = f1.gets
      id = line.match(/(m\.[[:alnum:]_]+)/)[1]
      #id = line.match(/m\.([[:alnum:]_]+)/)
      date = line.match(/"([[:digit:]-]+)"/)[1]
      p.id = id
      p.date_of_death = date
      @hash_person[id] = p
      #@hashDeath[id] = date
      #@hashId[id] = id
    end
  end

  File.open('sample-names.txt', 'r') do |f1|
    while line = f1.gets
      id = line.match(/(m\.[[:alnum:]_]+)/)[1]
      #id = line.match(/m\.([[:alnum:]_]+)/)
      name = line.match(/"([[:print:]]+)"/)[1]
      p.id = id
      p.name = name
      @hash_person[id] = p
      #@hashName[id] = name
      #@hashId[id] = id
    end
  end

  @hash_person.each do |k,v|
    # todo dorobit do suboru...

  end
end
end
