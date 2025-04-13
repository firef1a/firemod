package dev.fire.helper;
import java.util.function.Consumer;

public class CommandQueue {
    public String command;
    public long delay;

    public CommandQueue(String command, long delay) {
        this.command = command;
        this.delay = delay;
    }

    public CommandQueue(String command) {
        this.command = command;
        this.delay = getCommandCD();
    }

    public long getCommandCD() {
        return (50L * (command.length()) + 25L) + 100L;
    }
}
