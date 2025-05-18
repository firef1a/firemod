package dev.fire.helper;

import java.util.ArrayList;
import java.util.function.Consumer;

public class CommandQueue {
    public String command;
    public long delay;
    public ArrayList<String> singleHider;
    public ArrayList<String> multiHider;
    public ArrayList<String> entitySounds;
    public Consumer<Object[]> consumer;

    public CommandQueue(String command, long delay, ArrayList<String> singleHider, ArrayList<String> multiHider, ArrayList<String> entitySounds) {
        this.command = command;
        this.delay = delay;
        this.singleHider = singleHider;
        this.multiHider = multiHider;
        this.entitySounds = entitySounds;
    }

    public CommandQueue(String command, long delay, ArrayList<String> singleHider, ArrayList<String> multiHider) {
        this(command, delay, singleHider, multiHider, new ArrayList<>());
    }
    public CommandQueue(String command, long delay, ArrayList<String> singleHider) {
        this(command, delay, singleHider, new ArrayList<String>());
    }

    public CommandQueue(String command, long delay) {
        this(command, delay, new ArrayList<>());
    }

    public CommandQueue(String command) {
        this(command, 0);
    }

    public long getCommandCD() {
        return (50L * (command.length()) + 25L) + 100L;
    }

    public void setConsumer(Consumer<Object[]> consumer) {
        this.consumer = consumer;
    }
}
