import hashlib
from os import read
import re

pwdListLength = 1
rainbowTable = []

# Read in list of possible pwds + print no. of pwds processed
def processFile():
    global pwdListLength
    txtFile = open("Wordlist.txt")
    list = []
    for word in txtFile:
        #remove '/n' from readIn and append to list
        list.append([word.strip('\n'),False])
    print("No. of words read from file:",len(list))
    pwdListLength = len(list)
    return list

# Reduction Function
def reductionFunc(reduced):
    global pwdListLength
    reduced = re.sub(r'[a-zA-Z]',"",reduced) # remove all alphabetic char
    reduced = (int(reduced) * (pwdListLength % 32)) # modulo by length of passwordList 
    reduced = str(reduced) # convert back to str
    return str(reduced[:6]) # return first six digits

# Hash Function
def hashFunc(hashed):
    #encode var to bytes
    enc = bytes(hashed, encoding='utf-8')
    enc = hashlib.md5(enc)
    return (enc.hexdigest())

# Reduce and Hash Function
def redHashFunc(finalValue):
    finalValue = reductionFunc(finalValue)
    return hashFunc(finalValue)

# generate rainbowTable into txt file
def tableGenerator(rainbowTable):
    txtFile = open('Rainbowtable.txt','w')
    for i in rainbowTable:
        txtFile.write("%s %s \n" % (i[0],i[1]))
    txtFile.close
    print("Rainbowtable.txt consists of %s lines."%(len(rainbowTable)))

passwordList = processFile()

for word in passwordList:
    word[1] = True
    pw = word[0]
    pw = hashFunc(pw)
    for i in range(5):
        pw = redHashFunc(pw)
    rainbowTable.append([word[0],pw])

#sort table by hash values.
rainbowTable = sorted(rainbowTable, key=lambda x:x[1])    
tableGenerator(rainbowTable)

# SECOND STEP
# check if hash value exists in table
def checkHash(hash):
    for value in rainbowTable:
        if hash == value[1]:
            return (True,value)
    return (False,None)

def findHash(hash):
    count = 0
    while (count < 10000):
        hash = reductionFunc(hash)
        hash = hashFunc(hash)
        count += 1
        result = checkHash(hash)
        if (result[0]): 
            #return word if found
            return result[1][0]
    print("Unable to identify hash")
    exit()

# function to check if word matches hash value
def checkWordHash(word,hash):
    count = 0
    word = hashFunc(word)
    while((word!=hash)):
        word = redHashFunc(word)
        count += 1
        if count>10000:
            print ("Unable to match word to hash value")
            exit()
    return True

# user input
hash_input=''

while(True):
    print("\nPlease enter hash value")
    hash_input = input()
    if(len(hash_input) == 32): # check that hash value length is 32
        break
    else:
        print('Hash Value does not exist!')

result = checkHash(hash_input)

if(result[0]):
    if(checkWordHash ( result[1][0], hash_input ) ):
        print("Pre-image of ",hash_input," found! The word is",result[1][0])
else:
    word = findHash(hash_input)
    if(checkWordHash ( word, hash_input ) ):
        print("Pre-image of ",hash_input," The word is", word)