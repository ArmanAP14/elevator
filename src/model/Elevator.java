package elevator.model;

import elevator.controller.ElevatorController;
import elevator.floor.ElevatorQueue;
import elevator.floor.Floor;
import elevator.floor.FloorManager;
import elevator.passenger.Passenger;
import elevator.util.Logger;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;


public abstract class Elevator implements Runnable {

    protected static final long FLOOR_TRAVEL_MS = 500;
    protected static final double BREAK_PROBABILITY = 0.05;

    protected final int id;
    protected final ElevatorType type;
    protected final double weightCapacity;
    protected volatile int currentFloor = 0;
    protected volatile boolean running = true;
    protected volatile boolean broken = false;

    protected FloorManager floorManager;
    protected ElevatorController controller;

    private final AtomicLong totalTravelMs = new AtomicLong(0);
    private final Random random = new Random();

    public Elevator(int id, ElevatorType type, double weightCapacity) {
        this.id = id;
        this.type = type;
        this.weightCapacity = weightCapacity;
    }

    @Override
    public void run() {
        Logger.log("Elevator#" + id + " [" + type + "] started at floor " + currentFloor);
        int direction = 1; // 1 = up, -1 = down
        int numFloors = floorManager.getNumFloors();

        while (running) {
            try {
                serveCurrentFloor();

                if (!running) break;

                int nextFloor = currentFloor + direction;
                if (nextFloor >= numFloors) {
                    direction = -1;
                    nextFloor = currentFloor + direction;
                } else if (nextFloor < 0) {
                    direction = 1;
                    nextFloor = currentFloor + direction;
                }

                travelToFloor(nextFloor);

                if (!running) break;

                if (checkBreakdown()) break;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        Logger.log("Elevator#" + id + " stopped. Total travel time: " + totalTravelMs.get() + "ms");
    }


    protected void serveCurrentFloor() throws InterruptedException {
        Floor floor = floorManager.getFloor(currentFloor);
        ElevatorQueue queue = floor.getQueue(id);

        while (!queue.isEmpty() && running) {
            Passenger passenger = pickPassenger(queue);
            if (passenger == null) break;
            deliverPassenger(passenger);
            if (!running) break;
        }
    }


    protected Passenger pickPassenger(ElevatorQueue queue) {
        Passenger p = queue.pickBest(weightCapacity);
        if (p == null) return null;
        queue.removeBest(p);
        Logger.log("Elevator#" + id + " picked up " + p + " at floor " + currentFloor);
        return p;
    }


    protected void deliverPassenger(Passenger passenger) throws InterruptedException {
        int dest = passenger.getTask().isCompleted() ? 0 : passenger.getTask().getTargetFloor();

        travelToFloor(dest);

        synchronized (passenger) {
            passenger.setCurrentFloor(dest);
            passenger.notifyAll();
        }
        Logger.log("Elevator#" + id + " delivered " + passenger + " to floor " + dest);
    }

    protected void travelToFloor(int target) throws InterruptedException {
        if (target == currentFloor) return;
        int steps = Math.abs(target - currentFloor);
        long travelTime = steps * FLOOR_TRAVEL_MS;
        Thread.sleep(travelTime);
        totalTravelMs.addAndGet(travelTime);
        currentFloor = target;
        controller.addTravelTime(travelTime);
        Logger.log("Elevator#" + id + " arrived at floor " + currentFloor);
    }

    protected boolean checkBreakdown() throws InterruptedException {
        if (random.nextDouble() < BREAK_PROBABILITY) {
            broken = true;
            Logger.log("Elevator#" + id + " BROKE DOWN at floor " + currentFloor + "!");
            controller.reportBrokenElevator(this);

            synchronized (this) {
                while (broken && running) {
                    this.wait(1000);
                }
            }
            if (broken) {
                return true;
            }
            Logger.log("Elevator#" + id + " repaired and resuming at floor " + currentFloor);
        }
        return false;
    }

    public void repair() {
        synchronized (this) {
            broken = false;
            Logger.log("Elevator#" + id + " has been repaired.");
            this.notifyAll();
        }
    }

    public void stop() {
        this.running = false;
        synchronized (this) {
            this.notifyAll();
        }
    }

    public abstract boolean canCarry(Passenger passenger);

    public int getId() { return id; }
    public ElevatorType getType() { return type; }
    public int getCurrentFloor() { return currentFloor; }
    public boolean isBroken() { return broken; }
    public double getWeightCapacity() { return weightCapacity; }

    public void setFloorManager(FloorManager fm) { this.floorManager = fm; }
    public void setController(ElevatorController c) { this.controller = c; }
}
