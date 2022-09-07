Using the console program:
You can enter your secret key into secret.txt or have the program randomly generate it.

During creation of the user, password requires a length between 8-16 characters and contains atleast one numeric.

To compile: 
g++ -std=c++11 filesystem.cpp main.cpp md5.cpp mysmalllib.cpp user.cpp -o FileSystem.exe

usable in mingw for windows or terminal in linux