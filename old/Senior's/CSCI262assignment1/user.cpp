/* * * * * * * * * * * * * * * * * * * *
*	Name		: Pang Jin Xiong Joel  *
*	Student ID	: 4643409              *
* * * * * * * * * * * * * * * * * * * */
#include "user.h"
#include "md5.h"
#include "mysmalllib.h"

using namespace std;

//public
bool user::loginUser()
{
	string password = "";
	cout << "Username: ";
	getline(cin, name);
	password = mylib::getpass("Password: ", true);

	if (!retrieveSalt())
	{
		cout << "\nAuthentication Failed." << endl;
		return false;
	}
	else
	{
		cout << name << " was found in salt.txt." << endl;
		cout << "Salt retrieved: " << salt << endl;
		cout << "hashing..." << endl;
		cout << "hash value: " << md5(password + salt) << endl;
		if (comparePSalt(md5(password + salt)))
		{
			cout << "Authentication for user " << name << " complete." << endl;
			cout << "The clearence level for " << name << " is " << (char)(clearence + 48) << '.' << endl;
			return true;
		}
		else
		{
			cout << "\nAuthentication Failed." << endl;
			return false;
		}
	}
}
//public
bool user::saveUser(string secret)
{
	string password;
	string temp = "1";
	cout << "Enter a username...: ";
	getline(cin, name);

	if (!retrieveSalt())
	{
		genSalt(secret);

		cout << "\nPassword length should be between 8-16 characters long and contain at least one numeric.\n"
			 << "Symbols available for use are ._@-" << endl;
		while (true)
		{
			password = mylib::getpass("Enter a password...: ", true);
			temp = mylib::getpass("Confirm password...: ", true);
			if (strcmp(password.c_str(), temp.c_str()) != 0)
				cout << "Passwords does not match." << endl;
			else if (password.length() < 8 || password.length() > 16)
				cout << "Password length should be between 8-16 characters long." << endl;
			else if (password.find_first_not_of(mylib::validCharacters.c_str()) != string::npos)
				cout << "Valid characters are A-Z, a-z, 0-9 and _.@-" << endl;
			else if (password.find_first_of("0123456789") == string::npos)
				cout << "Require the use of at least one numeric." << endl;
			else
			{
				break;
			}
		}
		saltPassword = md5(password + salt);

		char clear = 57;
		while (true)
		{
			cout << "\nChoose a clearence level 0, 1 or 2...: ";
			cin.get(clear);
			cin.ignore(256, '\n');
			if (clear < 48 || clear > 50)
				cout << "Invalid clearence level." << endl;
			else
			{
				clearence = (int)clear - (int)48;
				break;
			}
		}

		if (findUser("salt.txt", name) == "")
			return (mylib::writeFile("salt.txt", (name + ":" + salt + "\n"), ios::app)
			+ mylib::writeFile("shadow.txt", (name + ":" + saltPassword + ":" + (char)(clearence + 48) + "\n"), ios::app));
	}
	else
	{
		cout << "User already exist. Terminating program." << endl;
	}
	return false;
}

//private
string user::genSalt(string text)
{
	srand(mylib::Hash(text) ^ time(NULL));
	stringstream temp;
	rand();
	for (int i = 0; i < 8; i++)
		temp << rand() % 10;
	/*cout << "Example salt: " << temp.str() << endl;*/
	salt = temp.str();
	return salt;
}
//private
bool user::retrieveSalt()
{
	string temp = findUser("salt.txt", name);
	if (temp != "")
	{
		salt = mylib::delimString(temp, ':').at(1);
		return true;
	}
	else
		return false;
}
//private
bool user::comparePSalt(string psalt)
{
	string temp = findUser("shadow.txt", name);
	if (temp != "")
	{
		if (strcmp(mylib::delimString(temp, ':').at(1).c_str(), psalt.c_str()) == 0)
		{
			saltPassword = psalt;
			clearence = atoi(mylib::delimString(temp, ':').at(2).c_str());
			return true;
		}
		else
			return false;
	}
	else
		return false;
}
//private
string user::findUser(string filename, string uname)
{
	string text = "";
	ifstream infile;
	infile.open(filename.c_str());
	if (infile.is_open())
	{
		string line;
		while (getline(infile, line))
		{
			if (strcmp(mylib::tolower(mylib::delimString(line, ':').at(0)).c_str(), mylib::tolower(uname).c_str()) == 0)
			{
				text = line;
				break;
			}
		}
		infile.close();
		/*if (text == "")
		{
			cout << "User not found." << endl;
		}*/
	}
	else
	{
		mylib::writeFile(filename, "");
	}
	return text;
}