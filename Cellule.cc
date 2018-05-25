#include "Cellule.h"

Cellule::Cellule()
{
	celluleSuivante = NULL;
	estOccupee = false;
};

void Cellule::affiche()
{
	if (estOccupee)
	{
		cout << "[x]" ;
	} else {
		cout << "[ ]" ;
	}
}

Cellule* &Cellule::getCelluleSuivante()
{
	return celluleSuivante;
}

void Cellule::setCelluleSuivante(Cellule &c)
{
	celluleSuivante = &c;
}

bool Cellule::getEstOccupee()
{
	return estOccupee;
}

void Cellule::setEstOccupee(bool b)
{
	estOccupee = b;
}

void Cellule::evolue()
{
	if (estOccupee)
	{
		if (not(celluleSuivante==NULL))
		{
			if (not(celluleSuivante->getEstOccupee()))
			{
				setEstOccupee(false);
				celluleSuivante->setEstOccupee(true);
			}
		} else 
		{
			setEstOccupee(false);
		}
	}
}
