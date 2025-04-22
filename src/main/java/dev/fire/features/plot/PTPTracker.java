package dev.fire.features.plot;

import dev.fire.Mod;
import dev.fire.features.Feature;
import dev.fire.helper.CommandQueue;
import dev.fire.helper.CommandQueueHelper;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PTPTracker extends Feature {
    public static HashMap<String, String> playerData;
    public static PTPState ptpState;

    public PTPTracker() {
        init("ptptracker", "Internal PTP Tracker", "Automatically sends ptp messages for you.");
        playerData = new HashMap<>();
        ptpState = PTPState.SPAWN;
    }
    public static void requestPTP() {
        CommandQueueHelper.addCommand(
                new CommandQueue(
                        "/plot data players",
                        0,
                        new ArrayList<>(List.of(
                                "^ {39}\\nPlayer Count: \\d* \\(\\d* playing\\)\\n(.*)",
                                "^Error: You must be in a plot to use this command!",
                                "^Error: You need to be a plot developer to use this command!"
                            ))
                        ));

    }

    @Override
    public void onChatMessage(Text message, CallbackInfo ci) {
        String text = message.getString();
        Matcher matcher;

        matcher = Pattern.compile("^ {39}\\nPlayer Count: \\d* \\(\\d* playing\\)\\n").matcher(text);
        if (matcher.find()) {
            String data = text.substring(matcher.end());
            playerData = new HashMap<>();
            ptpState = PTPState.VALID;

            boolean done = false;
            while (!done) {
                matcher = Pattern.compile("\\n→ (.{3,16}): (Play|Dev|Build) \\[⏼]").matcher(data);
                if (matcher.find()) {
                    playerData.put(matcher.group(1), matcher.group(2).toLowerCase());
                    data = data.substring(matcher.end());
                } else {  done = true; }
            }
        }

        matcher = Pattern.compile("^Error: You must be in a plot to use this command!").matcher(text);
        if (matcher.find())  {
            playerData.clear();
            ptpState = PTPState.SPAWN;
        }

        matcher = Pattern.compile("^Error: You need to be a plot developer to use this command!").matcher(text);
        if (matcher.find()) {
            playerData.clear();
            ptpState = PTPState.NO_PERM;
        }
    }
}
