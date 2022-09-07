/* * * * * * * * * * * * * * * * * * * *
 *	Name		: Pang Jin Xiong Joel  *
 *	Student ID	: 4643409              *
 * * * * * * * * * * * * * * * * * * * */
// main.cpp : Defines the entry point for the console application.

#include "stdafx.h"
#include "md5.h"
#include "user.h"
#include "mysmalllib.h"
#include "filesystem.h"
using namespace std;

bool genSecret(string &secret)
{
	secret = mylib::readFileLine("secret.txt");
	if (strcmp(secret.c_str(), "") != 0) //if no the same
	{
		return true; // no need to generate, we got the secret
	}
	else // 16 characters, from 33 to 126 for secret
	{
		char choice = 0;
		string ss;
		while (!(choice == 'y' || choice == 'Y' || choice == 'n' || choice == 'N'))
		{
			cout << "No secret key was found. Do you want it to be auto generated? (Y)es or (N)o ";
			cin.get(choice);
			cin.ignore(256, '\n');
		}
		if (choice == 'y' || choice == 'Y')
		{
			srand(time(NULL));
			for (int i = 0; i < 16; i++)
				ss.push_back((char)(rand() % 94 + 33));
		}
		else
		{
			ss = mylib::getpass("Enter secret key: ", true);
		}
		cout << "Saving secret...";
		if (mylib::writeFile("secret.txt", ss))
		{
			cout << "Success!" << endl;
			secret = ss;
			return true;
		}
		else
		{
			cout << "Fail!" << endl;
			return false;
		}
	}
}

bool exit()
{
	char choice = 0;
	while (choice != 'Y' || choice != 'y' || choice != 'N' || choice != 'n')
	{
		cout << "\nShut down the FileSystem? (Y)es or (N)o ";
		cin.get(choice);
		cin.ignore(256, '\n');
		switch (choice)
		{
		case 'Y':
		case 'y':
			return true;
			break;
		case 'N':
		case 'n':
			return false;
			break;
		default:
			cout << "Invalid choice. Please re-enter your choice." << endl;
			break;
		}
	}
}

int main(int argc, char* argv[])
{
	cout << "MD5(This is a test) = " + md5("This is a test") << endl;
	string secret = "";
	if (genSecret(secret))
	{
		user u;
		if (argc == 1)
		{
			filesystem fs;
			if (u.loginUser())
			{
				bool quit = false;
				while (!quit)
				{
					cout << "\nOptions: (C)reate, (R)ead, (W)rite, (L)ist, (S)ave or (E)xit." << endl;
					char choice;
					cin.get(choice);
					cin.ignore(256, '\n');
					switch (choice)
					{
					case 'C':
					case 'c':
						fs.createFile(u.getName());
						break;
					case 'R':
					case 'r':
						fs.fileRead(u.getClearence());
						break;
					case 'W':
					case 'w':
						fs.fileWrite(u.getClearence());
						break;
					case 'L':
					case 'l':
						fs.listHolding();
						break;
					case 'S':
					case 's':
						fs.saveFiles();
						break;
					case 'E':
					case 'e':
						if (exit())
						{
							fs.listHolding();
							quit = true;
						}
						break;
					default:
						cout << "Invalid option chosen." << endl;
						break;
					}
				}
			}
		}
		else if ((argc == 2) && strcmp(argv[1], "-i") == 0)
		{
			u.saveUser(secret);
		}
		else
			cout << "Invalid params." << endl;
	}
	/*cout << "Press enter to exit...";
	cin.get();*/
	return 0;
}

