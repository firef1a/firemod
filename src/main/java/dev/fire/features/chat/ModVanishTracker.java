package dev.fire.features.chat;

import com.google.gson.JsonObject;
import dev.fire.Mod;
import dev.fire.config.Config;
import dev.fire.features.Feature;
import dev.fire.features.FeatureHudObjects;
import dev.fire.helper.CommandQueue;
import dev.fire.helper.CommandQueueHelper;
import dev.fire.render.ARGB;
import dev.fire.render.Alignment;
import dev.fire.render.Scaler;
import dev.fire.render.impl.ColorRectContainer;
import dev.fire.render.impl.ColorRectFeatureContainer;
import dev.fire.render.impl.TextList;
import dev.fire.utils.ChatUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModVanishTracker extends Feature {
    private static ColorRectFeatureContainer hudContainer;
    private static TextList hudTextList;

    private static String currentNode = "";
    private static Map<String, Integer> serverList = new HashMap<>();

    private static int currentNodePlayerCount, serverListPlayerCount;
    private static long timestamp;


    public ModVanishTracker() {
        init("modtracker", "Mod Vanish Tracker", "Exploits /server to detect vanished mods and admins.");
        timestamp = System.currentTimeMillis();

        Scaler hudPosition = Scaler.fromJsonOrDefault(getFeatureID() + ".modtracker", Config.configJSON, new Scaler(0.4739583333333333, 0.0125));

        hudContainer = new ColorRectFeatureContainer(hudPosition, 3, new ARGB(0.5, 0x00000),0,Alignment.NONE, Alignment.NONE, this);
        hudTextList = new TextList(new Scaler(0,0), 0, Alignment.NONE, Alignment.NONE, true);
        hudContainer.addSibling(hudTextList);

        FeatureHudObjects.registerObject(hudContainer);
    }

    @Override
    public void saveConfig(JsonObject jsonObject) {
        hudContainer.position.saveConfig(getFeatureID() + ".modtracker", jsonObject);
    }

    public void tick() {
        //if (Mod.MC.getServer() != null) Mod.log("Current player count: " + Mod.MC.getServer().getCurrentPlayerCount());
        String command = "/server";
        if (!CommandQueueHelper.hasCommand(command)) {
            long currentTimestamp = System.currentTimeMillis();
            if (currentTimestamp - timestamp > 5000L) {
                timestamp = currentTimestamp;
                CommandQueueHelper.addCommand(new CommandQueue(command, 0, new ArrayList<>(List.of("^You are currently connected to (.*).", "^Available servers: "))));
            }
        }
    }

    private void updateModTrackerHud() {
        hudContainer.setEnabled(true);
        int difference = Math.abs(serverListPlayerCount - currentNodePlayerCount);
        ArrayList<Text> textList = new ArrayList<>();
        textList.add(Text.literal("extdevtools:").withColor(0x7fb2fa).styled(style -> style.withUnderline(true)));
        textList.add(Text.literal("Player List: " + currentNodePlayerCount).withColor(0x7fb2fa));
        textList.add(Text.literal("Server List: " + serverListPlayerCount).withColor(0x7fe1fa));
        if (difference != 0) {
            textList.add(Text.literal(difference + " in vanish!").withColor(0xf7554f));
        }
        hudTextList.setTextList(textList);
    }

    @Override
    public void renderHUD(DrawContext context, RenderTickCounter tickCounter) {
        updateModTrackerHud();
    }

    @Override
    public void onChatMessage(Text message, CallbackInfo ci) {
        String messageText = message.getString();
        Matcher matcher = Pattern.compile("^You are currently connected to (.*).", Pattern.CASE_INSENSITIVE).matcher(messageText);
        if (matcher.find()) { currentNode = matcher.group(1); }
        if (!Pattern.compile("^Available servers: ", Pattern.CASE_INSENSITIVE).matcher(messageText).find()) { return; }
        //Mod.log(message.toString());

        List<Text> textList =  message.getSiblings();
        for (int i = 2; i < textList.size(); i++) {
            Text text = textList.get(i);
            String content = text.getString();
            if (content.equals(", ")) continue;
            HoverEvent hover = text.getStyle().getHoverEvent();
            Text hoverValue = (Text) hover.getValue(hover.getAction());
            Text data = hoverValue.getSiblings().get(1).getSiblings().get(0);

            int playerCount = Integer.valueOf(data.getString());
            serverList.put(content, playerCount);
        }

        currentNodePlayerCount = Mod.MC.getNetworkHandler().getListedPlayerListEntries().size();
        serverListPlayerCount = serverList.get(currentNode);
    }

}
