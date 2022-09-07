Name: Whitney Chng
UOW ID: 6956865

The python application is built using Visual Studio Code

*** Code Instructions:
Please ensure that Wordlist.txt is in the same source folder as the executable file.
Run and Debug the file and the code should run.

*** Extra Info:
If you are opening another .txt file, please edit line 11 "txtFile = open("Wordlist.txt")"
to the new file name.

This code is based on rainbow table.
In rainbow.txt, the first index of each list item is the original word from wordlist.txt and the second index is the reduction and hashed word
The reduction function works by removing all alpha characters, and then multiplying the result with the modulo of the length of password list by 32.

