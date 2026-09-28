package elevator.model;

import elevator.passenger.Passenger;
import elevator.passenger.PassengerRole;
import elevator.passenger.Porter;

public class FreightElevator extends Elevator {

    public FreightElevator(int id, double weightCapacity) {
        super(id, ElevatorType.FREIGHT, weightCapacity);
    }

    @Override
    public boolean canCarry(Passenger passenger) {
        if (passenger.getRole() != PassengerRole.PORTER) return false;
        Porter porter = (Porter) passenger;
        return porter.getTotalWeight() <= weightCapacity;
    }
}
