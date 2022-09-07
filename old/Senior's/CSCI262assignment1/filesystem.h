/* * * * * * * * * * * * * * * * * * * *
*	Name		: Pang Jin Xiong Joel  *
*	Student ID	: 4643409              *
* * * * * * * * * * * * * * * * * * * */
#include "stdafx.h"
#include "file.h"

class filesystem
{
public:
	filesystem();
	void createFile(std::string owner);
	void listHolding();
	void listRecords();
	void fileRead(int level);
	void fileWrite(int level);
	bool saveFiles();
private:
	std::vector<file> files;
	int records;
	void init();
	int findFile(std::string name);
};