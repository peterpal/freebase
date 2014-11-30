require 'test/spec'
require '../../ruby/src/parser'

context 'Parser' do

  specify 'should parse person data to small txt file' do
    fixture = '../../data/people.txt'
    p = Parse.new
    p.path_birth = '../../data/sample_date_of_birth.txt'
    p.path_death = '../../data/sample_date_of_death.txt'
    p.path_names = '../../data/sample_names.txt.'
    p.Parse
    output = '../../data/data.txt'
    expect(output).to be_eql File.read(fixture)
  end
end