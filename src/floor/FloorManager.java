package elevator.floor;

import elevator.controller.ElevatorController;
import elevator.model.Elevator;
import elevator.passenger.Passenger;
import elevator.util.Logger;

import java.util.List;


public class FloorManager {

    private final Floor[] floors;
    private final List<Elevator> elevators;
    private final ElevatorController controller;

    public FloorManager(int numFloors, List<Elevator> elevators, ElevatorController controller) {
        this.elevators = elevators;
        this.controller = controller;
        this.floors = new Floor[numFloors];
        for (int i = 0; i < numFloors; i++) {
            floors[i] = new Floor(i, elevators.size());
        }
    }


    public void waitForElevator(Passenger passenger, int fromFloor, int toFloor) throws InterruptedException {
        Elevator chosen = chooseSuitableElevator(passenger, fromFloor);
        if (chosen == null) {
            Logger.log("WARNING: No suitable elevator found for " + passenger + " at floor " + fromFloor);
            return;
        }

        Floor floor = floors[fromFloor];
        ElevatorQueue queue = floor.getQueue(chosen.getId());

        synchronized (passenger) {
            queue.enqueue(passenger);
            Logger.log(passenger + " joined queue of Elevator#" + chosen.getId() + " at floor " + fromFloor);
            while (passenger.getCurrentFloor() != toFloor) {
                passenger.wait(5000);
                if (!passenger.isRunning()) return;
            }
        }
    }

    private Elevator chooseSuitableElevator(Passenger passenger, int fromFloor) {
        for (Elevator e : elevators) {
            if (e.canCarry(passenger) && !e.isBroken()) {
                return e;
            }
        }
        return null;
    }

    public Floor getFloor(int number) {
        return floors[number];
    }

    public int getNumFloors() {
        return floors.length;
    }

    public ElevatorController getController() {
        return controller;
    }
}
