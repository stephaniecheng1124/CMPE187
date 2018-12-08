/*** 
 * Author - Stephanie Cheng
 * Class - CMPE 187, Fall 2018.
 * Description - 
	 * This elevator algorithm selects the first qualifying car going in the same direction when a request is made.
	 * However, it prioritizes cars that are in IDLE mode 
	 * This method allows to user to be matched with an elevator that is already going to same way, which will reduce wait time.
 */

import java.util.List;


public class SameDirectionAlgorithm implements IAlgorithm {

	@Override
	public ICar findBestCar(List<ICar> lstCars, Direction direction, int destinationFloorNumber) {
		
		System.out.println("In SameDirectionAlgorithm.java ");
		ICar bestCar = null;
		
		if(lstCars.size() == 1){
			//return the first elevator if there is only one elevator
			bestCar = lstCars.get(0);
		}
		else if(lstCars.size() > 1){
			System.out.println("SIZE IS MORE THAN ONE");
			//Set the default best car to the first car in the list
			bestCar = lstCars.get(0);
			int numTasks = 0;
			numTasks = lstCars.get(0).getUserPanelQueue().getNumTasks();
			
			
			//If the first car is idle  or is about to be idle, return it
			if (bestCar.getStatus() == CarStatus.IDLE || (bestCar.getStatus() == CarStatus.STOPPED && numTasks == 0 )) {
				return bestCar;
			}
			
			for (int i = 1; i < lstCars.size()-1; i++) {
				
				//Calculate the elevator's task number, direction status, and the current floor it is on
				numTasks = lstCars.get(i).getUserPanelQueue().getNumTasks();
				CarStatus status = lstCars.get(i).getStatus();
				int elevatorCurrentFloor = lstCars.get(i).getCurrentFloorNumber();
				
				//If the elevator is stopped, see if it is about to go up, down, or prepare to be idle
				if (status == CarStatus.STOPPED) {	
					if (lstCars.get(i).getUserPanel().getSelection() == 0 || numTasks == 0) {
						status = CarStatus.IDLE;
					}
					else if (elevatorCurrentFloor > lstCars.get(i).getUserPanel().getSelection()) {
						status = CarStatus.MOVING_DOWN;
					}
					else if (elevatorCurrentFloor < lstCars.get(i).getUserPanel().getSelection()) {
						status = CarStatus.MOVING_UP;
					}
				}
				
				//If found a car that matches the criteria (Same direction & on the way or idle), return that car 
				if (status == CarStatus.IDLE) {
					bestCar = lstCars.get(i);
					return bestCar;
				}
				else if (status == CarStatus.MOVING_DOWN  && direction == Direction.DOWN) {
					if (elevatorCurrentFloor >= destinationFloorNumber) {
						bestCar = lstCars.get(i);
						return bestCar;
					}
				}
				else if (status == CarStatus.MOVING_UP  && direction == Direction.UP) {
					if (elevatorCurrentFloor <= destinationFloorNumber) {
						bestCar = lstCars.get(i);
						return bestCar;
					}
				}
				
			}
			
			//If none of the other cars in the list satisfy the requirements
			return bestCar;
		
		}
		else {
			System.out.println("In SameDirectionAlgorithm.java - There is no car in the list");
		}
		
		return bestCar;
			
	}

}
