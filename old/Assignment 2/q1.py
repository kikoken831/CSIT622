from itertools import permutations
from itertools import product

s = input("Enter number of sub puzzles: ")
s = int(s)
w = s * pow(2,s)

#Calculates how many unique permutations
def unique_permutations(s, w):
    #List containing range of subPuzzleCount+1
    lst = list(range(1, int(s+1)))
    #Dictionary to hold hash:frequency key:pair values
    uniquePermDict = dict()
    
    #All possible permutations
    for perms in product(lst, repeat=s):
        #If sum of tries == expected hash, add to dict
        #If number of tries is new, set to 1
        if sum(perms) in uniquePermDict:
            uniquePermDict[sum(perms)] += 1
        else:
            uniquePermDict[sum(perms)] = 1

    for key, value in uniquePermDict.items():
        print(key, ':', value)
    
unique_permutations(s,w)