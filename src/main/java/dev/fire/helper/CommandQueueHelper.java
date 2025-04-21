package dev.fire.helper;

import dev.fire.Mod;
import dev.fire.features.commands.CommandHider;
import dev.fire.utils.ChatUtils;

import java.util.ArrayList;

public class CommandQueueHelper {
    public static ArrayList<CommandQueue> commandQueue = new ArrayList<>();
    private static long nextTimestamp = -1;

    public static void setTimestamp(long timestamp) { nextTimestamp = timestamp; }
    public static void addCurrentTimestamp(long addTimestamp) { nextTimestamp = System.currentTimeMillis() + addTimestamp; }

    public static void addCommand(CommandQueue command) {
        commandQueue.add(command);
    }

    public static boolean hasCommand(String command) {
        for (CommandQueue c : commandQueue) {
            if (c.command.equals(command)) {
                return true;
            }
        }
        return false;
    }

    public static void tick() {
        long currentTimestamp = System.currentTimeMillis();
        if (currentTimestamp > nextTimestamp && nextTimestamp != -1 && !commandQueue.isEmpty() && Mod.MC.getNetworkHandler() != null) {
            CommandQueue command = commandQueue.removeFirst();
            CommandHider.addSingleHiddenCommand(command.singleHider);
            CommandHider.addMultiHiddenCommand(command.multiHider);
            ChatUtils.sendMessage(command.command);
            nextTimestamp = currentTimestamp + command.delay;
        }
    }
}
