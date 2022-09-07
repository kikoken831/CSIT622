/* * * * * * * * * * * * * * * * * * * *
*	Name		: Pang Jin Xiong Joel  *
*	Student ID	: 4643409              *
* * * * * * * * * * * * * * * * * * * */
#include "stdafx.h"

class user
{
public:
	std::string getSalt() { return salt; }
	std::string getSaltPassword() { return saltPassword; }
	std::string getName() { return name; }
	int getClearence() { return clearence; }

	bool loginUser();
	bool saveUser(std::string secret);
private:
	std::string salt;
	std::string saltPassword;
	std::string name;
	int clearence;
	std::string genSalt(std::string text);
	bool retrieveSalt();
	bool comparePSalt(std::string psalt);
	std::string findUser(std::string filename, std::string uname);
};