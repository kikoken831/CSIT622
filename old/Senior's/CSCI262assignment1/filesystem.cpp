/* * * * * * * * * * * * * * * * * * * *
*	Name		: Pang Jin Xiong Joel  *
*	Student ID	: 4643409              *
* * * * * * * * * * * * * * * * * * * */
#include "filesystem.h"
#include "mysmalllib.h"
#include <iomanip>

using namespace std;

filesystem::filesystem()
{
	init();
}

void filesystem::createFile(string owner)
{
	string name;
	char l = 57;
	cout << "\nEnter a filename: ";
	getline(cin, name);

	while (true)
	{
		cout << "Choose a security level for this file(0, 1 or 2): "; 
		cin.get(l);
		cin.ignore(256, '\n');
		if (l < 48 || l > 50)
		{
			cout << "Invalid clearence level.\n" << endl;
		}
		else break;
	}
	if (findFile(name) == files.size())
	{
		file f(name, ((int)l - (int)48), owner);
		files.push_back(f);
	}
	else
	{
		cout << "\nFile already exist.\n" << endl;
	}
}

void filesystem::listHolding()
{
	cout << endl;
	cout << std::setw(5) << right << " " << std::setw(20) << left << "File name"
		<< std::setw(20) << left << "Clearence"
		<< std::setw(20) << left << "Creator" << endl;
	for (int i = 0; i < records; i++)
	{
		cout << (char)(i + 49) << std::setw(5) << left << ". " << std::setw(20) << left << files.at(i).getName() << std::setw(20) << left << files.at(i).getLevel() << std::setw(20) << left << files.at(i).getOwner() << endl;
	}
	for (int i = records; i < files.size(); i++)
	{
		cout << (char)(i + 49) << std::setw(5) << left << ". " << std::setw(20) << left << files.at(i).getName() << std::setw(20) << left << files.at(i).getLevel() << std::setw(20) << left << files.at(i).getOwner() << std::setw(20) << left << " *" << endl;
	}
	if (records != files.size())
		cout << "* represents files that have not been saved." << endl;
}

void filesystem::listRecords()
{
	cout << endl;
	cout << "    File name\tClearence\tCreator" << endl;
	for (int i = 0; i < records; i++)
	{
		cout << (char)(i + 49) << ". " << files.at(i).getName() << '\t' << files.at(i).getLevel() << '\t' << files.at(i).getOwner() << endl;
	}
}

void filesystem::fileRead(int level)
{
	string name = "";
	cout << "\nFile name? ";
	getline(cin, name);
	int ind = findFile(name);
	if (ind < files.size())
	{
		if (level >= files.at(ind).getLevel()) // read down
		{
			cout << "\nSuccess!" << endl;
			cout << "File name: " << files.at(ind).getName() << endl;
			cout << "File level: " << files.at(ind).getLevel() << endl;
			cout << "File owner: " << files.at(ind).getOwner() << '\n' << endl;
		}
		else // read up
		{
			cout << "\nFailure!\nInsufficient clearence level.\n" << endl;
		}
	}
	else
	{
		cout << "\nFile does not exist.\n" << endl;
	}
}

void filesystem::fileWrite(int level)
{
	string name = "";
	cout << "\nFile name? ";
	getline(cin, name);
	int ind = findFile(name);
	if (ind < files.size())
	{
		if (level <= files.at(ind).getLevel()) // write up
		{
			cout << "\nSuccess!" << endl;
			cout << "File name: " << files.at(ind).getName() << endl;
			cout << "File level: " << files.at(ind).getLevel() << endl;
			cout << "File owner: " << files.at(ind).getOwner()  << '\n' << endl;
		}
		else // write down
		{
			cout << "\nFailure!\nInsufficient clearence level.\n" << endl;
		}
	}
	else
	{
		cout << "\nFile does not exist.\n" << endl;
	}
}

bool filesystem::saveFiles()
{
	string text = "";
	if (records != files.size())
	{
		cout << "Saving......";
		for (int i = 0; i < files.size() - 1; i++)
		{
			text.append(files.at(i).getName() + ':' + (char)(files.at(i).getLevel() + 48) + ':' + files.at(i).getOwner() + '\n');
		}
		text.append(files.at(files.size() - 1).getName() + ':' + (char)(files.at(files.size() - 1).getLevel() + 48) + ':' + files.at(files.size() - 1).getOwner());

		if (mylib::writeFile("Files.store", text))
		{
			records = files.size();
			cout << "Success!" << endl;
			return true;
		}
		else
		{
			cout << "Failed!\nPlease check permissions for Files.store." << endl;
			return false;
		}
	}
	else
	{
		cout << "No new files was added." << endl;
		return false;
	}
}

void filesystem::init()
{
	vector<string> lines = mylib::readFile("Files.store");
	for (int i = 0; i < lines.size(); i++)
	{
		vector<string> delim = mylib::delimString(lines.at(i), ':');
		file f(delim.at(0), atoi(delim.at(1).c_str()), delim.at(2));
		files.push_back(f);
	}
	records = files.size();
}

int filesystem::findFile(string name)
{//todo: use if else to increment i
	int i = 0;
	while (i < files.size())
	{
		if (strcmp(mylib::tolower(files.at(i).getName()).c_str(), mylib::tolower(name).c_str()) == 0)
			break;
		else
			i++;
	}
	return i;
}