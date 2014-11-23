require 'test/unit'

class PeopleTest < Test::Unit::TestCase
  def test_simple
    firstPerson = Person.new
    secondPerson = Person.new
    assert_equal(true, firstPerson, secondPerson)
    assert_equal(true, firstPerson, firstPerson)
    assert_equal(true, secondPerson, secondPerson)
  end
end