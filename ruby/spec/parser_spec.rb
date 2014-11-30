# To run this test you need to install rspec gem (gem install rspec).
# Then run command rspec path_to_rspec_file/name
# In console opened in spec directory run rspec parser_spec.rb

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