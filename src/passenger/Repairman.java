package elevator.passenger;

import elevator.task.Task;

public class Repairman extends Passenger {
    private final long repairDurationMs;

    public Repairman(int age, double weight, long repairDurationMs, Task task) {
        super(age, weight, PassengerRole.REPAIRMAN, task);
        this.repairDurationMs = repairDurationMs;
    }

    public long getRepairDurationMs() { return repairDurationMs; }
}
