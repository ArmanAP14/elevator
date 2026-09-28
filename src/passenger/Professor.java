package elevator.passenger;

import elevator.task.Task;

public class Professor extends Passenger {
    public Professor(int age, double weight, Task task) {
        super(age, weight, PassengerRole.PROFESSOR, task);
    }
}
