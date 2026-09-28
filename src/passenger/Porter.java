package elevator.passenger;

import elevator.task.Task;

public class Porter extends Passenger {
    private final double cargoWeight;

    public Porter(int age, double weight, double cargoWeight, Task task) {
        super(age, weight, PassengerRole.PORTER, task);
        this.cargoWeight = cargoWeight;
    }

    public double getCargoWeight() { return cargoWeight; }

    public double getTotalWeight() { return weight + cargoWeight; }
}
