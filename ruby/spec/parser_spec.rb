# To run this test you need to install rspec gem (gem install rspec).
# Then you need to prepare 4 files from freebase dump:
#   1. file with triplets, which contains date of birth (ns/people.person.date_of_birth)
#   2. file with triplets, which contains date of death (ns/people.deceased_person.date_of_death)
#   3. file with english names (type.object.name)
#   4. file with people data (id;name;date_of_birth;date_of_death), which has complete data (skip person, who has null data)
# Then run command rspec path_to_rspec_file/name
# In console opened in spec directory run rspec parser_spec.rb
# From files with date of birth, date of death and names are parsed people data and connected to form (id;name;date_of_birth;date_of_death)
# This output is compared to your prepared file with people data, and you get true for same results, false, when an error has occured

require '../../ruby/src/parser'

describe :parser do
  it 'should parse person data to small txt file' do
    input = '../../data/people'
    p = Parser.new
    p.path_birth = '../../data/sample_input_date_of_birth.txt'
    p.path_death = '../../data/sample_input_date_of_death.txt'
    p.path_names = '../../data/sample_input_names.txt'
    p.Parse
    output = '../../data/sample_output_people.txt'
    expect(File.read(output)).to be_eql File.read(input)
  end
end