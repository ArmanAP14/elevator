package elevator.fairness;

import elevator.passenger.Passenger;
import elevator.passenger.PassengerRole;
import elevator.task.Priority;

import java.util.Comparator;

public class PassengerComparator {

    public static Comparator<Passenger> get(FairnessStrategy strategy) {
        return switch (strategy) {
            case BY_TASK_PRIORITY -> byTaskPriority();
            case BY_ROLE -> byRole();
            case BY_AGE -> byAge();
        };
    }

    private static Comparator<Passenger> byTaskPriority() {
        return Comparator.comparingInt(p -> -priorityValue(p.getTask().getPriority()));
    }

    private static Comparator<Passenger> byRole() {
        return Comparator.comparingInt(p -> -roleValue(p.getRole()));
    }

    private static Comparator<Passenger> byAge() {
        return Comparator.comparingInt(p -> -p.getAge());
    }

    private static int priorityValue(Priority p) {
        return switch (p) {
            case LOW -> 0;
            case MEDIUM -> 1;
            case HIGH -> 2;
        };
    }

    private static int roleValue(PassengerRole r) {
        return switch (r) {
            case VICE_DEAN -> 4;
            case PROFESSOR -> 3;
            case REPAIRMAN -> 2;
            case UNDERGRADUATE -> 1;
            case PORTER -> 0;
        };
    }
}
