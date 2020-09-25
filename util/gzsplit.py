grep -obUaP "\x1F\x8B\x08\x00\x00\x00\x00\x00" $FILENAME | cut -d ":" -f 1 > splits.txt

cat > split.py << EOF
import sys
input_file = sys.argv[1]
offset_file = sys.argv[2]
output_file = input_file.replace('.gz','')
offsets = map(long, open(offset_file).read().rstrip().split("\n"))

with open(input_file,"rb") as f:
    part = 1
    previous = 0
    for offset in offsets:
        print offset
        if offset == 0:
          continue

        file_size = offset - previous
        print file_size
        byte_data = f.read(file_size)
        print f.tell

        with open('splits/part-'+str(part)+'-of-'+str(len(offsets))+'.gz', 'wb') as o:
            o.write(byte_data)
        previous = offset
        part += 1
EOF

mkdir splits
python split.py $FILENAME splits.txt