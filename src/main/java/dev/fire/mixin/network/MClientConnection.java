package dev.fire.mixin.network;

import dev.fire.Mod;
import dev.fire.features.Features;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;

import java.io.IOException;

@Mixin(ClientConnection.class)
public class MClientConnection {
    @Inject(method = "handlePacket", at = @At("HEAD"))
    private static <T extends PacketListener> void handlePacket(Packet<T> packet, net.minecraft.network.listener.PacketListener listener, CallbackInfo ci) throws IOException {
        if (packet instanceof ChatMessageS2CPacket chatMessageS2CPacket) {
            Mod.log(chatMessageS2CPacket.body().content());
        }
        Features.implement(feature -> feature.handlePacket(packet, ci));
    }
}
