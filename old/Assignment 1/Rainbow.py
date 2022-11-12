import hashlib
from os import read
import re

rainbow_table = []
pw_list_len = 0


def read_In_Wordlist():
    global pw_list_len
    # Read in Wordlist.txt
    wordlist = open("./tutorial.txt")
    pw_list = []
    for line in wordlist:
        # Remove newline from file
        pw_list.append([line.strip('\n'), False])
    print(f'Words read in: {len(pw_list)}')
    pw_list_len = len(pw_list)
    return pw_list


def write_To_Rainbow_Table(rainbow_table):
    empty_Rainbow_Table = open('Rainbow.txt', 'w')
    for line in rainbow_table:
        # Write to rainbowtable.txt
        empty_Rainbow_Table.write(f'{line[0]} {line[1]}\n')
    empty_Rainbow_Table.close
    print(f'Wrote {len(rainbow_table)} lines to Rainbow.txt')


def reduction(reduction_str):
    global pw_list_len
    # Remove a-z characters
    reduction_str = re.sub(r'[a-zA-Z]', "", reduction_str)
    # Password length modulo 32
    reduction_str = (int(reduction_str) * (pw_list_len % 32))
    reduction_str = str(reduction_str)
    # Return first 6 digits of password
    return str(reduction_str[:6])


def hash_String(password):
    encoded_str = bytes(password, encoding='utf-8')
    encoded_str = hashlib.md5(encoded_str)
    return (encoded_str.hexdigest())


def reduce_And_Hash_Str(reduced_hashed_str):
    reduced_hashed_str = reduction(reduced_hashed_str)
    return hash_String(reduced_hashed_str)


wordlist = read_In_Wordlist()

for word in wordlist:
    word[1] = True
    password = word[0]
    # Hash password
    password = hash_String(password)
    for i in range(4):
        # Reduce hash, repeat 4 times
        password = reduce_And_Hash_Str(password)
    rainbow_table.append([word[0], password])

# Sort rainbow table by hash values.
rainbow_table = sorted(rainbow_table, key=lambda x: x[1])
write_To_Rainbow_Table(rainbow_table)

# Check for hash in rainbow table
def check_Rainbow_Table(hash):
    for line in rainbow_table:
        if hash == line[1]:
            return (True, line)
    return (False, None)


# Pass hashed password
def find_Hash(password):
    count = 0
    while (count <= 1000):
        password = reduction(password)
        password = hash_String(password)
        count += 1
        result = check_Rainbow_Table(password)
        if (result[0]):
            # Return password if found
            return result[1][0]
    print(f'No matching hashes were found. Exiting...')
    exit()


def match_Word_To_Hash(word, hash):
    count = 0
    word = hash_String(word)
    while(word != hash):
        word = reduce_And_Hash_Str(word)
        count += 1
        if count > 1000:
            print("ERROR: No word could be matched with this hash.")
            exit()
    return True


guessed_hash = ''
while(True):
    guessed_hash = input("Enter hash value: ")
    if(len(guessed_hash) == 32):
        break
    else:
        print('ERROR: Invalid hash length!')

result = check_Rainbow_Table(guessed_hash)

if(result[0]):
    if(match_Word_To_Hash(result[1][0], guessed_hash)):
        print(f'Hash identified, password is {result[1][0]}.')
else:
    word = find_Hash(guessed_hash)
    if(match_Word_To_Hash(word, guessed_hash)):
        print(f'Hash identified, password is {word}.')
