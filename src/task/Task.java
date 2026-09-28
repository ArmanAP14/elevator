package elevator.task;

import java.util.concurrent.atomic.AtomicInteger;

public class Task {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    private final int id;
    private final Priority priority;
    private final int targetFloor;
    private final long durationMs;
    private volatile boolean completed = false;

    public Task(Priority priority, int targetFloor, long durationMs) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.priority = priority;
        this.targetFloor = targetFloor;
        this.durationMs = durationMs;
    }

    public int getId() { return id; }
    public Priority getPriority() { return priority; }
    public int getTargetFloor() { return targetFloor; }
    public long getDurationMs() { return durationMs; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    @Override
    public String toString() {
        return "Task#" + id + "[floor=" + targetFloor + ", priority=" + priority + "]";
    }
}
