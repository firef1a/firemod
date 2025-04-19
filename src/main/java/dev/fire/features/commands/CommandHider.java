package dev.fire.features.commands;

import dev.fire.Mod;
import dev.fire.features.Feature;
import dev.fire.helper.CommandQueue;
import dev.fire.helper.CommandQueueHelper;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class CommandHider extends Feature {
    public static ArrayList<String> commandHiderList;
    public CommandHider() {
        init("commandhider", "Automated Command Hider", "Internal Feature, disable if it is hiding things it shouldn't");
        commandHiderList = new ArrayList<>();
    }

    public static void addHiddenCommand(String text ) { commandHiderList.add(text); }

    public void onChatMessage(Text message, CallbackInfo ci) {
        String text = message.getString();

        for (String hider : commandHiderList) {
            if (Pattern.compile(hider, Pattern.CASE_INSENSITIVE).matcher(text).find()) {
                ci.cancel();
                commandHiderList.remove(hider);
                break;
            }
        }
    }
}
