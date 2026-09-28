package elevator;

import elevator.controller.ElevatorController;
import elevator.floor.FloorManager;
import elevator.model.*;
import elevator.passenger.Passenger;
import elevator.passenger.Repairman;
import elevator.util.Logger;
import elevator.util.PassengerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Simulation {

    private static Simulation instance;

    private static final int NUM_FLOORS = 8;
    private static final int NUM_REPAIRMEN = 2;
    private static final double PUBLIC_CAPACITY = 300.0;
    private static final double VIP_CAPACITY = 250.0;
    private static final double FREIGHT_CAPACITY = 600.0;
    private static final long SIMULATION_RUN_MS = 15_000;

    private Simulation() {}

    public static synchronized Simulation getInstance() {
        if (instance == null) instance = new Simulation();
        return instance;
    }

    public void run(int numPassengers, int numElevators) throws InterruptedException {
        Logger.log("=== Sharif Math Faculty Elevator Simulation ===");
        Logger.log("Floors: " + NUM_FLOORS + ", Passengers: " + numPassengers + ", Elevators: " + numElevators);

        ElevatorController controller = ElevatorController.getInstance();

        List<Elevator> elevators = buildElevators(numElevators);
        List<Thread> elevatorThreads = new ArrayList<>();

        List<Passenger> passengers = new ArrayList<>();
        List<Repairman> repairmen = new ArrayList<>();
        for (int i = 0; i < NUM_REPAIRMEN; i++) repairmen.add(PassengerFactory.createRepairman(NUM_FLOORS));
        for (int i = 0; i < numPassengers; i++) passengers.add(PassengerFactory.createRandom(NUM_FLOORS));

        FloorManager floorManager = new FloorManager(NUM_FLOORS, elevators, controller);

        for (Elevator e : elevators) {
            e.setFloorManager(floorManager);
            e.setController(controller);
            Thread t = new Thread(e, "Elevator#" + e.getId());
            elevatorThreads.add(t);
        }

        List<Thread> passengerThreads = new ArrayList<>();
        for (Passenger p : passengers) {
            p.setFloorManager(floorManager);
            Thread t = new Thread(p, "Passenger#" + p.getId());
            passengerThreads.add(t);
        }
        for (Repairman r : repairmen) {
            r.setFloorManager(floorManager);
        }

        controller.initialize(floorManager, elevators, elevatorThreads, passengerThreads);

        controller.startRepairmen(repairmen);

        for (Thread t : elevatorThreads) t.start();

        for (Thread t : passengerThreads) {
            t.start();
            Thread.sleep(100);
        }

        Logger.log("Simulation running for " + SIMULATION_RUN_MS + "ms...");
        Thread.sleep(SIMULATION_RUN_MS);

        controller.shutdown();
    }

    private List<Elevator> buildElevators(int numElevators) {
        List<Elevator> list = new ArrayList<>();
        for (int i = 0; i < numElevators; i++) {
            Elevator e;
            int mod = i % 3;
            if (mod == 0) e = new PublicElevator(i, PUBLIC_CAPACITY);
            else if (mod == 1) e = new VipElevator(i, VIP_CAPACITY);
            else e = new FreightElevator(i, FREIGHT_CAPACITY);
            list.add(e);
        }
        return list;
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of passengers (N): ");
        int n = scanner.nextInt();
        System.out.print("Enter number of elevators (M): ");
        int m = scanner.nextInt();
        Simulation.getInstance().run(n, m);
    }
}
