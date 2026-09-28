package elevator.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public static synchronized void log(String message) {
        System.out.printf("[%s] [%s] %s%n",
                LocalTime.now().format(FMT),
                Thread.currentThread().getName(),
                message);
    }
}
