package elevator.util;

import elevator.passenger.*;
import elevator.task.Priority;
import elevator.task.Task;

import java.util.Random;

public class PassengerFactory {

    private static final Random RNG = new Random();
    private static final long MIN_TASK_MS = 500;
    private static final long MAX_TASK_MS = 2000;

    public static Passenger createRandom(int numFloors) {
        int role = RNG.nextInt(4);
        int age = 18 + RNG.nextInt(50);
        double weight = 50 + RNG.nextDouble() * 70;
        Task task = createRandomTask(numFloors);

        return switch (role) {
            case 0 -> new Undergraduate(age, weight, task);
            case 1 -> new Professor(age, weight, task);
            case 2 -> new ViceDean(age, weight, task);
            default -> {
                double cargo = 10 + RNG.nextDouble() * 90;
                yield new Porter(age, weight, cargo, task);
            }
        };
    }

    public static Task createRandomTask(int numFloors) {
        Priority[] priorities = Priority.values();
        Priority priority = priorities[RNG.nextInt(priorities.length)];
        int floor = 1 + RNG.nextInt(numFloors - 1);
        long duration = MIN_TASK_MS + (long)(RNG.nextDouble() * (MAX_TASK_MS - MIN_TASK_MS));
        return new Task(priority, floor, duration);
    }

    public static Repairman createRepairman(int numFloors) {
        int age = 25 + RNG.nextInt(30);
        double weight = 60 + RNG.nextDouble() * 40;
        long repairTime = 1000 + (long)(RNG.nextDouble() * 2000);
        Task task = createRandomTask(numFloors);
        return new Repairman(age, weight, repairTime, task);
    }
}
