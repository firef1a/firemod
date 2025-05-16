package dev.fire.mixin.network;

import dev.fire.Mod;
import dev.fire.features.Features;
import dev.fire.helper.CommandQueueHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.packet.c2s.common.ClientOptionsC2SPacket;
import net.minecraft.network.packet.c2s.config.SelectKnownPacksC2SPacket;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;

import java.util.Collections;
import java.util.Map;

@Mixin(ClientConnection.class)
public abstract class MClientConnection {
    @Shadow protected abstract void sendImmediately(Packet<?> packet, @Nullable PacketCallbacks callbacks, boolean flush);

    @Shadow public abstract void flush();

    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void handlePacket(Packet<T> packet, net.minecraft.network.listener.PacketListener listener, CallbackInfo ci) {
        if (packet instanceof GameMessageS2CPacket(Text content, boolean overlay)) {
            if (content.getString().equals("◆ Welcome back to DiamondFire! ◆")) { CommandQueueHelper.addCurrentTimestamp(1500L); }
            Features.implement(feature -> feature.onChatMessage(content, ci));
        }
        //Mod.log(packet.getPacketId().toString());
        Features.implement(feature -> feature.handlePacket(packet, ci));
    }

    @ModifyVariable(method = "handlePacket", at = @At("HEAD"), argsOnly = true)
    private static <T extends PacketListener> Packet<T> handlePacket(Packet<T> packet) {
        if (packet instanceof GameMessageS2CPacket(Text content, boolean overlay)) {
            Text new_context = Features.editChatMessage(content);
            return (Packet<T>) new GameMessageS2CPacket(new_context, overlay);
        }
        return packet;
    }

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;Z)V", at = @At("HEAD"), cancellable = true)
    private <T extends PacketListener> void handlePacket(Packet<?> packet, @Nullable PacketCallbacks callbacks, boolean flush, CallbackInfo ci) {
        Features.implement(feature -> feature.sendPacket(packet, ci));
    }
}
