package elevator.passenger;

import elevator.task.Task;
import elevator.util.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Passenger implements Runnable {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    protected final int id;
    protected final int age;
    protected final double weight;
    protected final PassengerRole role;
    protected final Task task;
    protected volatile int currentFloor;
    protected volatile boolean running = true;
    protected volatile boolean onElevator = false;
    protected volatile boolean taskDone = false;

    protected elevator.floor.FloorManager floorManager;

    public Passenger(int age, double weight, PassengerRole role, Task task) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.age = age;
        this.weight = weight;
        this.role = role;
        this.task = task;
        this.currentFloor = 0;
    }

    @Override
    public void run() {
        try {
            Logger.log("Passenger#" + id + " [" + role + "] arrives at building. Task: " + task);
            goToTaskFloor();
            if (!running) return;
            performTask();
            if (!running) return;
            returnToGround();
            Logger.log("Passenger#" + id + " [" + role + "] completed task and left the building.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.log("Passenger#" + id + " interrupted.");
        }
    }

    private void goToTaskFloor() throws InterruptedException {
        int target = task.getTargetFloor();
        if (currentFloor == target) return;
        Logger.log("Passenger#" + id + " waiting for elevator at floor " + currentFloor + " -> " + target);
        floorManager.waitForElevator(this, currentFloor, target);
    }

    private void performTask() throws InterruptedException {
        if (!running) return;
        Logger.log("Passenger#" + id + " performing " + task + " on floor " + currentFloor);
        Thread.sleep(task.getDurationMs());
        task.setCompleted(true);
        taskDone = true;
        floorManager.getController().recordCompletedTask(task.getId());
        Logger.log("Passenger#" + id + " finished " + task);
    }

    private void returnToGround() throws InterruptedException {
        if (currentFloor == 0) return;
        Logger.log("Passenger#" + id + " returning to ground floor from " + currentFloor);
        floorManager.waitForElevator(this, currentFloor, 0);
    }

    public void stop() {
        this.running = false;
    }

    public int getId() { return id; }
    public int getAge() { return age; }
    public double getWeight() { return weight; }
    public PassengerRole getRole() { return role; }
    public Task getTask() { return task; }
    public int getCurrentFloor() { return currentFloor; }
    public void setCurrentFloor(int floor) { this.currentFloor = floor; }
    public boolean isRunning() { return running; }
    public void setFloorManager(elevator.floor.FloorManager fm) { this.floorManager = fm; }

    @Override
    public String toString() {
        return "Passenger#" + id + "[" + role + ", age=" + age + ", weight=" + weight + "]";
    }
}
