Kendrick Kee
7366814

This is a python application that performs a rainbow table attack


#must have python3 installed and be able to run from command cli using the 'python3' prefix
#the terminal must be in the directory where the file and python script is located
#to run the program use the following command
python3 Rainbow.py <input file name>

for example:
                python3 Rainbow.py ./Passwords.txt


Sample output:


    Number of lines in file:  25143
    Rainbow table created
    Number of lines in rainbow table:  0
    Loading rainbow table
    Rainbow table loaded
    Number of lines in rainbow table:  13165
    Please enter a hash value: 112342134
    No hash match could be found