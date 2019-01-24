package elements;

import java.util.ArrayList;

import utils.Utils;

public class MaxVehicleOutflow {
	
	private ArrayList<Road> roads = new ArrayList<Road>();
	private int globalOutflow;
	private int counter = 0;
	
	public MaxVehicleOutflow(Road road, int globalOutflow) {
		roads.add(road);
		road.useSingleOutflow(false);
		this.globalOutflow = globalOutflow;
	}

	public void addRoad(Road road) {
		roads.add(road);
		road.useSingleOutflow(false);
	}
	public void addRoad(Road road1, Road road2) {
		roads.add(road1);
		road1.useSingleOutflow(false);
		roads.add(road2);
		road2.useSingleOutflow(false);
	}
	public void tick() {
		counter++;
		
		// if a vehicle can pass the exit
		if (counter>globalOutflow) {
			counter = 0;
			
			if (roads.size() > 2) {
				Utils.log("ERROR : more than 2 roads in MaxVehicleOutflow, case not considered");
			}
			if (roads.size() == 2) {
				
				// number of vehicle on each road
				int file1 = roads.get(0).getNumberOfVehiclesAtEnd(13);
				int file2 = roads.get(1).getNumberOfVehiclesAtEnd(13);
				
				// if road 0 have less vehicle than road 1
				if (file1 < file2) {

					// block road 0, unblock road 1
					roads.get(0).getRoadCells().get(roads.get(0).getRoadCells().size()-1).setBlocked(true);
					roads.get(1).getRoadCells().get(roads.get(1).getRoadCells().size()-1).setBlocked(false);
					
				} else {
					
					// unblock road 0, block road 1
					roads.get(0).getRoadCells().get(roads.get(0).getRoadCells().size()-1).setBlocked(false);
					roads.get(1).getRoadCells().get(roads.get(1).getRoadCells().size()-1).setBlocked(true);
					
				}
				
			} else if (roads.size()==1) {
				Utils.log("ERROR : in MaxVehicleOutflow, roads is size 1 (unuseful)\n");
			} else {
				Utils.log("ERROR : in MaxVehicleOutflow, roads is size 0\n");
			}
			
		} else {
			roads.get(0).getRoadCells().get(roads.get(0).getRoadCells().size()-1).setBlocked(true);
			roads.get(1).getRoadCells().get(roads.get(1).getRoadCells().size()-1).setBlocked(true);
		}
	}
}
