using namespace std;
#include <iostream>
#include "Cell.h"
#include "Road.h"
#include "RoundAbout.h"
#include "RoundAboutCell.h"
#include "CrossRoads.h"
#include "Network.h"

int main() {
	/*Cell c1 = Cell();
	Cell c2 = Cell();
	Cell c3 = Cell();
	Cell c4 = Cell();
	Cell c5 = Cell();
	c1.setNextCell(c2);
	Road R = Road(c1);
	R.addCell(c2);
	R.addCell(c3);
	R.addCell(c4);
	R.addCell(c5);
	
	cout << "Voici notre route :" << endl;
	R.display();
	cout << "On va essayer d'afficher la cellule après c1 :" << endl;
	R.getRoadCells()[0]->display();
	cout << endl;
	cout << "Partant de la première cellule, on va rajouter un élément dans la cellule suivante :" << endl;
	R.getRoadCells()[0]->setIsOccupied(true);
	R.display();
	
	cout << "L'adresse de c1 est :\t\t\t\t\t" << &c1 << endl;
	cout << "L'adresse pointée par l'élément 1 de la route est :\t" << R.getRoadCells()[0] << endl;
	
	cout << "L'adresse de c2 est :\t\t\t\t\t" << &c2 << endl;
	cout << "L'adresse pointée par l'élément 2 de la route est :\t" << R.getRoadCells()[1] << endl;
	
	cout << "L'adresse de c3 est :\t\t\t\t\t" << &c3 << endl;
	cout << "L'adresse pointée par l'élément 3 de la route est :\t" << R.getRoadCells()[2] << endl;
	
	cout << "L'adresse de c4 est :\t\t\t\t\t" << &c4 << endl;
	cout << "L'adresse pointée par l'élément 4 de la route est :\t" << R.getRoadCells()[3] << endl;
	
	cout << "L'adresse de c5 est :\t\t\t\t\t" << &c5 << endl;
	cout << "L'adresse pointée par l'élément 5 de la route est :\t" << R.getRoadCells()[4] << endl;
	
	cout << "Essayons de simuler le trafic de la route :" << endl;
	R.getRoadCells()[1]->setIsOccupied(true);
	R.display();
	vector<int> fullCells = {};
	for (unsigned int j(1); j<=7; ++j)
	{
		fullCells = {};
		for (unsigned int i(0); i < R.getRoadCells().size(); ++i)
		{
			if (R.getRoadCells()[i]->getIsOccupied())
			{
				fullCells.push_back(i);
			}
		}
		for (unsigned int i(0); i < fullCells.size(); ++i)
		{
			R.getRoadCells()[fullCells[i]]->evolve();
		}
		R.display();
	}
	
	
	cout << "On va maintenant essayer d'initialiser une nouvelle route avec un vecteur de Cellules !" << endl;
	
	vector<Cell> vectCells(10);
	
	cout << "Taille de vectCellules : " << vectCells.size() << endl;
	
	for (Cell c: vectCells)
	{
		c.display();
	}
	cout << endl ;
	cout << "On crée une route R2 et on l'affiche : " << endl;
	Road R2(vectCells);
	
	R2.display();
	
	cout << "On va créer un rond-point de 7 cellules dont les deux premières sont remplies : " << endl;
	
	vector<Cell> vectRACells(7);
	RoundAbout RA(vectRACells);
	
	RA.getRoadCells()[0]->setIsOccupied(true);
	RA.getRoadCells()[1]->setIsOccupied(true);
	RA.display();
	
	for (unsigned int j(1); j<=10; ++j)
	{
		fullCells = {};
		for (unsigned int i(0); i < RA.getRoadCells().size(); ++i)
		{
			if (RA.getRoadCells()[i]->getIsOccupied())
			{
				fullCells.push_back(i);
			}
		}
		for (unsigned int i(0); i < fullCells.size(); ++i)
		{
			RA.getRoadCells()[fullCells[i]]->evolve();
		}
		RA.display();
	}
	*/
	/*
	// Tests sur les ronds-points:
	vector<Cell> vRi1(5);
	vector<Cell> vRo1(5);
	Road Ri1(vRi1);
	Road Ro1(vRo1);
	CrossRoadsCell C1;
	
	vector<Cell> vRi2(5);
	vector<Cell> vRo2(5);
	Road Ri2(vRi1);
	Road Ro2(vRo1);
	CrossRoadsCell C2;
	
	vector<Cell> vRi3(5);
	vector<Cell> vRo3(5);
	Road Ri3(vRi1);
	Road Ro3(vRo1);
	CrossRoadsCell C3;
	
	vector<Cell> vRi4(5);
	vector<Cell> vRo4(5);
	Road Ri4(vRi1);
	Road Ro4(vRo1);
	CrossRoadsCell C4;
	
	CrossRoads RondPoint(Ri1, Ri2, Ri3, Ri4, Ro1, Ro2, Ro3, Ro4, C1, C2, C3, C4);
	cout << "Comparaison des adresses des routes initialisées plus haut et des routes dans le rond point: " << endl;
	
	cout << RondPoint.roadsIN[0] << endl;
	cout << &(Ri1) << endl;
	
	cout << RondPoint.roadsIN[1] << endl;
	cout << &(Ri2) << endl;
	
	cout << RondPoint.roadsIN[2] << endl;
	cout << &(Ri3) << endl;
	
	cout << RondPoint.roadsIN[3] << endl;
	cout << &(Ri4) << endl;
	
	cout << RondPoint.roadsOUT[0] << endl;
	cout << &(Ro1) << endl;
	
	cout << RondPoint.roadsOUT[1] << endl;
	cout << &(Ro2) << endl;
	
	cout << RondPoint.roadsOUT[2] << endl;
	cout << &(Ro3) << endl;
	
	cout << RondPoint.roadsOUT[3] << endl;
	cout << &(Ro4) << endl;
	
	cout << "L'adresse de la cellule suivant C1 devrait être la même que l'adresse de la première cellule de Ro1: " << endl;
	cout << RondPoint.roadsOUT[0]->getRoadCells()[0] << endl;
	cout << Ro1.getRoadCells()[0] << endl;
	cout << RondPoint.cells[0]->getNextCell() << endl;
	*/
	
	// Road evolution test =================================================
	Cell c1 = Cell();
	c1.setIsOccupied(true);
	Cell c2 = Cell();
	c2.setIsOccupied(true);
	Cell c3 = Cell();
	c3.setIsOccupied(true);
	Cell c4 = Cell();
	Cell c5 = Cell();
	Cell c6 = Cell();
	Cell c7 = Cell();
	c1.setNextCell(c2);
	Road road = Road(c1);
	road.addCell(c2);
	road.addCell(c3);
	road.addCell(c4);
	road.addCell(c5);
	road.addCell(c6);
	road.addCell(c7);
	
	cout << " --- Road evolution ---" << endl;
	Network net = Network();
	net.setRoad(road);
	net.displayRoad();
	for (unsigned int j(1); j<=8; ++j) {
		net.computeEvolutionRoad();
		net.evolveRoad();
		net.displayRoad();
	}
	
	// RoundAbout evolution test ===========================================
	cout << " --- RoundAbout evolution ---" << endl;
	
	vector<Cell> vectRACells(7);
	RoundAbout ra(vectRACells);
	
	ra.getRoadCells()[0]->setIsOccupied(true);
	ra.getRoadCells()[1]->setIsOccupied(true);
	
	Network net2 = Network();
	net2.setRoundAbout(ra);
	net2.displayRoundAbout();
	
	for (unsigned int j(1); j<=15; ++j) {
		net2.computeEvolutionRoundAbout();
		net2.evolveRoundAbout();
		net2.displayRoundAbout();
	}
}

