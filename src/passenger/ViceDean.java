package elevator.passenger;

import elevator.task.Task;

public class ViceDean extends Passenger {
    public ViceDean(int age, double weight, Task task) {
        super(age, weight, PassengerRole.VICE_DEAN, task);
    }
}
