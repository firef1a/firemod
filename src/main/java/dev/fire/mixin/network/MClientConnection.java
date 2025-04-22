package dev.fire.mixin.network;

import com.mojang.brigadier.suggestion.Suggestion;
import dev.fire.Mod;
import dev.fire.features.Features;
import dev.fire.helper.CommandQueueHelper;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.message.FilterMask;
import net.minecraft.network.message.MessageBody;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Mixin(ClientConnection.class)
public class MClientConnection {
    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void handlePacket(Packet<T> packet, net.minecraft.network.listener.PacketListener listener, CallbackInfo ci) {
        if (packet instanceof GameMessageS2CPacket(Text content, boolean overlay)) {
            if (content.getString().equals("◆ Welcome back to DiamondFire! ◆")) { CommandQueueHelper.addCurrentTimestamp(1500L); }
            Features.implement(feature -> feature.onChatMessage(content, ci));
        }
        //Mod.log(packet.getPacketId().toString());
        Features.implement(feature -> feature.handlePacket(packet, ci));
    }
}
