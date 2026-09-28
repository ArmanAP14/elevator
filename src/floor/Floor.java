package elevator.floor;

import java.util.HashMap;
import java.util.Map;

public class Floor {
    private final int floorNumber;
    private final Map<Integer, ElevatorQueue> queues;

    public Floor(int floorNumber, int numElevators) {
        this.floorNumber = floorNumber;
        this.queues = new HashMap<>();
        for (int i = 0; i < numElevators; i++) {
            queues.put(i, new ElevatorQueue());
        }
    }

    public ElevatorQueue getQueue(int elevatorId) {
        return queues.get(elevatorId);
    }

    public int getFloorNumber() {
        return floorNumber;
    }
}
