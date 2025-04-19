package dev.fire.helper;
import org.python.antlr.ast.Str;

import java.util.ArrayList;
import java.util.function.Consumer;

public class CommandQueue {
    public String command;
    public long delay;
    public ArrayList<String> returnHiderList;
    public CommandQueue(String command, long delay, ArrayList<String> returnHiderList) {
        this.command = command;
        this.delay = delay;
        this.returnHiderList = returnHiderList;
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
}
