package elevator.floor;

import elevator.fairness.FairnessStrategy;
import elevator.fairness.PassengerComparator;
import elevator.passenger.Passenger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class ElevatorQueue {

    private final List<Passenger> waiting = new ArrayList<>();
    private FairnessStrategy strategy = FairnessStrategy.BY_TASK_PRIORITY;

    public synchronized void enqueue(Passenger p) {
        waiting.add(p);
    }

    public synchronized boolean remove(Passenger p) {
        return waiting.remove(p);
    }


    public synchronized Passenger pickBest(double weightCapacity) {
        if (waiting.isEmpty()) return null;
        Comparator<Passenger> cmp = PassengerComparator.get(strategy);
        return waiting.stream()
                .filter(p -> getEffectiveWeight(p) <= weightCapacity)
                .min(cmp)
                .orElse(null);
    }

    public synchronized boolean removeBest(Passenger p) {
        return waiting.remove(p);
    }

    public synchronized boolean isEmpty() {
        return waiting.isEmpty();
    }

    public synchronized int size() {
        return waiting.size();
    }

    public void setStrategy(FairnessStrategy strategy) {
        this.strategy = strategy;
    }

    private double getEffectiveWeight(Passenger p) {
        if (p instanceof elevator.passenger.Porter porter) {
            return porter.getTotalWeight();
        }
        return p.getWeight();
    }
}
