package dev.fire.features.plot;

import dev.fire.Mod;
import dev.fire.features.Feature;
import dev.fire.helper.CommandQueue;
import dev.fire.helper.CommandQueueHelper;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Map.entry;

public class CTPTracker extends Feature {
    public static CTPState ctpState;
    public static Map<String, ArrayList<String>> ctpResult;
    public static Map<Integer, String> idMap = new HashMap<>(Map.ofEntries(
            entry(5000001, "event"),
            entry(5000002, "function"),
            entry(5000003, "process")
    ));
    public CTPTracker() {
        init("ctptracker", "Internal CTP Tracker", "Automatically sends ctp packets for you.");
        ctpResult = new HashMap<>();
        ctpState = CTPState.NO_PERM;
    }

    public static void sendCTPSuggestion() {
        if (Mod.MC.getNetworkHandler() == null) return;
        ArrayList<RequestCommandCompletionsC2SPacket> requestCommandCompletionsC2SPackets = new ArrayList<>(List.of(
                new RequestCommandCompletionsC2SPacket(5000001, "/ctp event "),
                new RequestCommandCompletionsC2SPacket(5000002, "/ctp function "),
                new RequestCommandCompletionsC2SPacket(5000003, "/ctp process ")
        ));
        for (RequestCommandCompletionsC2SPacket p : requestCommandCompletionsC2SPackets) { Mod.MC.getNetworkHandler().sendPacket(p); }

        CommandQueueHelper.addCommand(
                new CommandQueue(
                        "/plot data",
                        0,
                        new ArrayList<>(List.of(
                                "^Usage: \\n/plot data entities\\n/plot data vars\\n/plot data tasks\\n/plot data players\\n/plot data plot",
                                "^Error: You must be in a plot to use this command!"
                        ))
                ));
    }

    @Override
    public void onChatMessage(Text message, CallbackInfo ci) {
        String text = message.getString();
        Matcher matcher;

        matcher = Pattern.compile("^Usage: \\n/plot data entities\\n/plot data vars\\n/plot data tasks\\n/plot data players\\n/plot data plot").matcher(text);
        if (matcher.find()) { ctpState = CTPState.VALID; }

        matcher = Pattern.compile("^Error: You must be in a plot to use this command!").matcher(text);
        if (matcher.find()) { ctpState = CTPState.NO_PERM; }
    }

    @Override
    public void handlePacket(Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof CommandSuggestionsS2CPacket(int id, int start, int length, List<CommandSuggestionsS2CPacket.Suggestion> suggestions)) {
            Mod.log(id + " " + start + " " + length + " "  +suggestions);
            if (idMap.containsKey(id)) {
                String eventId = idMap.get(id);
                ArrayList<String> result = new ArrayList<>();
                ArrayList<String> entityResult = new ArrayList<>();
                for (CommandSuggestionsS2CPacket.Suggestion suggestion : suggestions) {
                    String text = suggestion.text();
                    if (eventId.equals("event")) {
                        if (text.startsWith("Entity")) {
                            entityResult.add(text);
                        } else {
                            result.add(text);
                        }
                    } else {
                        result.add(text);
                    }
                }
                ctpResult.put(eventId, result);
                if (eventId.equals("event")) ctpResult.put("entity", entityResult);
                Mod.log(ctpResult.toString());
            }
        }
    }
}
