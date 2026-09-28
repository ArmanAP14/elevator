package elevator.passenger;

import elevator.task.Task;

public class Undergraduate extends Passenger {
    public Undergraduate(int age, double weight, Task task) {
        super(age, weight, PassengerRole.UNDERGRADUATE, task);
    }
}
