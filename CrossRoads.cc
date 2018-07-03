#include "CrossRoads.h"

CrossRoads::CrossRoads(Road &Ri1, Road &Ri2, Road &Ri3, Road &Ri4, Road &Ro1, Road &Ro2, Road &Ro3, Road &Ro4, CrossRoadsCell &C1, CrossRoadsCell &C2, CrossRoadsCell &C3, CrossRoadsCell &C4) 
{
	
	roadsIN[0] = &Ri1;
	roadsIN[1] = &Ri2;
	roadsIN[2] = &Ri3;
	roadsIN[3] = &Ri4;

	roadsOUT[0] = &Ro1;
	roadsOUT[1] = &Ro2;
	roadsOUT[2] = &Ro3;
	roadsOUT[3] = &Ro4;
	
	cells[0] = &C1;
	cells[1] = &C2;
	cells[2] = &C3;
	cells[3] = &C4;
	
	C1.setNextCellIN(C2);
	C2.setNextCellIN(C3);
	C3.setNextCellIN(C4);
	C4.setNextCellIN(C1);
	
	Ri1.getRoadCells().back()->setNextCell(C1);
	C1.setNextCell(*(Ro1.getRoadCells().front()));
	
	Ri2.getRoadCells().back()->setNextCell(C2);
	C2.setNextCell(*(Ro2.getRoadCells().front()));
	
	Ri3.getRoadCells().back()->setNextCell(C3);
	C3.setNextCell(*(Ro3.getRoadCells().front()));
	
	Ri4.getRoadCells().back()->setNextCell(C4);
	C4.setNextCell(*(Ro4.getRoadCells().front()));
}
