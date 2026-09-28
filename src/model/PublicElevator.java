package elevator.model;

import elevator.passenger.Passenger;
import elevator.passenger.PassengerRole;

public class PublicElevator extends Elevator {

    public PublicElevator(int id, double weightCapacity) {
        super(id, ElevatorType.PUBLIC, weightCapacity);
    }

    @Override
    public boolean canCarry(Passenger passenger) {
        return passenger.getRole() != PassengerRole.PORTER
                && passenger.getWeight() <= weightCapacity;
    }
}
