# This class contains personal data: id, name, date_of_birth, date_of_death

class Person

  attr_accessor :age

  def id= id
    @id = id
  end

  def name= name
    @name = name
  end

  def date_of_birth= date_of_birth
    @date_of_birth = date_of_birth
  end

  def date_of_death= date_of_death
    @date_of_death = date_of_death
  end

  def get_date_of_birth
    return @date_of_birth
  end

  def get_name
    return @name
  end

  def get_id
    return @id
  end

  def get_date_of_death
    return @date_of_death
  end
end