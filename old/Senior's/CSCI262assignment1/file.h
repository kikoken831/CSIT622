/* * * * * * * * * * * * * * * * * * * *
*	Name		: Pang Jin Xiong Joel  *
*	Student ID	: 4643409              *
* * * * * * * * * * * * * * * * * * * */
#include "stdafx.h"

class file
{
public:
	file(std::string n, int l, std::string own)
	{
		name = n;
		level = l;
		owner = own;
	}
	std::string getName(){ return name; }
	int getLevel(){ return level; }
	std::string getOwner(){ return owner; }
private:
	std::string name;
	int level;
	std::string owner;
};