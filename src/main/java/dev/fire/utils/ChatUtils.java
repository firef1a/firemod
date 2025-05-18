package dev.fire.utils;

import dev.fire.Mod;
import net.minecraft.text.Text;

import java.util.Objects;

public class ChatUtils {
    public static void sendMessage(String content) {
        if (content.charAt(0) == '/') {
            Objects.requireNonNull(Mod.MC.getNetworkHandler()).sendChatCommand(content.substring(1));
        } else {
            Objects.requireNonNull(Mod.MC.getNetworkHandler()).sendChatMessage(content);
        }
    }
}
