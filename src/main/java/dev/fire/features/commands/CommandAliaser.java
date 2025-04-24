package dev.fire.features.commands;

import com.mojang.brigadier.tree.RootCommandNode;
import dev.fire.Mod;
import dev.fire.features.Feature;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.command.CommandManager;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.packet.s2c.play.CommandTreeS2CPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;

public class CommandAliaser extends Feature {
    public CommandAliaser() {
        init("commandaliaser", "Command Autoaliaser", "Manages command aliasing via packet manipulation");
    }

    @Override
    public void handlePacket(Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof CommandTreeS2CPacket commandTreeS2CPacket) {
            //Mod.log(commandTreeS2CPacket.getCommandTree());
        }
    }

    @Override
    public void sendPacket(Packet<?> packet, CallbackInfo ci) {
    }
}
