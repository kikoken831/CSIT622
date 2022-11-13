Name: Kendrick Kee
UOW ID: 7366814

CSCI262 A3

To run the program:
java IDS <Events file name> <Stats file name> <No of days>
example:
java IDS Events.txt Stats.txt 10

the only user input needed during the program is for the file name of the live data stats generation.
the prompt will look like this:

"Enter 'q' to quit or Enter <List Ship.Stats File path> <No of days> (i.e Ship.Stats.txt 5):"

the input requires you to input a file name and number of days for generation as such:
<File name for live stats> <No of days>
example:
AnomalyStats.txt 10

Do note that the file should be in the same directory upon running the program to prevent fileNotFoundException

If a recompilation is needed.
cd to the directory with the java files and run:
javac *.java