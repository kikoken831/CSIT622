# purpose of this program is to perform a rainbow table
# attack on a list of passwords

import hashlib
import sys

lengthOfList = 0
rainbow_table = []
wordList = []
# read in a txt file


def readInFile():
    global lengthOfList
    wordList = []
    # open the file
    file = open(sys.argv[1], "r")
    # read the file and get count of lines
    for line in file.readlines():
        lengthOfList += 1
        # strip new line char and append to word list as unused
        wordList.append([line.strip('\n'), False])
    # close the file
    print("Number of lines in file: ", lengthOfList)
    file.close()
    return wordList

# takes a list of words and performs hash chaining
def hashChain(wordList):
    global rainbow_table
    # loop through the list
    for word in wordList:
        hashList = []
        if(word[1] == False):
            # set the word to used
            word[1] = True
            # get the password
            password = word[0]
            # hash the password
            password = hashPassword(password)

            # loop 4 times
            for i in range(4):
                # reduce the password
                reducedValue = reducePassword(password)
                # if the reduced value as indexed in wordList is not used
                if wordList[reducedValue - 1][1] == False:
                    # set the word to used
                    wordList[reducedValue - 1][1] = True
                    # get the password
                    password = wordList[reducedValue - 1][0]
                    # hash the password
                    password = hashPassword(password)
                else:
                    # get the password
                    password = wordList[reducedValue - 1][0]
                    # hash the password
                    password = hashPassword(password)
            # append the password and the word to the rainbow table
            rainbow_table.append([password, word[0]])
    # Sort rainbow table by hash values.
    rainbow_table = sorted(rainbow_table, key=lambda x: x[0])
    # write the rainbow table to a file
    writeRainbowTable(rainbow_table)
    #clear the rainbow table
    rainbow_table = []
# function that writes the rainbow table to a file


def writeRainbowTable(rainbow_table):
    # open the file
    file = open("./Rainbow.txt", "w")
    # loop through the rainbow table
    for line in rainbow_table:
        # write the line to the file
        file.write(line[0] + " " + line[1] + "\n")
    # close the file
    file.close()

# function that hashes the password with md5


def hashPassword(password):
    # encode the password
    encodedPassword = bytes(password, encoding='utf-8')
    # hash the password
    encodedPassword = hashlib.md5(encodedPassword)
    # return the hashed password
    return (encodedPassword.hexdigest())
# function that reduces the password in hexadecimal to number


def reducePassword(password):
    # append 0x to start of string to represent a hex value
    password = "0x" + password
    # convert the password to an int
    password = int(password, 16)
    # modulo the password by the length of the list and increment by 1
    password = (password % lengthOfList) + 1
    return password

# function that searches the rainbow table for a hash
def searchRainbowTable(userInput):
    # loop through the rainbow table
    for line in rainbow_table:
        # if the hash is found
        if(line[0] == userInput):
            return line[1]
    # if the hash is not found
    return False

# function to load the rainbow table from a file
def loadRainbowTable():
    global rainbow_table
    print("Loading rainbow table")
    # open the file
    file = open("./Rainbow.txt", "r")
    # read the file
    for line in file.readlines():
        # strip the new line char
        line = line.strip('\n')
        # split the line by the space
        line = line.split(" ")
        # append the line to the rainbow table
        rainbow_table.append(line)
    # close the file
    file.close()
    print("Rainbow table loaded")
    print("Number of lines in rainbow table: ", len(rainbow_table))

##########MAIN METHOD#############
# main method for the program

def main():
    # print the command line arguments
    # print("File to be read on system: ", sys.argv[1])
    # read in the file
    wordList = readInFile()
    hashChain(wordList)
    print("Rainbow table created")
    print("Number of lines in rainbow table: ", len(rainbow_table))
    # load the rainbow table
    loadRainbowTable()
    # prompt user to enter a hash
    # get the user input

    userInput = input("Please enter a hash value: ")
    userInput = hashPassword(userInput)
    for i in range(len(rainbow_table)):
        if rainbow_table[i][0] == userInput:
             for i in range(4):
                word = reducePassword(rainbow_table[i][0])
                word = wordList[word-1][0]
                if hashPassword(word) == userInput:
                    print(word)
                    exit()

    #hash is not in whole rainbow table
    for i in range(1000):
        newCandidate = reducePassword(userInput)
        newCandidate = wordList[newCandidate-1][0]
        for i in range(len(rainbow_table)):
            if rainbow_table[i][0] == newCandidate:
                for i in range(4):
                    word = reducePassword(rainbow_table[i][0])
                    word = wordList[word-1][0]
                    if hashPassword(word) == userInput:
                        print(word)
                        exit()
        userInput = hashPassword(newCandidate)

    print("No hash match could be found")


#function to find a reduced hash that is found in the rainbow table
def searchRainbowTable(hash):
    for line in rainbow_table:
        if hash == line[0]:
            return (True, line)
    return (False, None)



if __name__ == "__main__":
    main()
