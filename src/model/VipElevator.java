package elevator.model;

import elevator.passenger.Passenger;
import elevator.passenger.PassengerRole;

public class VipElevator extends Elevator {

    public VipElevator(int id, double weightCapacity) {
        super(id, ElevatorType.VIP, weightCapacity);
    }

    @Override
    public boolean canCarry(Passenger passenger) {
        PassengerRole role = passenger.getRole();
        return (role == PassengerRole.PROFESSOR || role == PassengerRole.VICE_DEAN)
                && passenger.getWeight() <= weightCapacity;
    }
}
