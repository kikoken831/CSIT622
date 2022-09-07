/* * * * * * * * * * * * * * * * * * * *
*	Name		: Pang Jin Xiong Joel  *
*	Student ID	: 4643409              *
* * * * * * * * * * * * * * * * * * * */
#include "mysmalllib.h"
#include <cctype>
using namespace std;

namespace mylib
{
	bool writeFile(string filename, string text, ios_base::openmode type){
		ofstream outfile;
		outfile.open(filename.c_str(), type);
		if (outfile.is_open())
		{
			outfile << text;
			outfile.close();
			return true;
		}
		return false;
	}

	bool writeFile(string filename, string text)
	{
		return writeFile(filename, text, ios::out);
	}

	vector<string> readFile(string filename)
	{
		vector<string> text;
		ifstream infile;
		infile.open(filename.c_str());
		if (infile.is_open())
		{
			string line;
			while (getline(infile, line))
				text.push_back(line);
			infile.close();
		}
		return text;
	}

	string readFileLine(string filename)
	{
		string text = "";
		ifstream infile;
		infile.open(filename.c_str());
		if (infile.is_open())
		{
			string line;
			getline(infile, line);
			text = line;
			infile.close();
		}
		return text;
	}

	int Hash(string text)
	{
		string temp;
		for (int i = 0; i < text.length(); i++)
			temp.push_back((int)text[i]);
		return atoi(temp.c_str());
	}

	vector<string> delimString(string text, char delim)
	{
		stringstream ss(text);
		vector<string> elem;
		string temp;
		while (getline(ss, temp, delim))
			elem.push_back(temp);
		return elem;
	}

	string tolower(string text)
	{
		string ret = "";
		for (int i = 0; i < text.length(); i++)
			ret += (char)std::tolower(text[i]);
		return ret;
	}

#ifdef WIN32
	void echo(bool on = true)
	{
		DWORD  mode;
		HANDLE hConIn = GetStdHandle(STD_INPUT_HANDLE);
		GetConsoleMode(hConIn, &mode);
		mode = on
			? (mode | ENABLE_ECHO_INPUT)
			: (mode & ~(ENABLE_ECHO_INPUT));
		SetConsoleMode(hConIn, mode);
	}

	string getpass(const char *prompt, bool show_asterisk)
	{
		const char BACKSPACE = 8;
		const char RETURN = 13;

		string password;
		unsigned char ch = 0;

		cout << prompt;

		DWORD con_mode;
		DWORD dwRead;

		HANDLE hIn = GetStdHandle(STD_INPUT_HANDLE);

		GetConsoleMode(hIn, &con_mode);
		SetConsoleMode(hIn, con_mode & ~(ENABLE_ECHO_INPUT | ENABLE_LINE_INPUT));

		while (ReadConsoleA(hIn, &ch, 1, &dwRead, NULL) && ch != RETURN)
		{
			if (ch == BACKSPACE)
			{
				if (password.length() != 0)
				{
					if (show_asterisk)
						cout << "\b \b";
					password.resize(password.length() - 1);
				}
			}
			else
			{
				password += ch;
				if (show_asterisk)
					cout << '*';
			}
		}

		GetConsoleMode(hIn, &con_mode);
		SetConsoleMode(hIn, con_mode | (ENABLE_ECHO_INPUT | ENABLE_LINE_INPUT));
		cout << endl;
		return password;
	}
#else
	void echo( bool on = true )
	{
		struct termios settings;
		tcgetattr( STDIN_FILENO, &settings );
		settings.c_lflag = on
			? (settings.c_lflag |   ECHO )
			: (settings.c_lflag & ~(ECHO));
		tcsetattr( STDIN_FILENO, TCSANOW, &settings );
	}
	int getch() {
		int ch;
		struct termios t_old, t_new;

		tcgetattr(STDIN_FILENO, &t_old);
		t_new = t_old;
		t_new.c_lflag &= ~(ICANON | ECHO);
		tcsetattr(STDIN_FILENO, TCSANOW, &t_new);

		ch = getchar();

		tcsetattr(STDIN_FILENO, TCSANOW, &t_old);
		return ch;
	}

	string getpass(const char *prompt, bool show_asterisk)
	{
		const char BACKSPACE = 127;
		const char RETURN = 10;

		string password;
		unsigned char ch = 0;

		cout << prompt;

		while ((ch = getch()) != RETURN)
		{
			if (ch == BACKSPACE)
			{
				if (password.length() != 0)
				{
					if (show_asterisk)
						cout << "\b \b";
					password.resize(password.length() - 1);
				}
			}
			else
			{
				password += ch;
				if (show_asterisk)
					cout << '*';
			}
		}
		cout << endl;
		return password;
	}
#endif
}