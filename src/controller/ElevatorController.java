package elevator.controller;

import elevator.floor.FloorManager;
import elevator.model.Elevator;
import elevator.passenger.Passenger;
import elevator.passenger.Repairman;
import elevator.util.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


public class ElevatorController {

    private static ElevatorController instance;

    private final AtomicLong totalTravelMs = new AtomicLong(0);
    private final List<Integer> completedTaskIds = Collections.synchronizedList(new ArrayList<>());

    private final Object repairLock = new Object();
    private final List<Elevator> brokenElevators = new ArrayList<>();
    private final List<Thread> repairmanThreads = new ArrayList<>();

    private FloorManager floorManager;
    private List<Elevator> elevators;
    private List<Thread> elevatorThreads;
    private List<Thread> passengerThreads;

    private ElevatorController() {}

    public static synchronized ElevatorController getInstance() {
        if (instance == null) instance = new ElevatorController();
        return instance;
    }

    public void initialize(FloorManager fm, List<Elevator> elevators,
                           List<Thread> elevatorThreads, List<Thread> passengerThreads) {
        this.floorManager = fm;
        this.elevators = elevators;
        this.elevatorThreads = elevatorThreads;
        this.passengerThreads = passengerThreads;
    }

    public void addTravelTime(long ms) {
        totalTravelMs.addAndGet(ms);
    }

    public void recordCompletedTask(int taskId) {
        completedTaskIds.add(taskId);
    }

    public void reportBrokenElevator(Elevator e) {
        synchronized (repairLock) {
            brokenElevators.add(e);
            repairLock.notifyAll();
        }
    }


    public void startRepairmen(List<Repairman> repairmen) {
        for (Repairman r : repairmen) {
            Thread t = new Thread(() -> repairmanLoop(r), "Repairman#" + r.getId());
            repairmanThreads.add(t);
            t.start();
        }
    }

    private void repairmanLoop(Repairman repairman) {
        Logger.log(repairman + " waiting for broken elevators.");
        synchronized (repairLock) {
            while (repairman.isRunning()) {
                while (brokenElevators.isEmpty() && repairman.isRunning()) {
                    try {
                        repairLock.wait(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                if (!brokenElevators.isEmpty()) {
                    Elevator e = brokenElevators.remove(0);
                    try {
                        repairLock.wait(repairman.getRepairDurationMs());
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    e.repair();
                    Logger.log(repairman + " repaired Elevator#" + e.getId());
                }
            }
        }
    }


    public void shutdown() throws InterruptedException {
        Logger.log("=== SHUTDOWN SIGNAL SENT ===");

        for (Thread t : passengerThreads) t.interrupt();
        for (Thread t : passengerThreads) t.join(3000);

        for (Elevator e : elevators) e.stop();
        for (Thread t : elevatorThreads) t.join(3000);

        for (Thread t : repairmanThreads) t.interrupt();

        printReport();
    }

    private void printReport() {
        System.out.println("\n========== SIMULATION REPORT ==========");
        System.out.println("Total elevator travel time: " + totalTravelMs.get() + " ms");
        System.out.println("Completed task IDs: " + completedTaskIds);
        System.out.println("Total tasks completed: " + completedTaskIds.size());
        System.out.println("========================================\n");
    }
}
