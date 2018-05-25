#include "Case.h"

Case::Case()
{
	caseSuivante = NULL;
	estOccupee = false;
};

void Case::affiche()
{
	if (estOccupee)
	{
		cout << "[x]" ;
	} else {
		cout << "[ ]" ;
	}
}

Case* &Case::getCaseSuivante()
{
	return caseSuivante;
}

void Case::setCaseSuivante(Case &c)
{
	caseSuivante = &c;
}

bool Case::getEstOccupee()
{
	return estOccupee;
}

void Case::setEstOccupee(bool b)
{
	estOccupee = b;
}

void Case::evolue()
{
	if (estOccupee)
	{
		if (not(caseSuivante==NULL))
		{
			if (not(caseSuivante->getEstOccupee()))
			{
				setEstOccupee(false);
				caseSuivante->setEstOccupee(true);
			}
		} else 
		{
			setEstOccupee(false);
		}
	}
}
