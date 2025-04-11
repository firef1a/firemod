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

    public static void displayMessage(Text content) {
        if (Mod.MC.player != null) {
            Mod.MC.player.sendMessage(
                    Text.literal("[" + Mod.MOD_NAME.toUpperCase() +"]").withColor(0xf57d5b)
                            .append(
                                    Text.literal(" ")
                                            .append(content)
                                            .withColor(0xfff1ed)
                            ), false);
        }
    }
}
