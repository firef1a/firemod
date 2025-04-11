package dev.fire.features.ext;

import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;

public class PacketHandler {
    public static void handlePacket(Packet<?> packet, CallbackInfo ci) {

    }
}
