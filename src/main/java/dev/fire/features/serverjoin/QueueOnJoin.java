package dev.fire.features.serverjoin;

import dev.fire.Mod;
import dev.fire.features.Feature;
import dev.fire.utils.ChatUtils;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;

public class QueueOnJoin extends Feature {
    public QueueOnJoin() {
        init("queueonjoin", "/queue on join");
    }

    public void onChatMessage(Text message, CallbackInfo ci) {
        String msg = message.getString();
        if (msg.equals("◆ Welcome back to DiamondFire! ◆")) {
            ChatUtils.sendMessage("/support queue");
            Mod.log("Attemping to run /support queue");
        }
    }
}
