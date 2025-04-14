package dev.fire.mixin.hud;

import dev.fire.Mod;
import dev.fire.features.Feature;
import dev.fire.features.Features;
import dev.fire.helper.CommandQueue;
import dev.fire.helper.CommandQueueHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.python.antlr.ast.Call;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class MChatHud {
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"))
    public void addMessage(Text message, CallbackInfo ci) {
        if (message.getString().equals("◆ Welcome back to DiamondFire! ◆")) { CommandQueueHelper.addCurrentTimestamp(1250L); }
        Features.implement(feature -> { feature.onChatMessage(message, ci); });
    }

    @ModifyVariable(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public Text inject(Text message) {
        return Features.editChatMessage(message);
    }
}