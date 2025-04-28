package dev.fire.features.chat.chathud;

import dev.fire.Mod;
import dev.fire.features.Feature;
import dev.fire.features.Features;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class SupportChatHud extends Feature {
    public static SChatHud hud;
    public static int tick;
    public SupportChatHud() {
        init("supportchathud", "Support Chat Hud", "Seperate HUD element for support staff");

    }

    @Override
    public void onChatMessage(Text message, CallbackInfo ci) {
        String text = message.getString();
        if (text.startsWith("[SUPPORT]")) {
            ci.cancel();
            hud.addMessage(message);
        }
    }


    @Override
    public void tick() {
        tick++;
        if (hud == null) hud = new SChatHud(Mod.MC);
    }
    @Override
    public void renderHUD(DrawContext context, RenderTickCounter tickCounter) {
        if (!this.isEnabled()) return;
        if (!(Mod.MC.currentScreen instanceof ChatScreen)) {
            hud.render(context, tick, 0,0, Mod.MC.inGameHud.getChatHud().isChatFocused());
        }

    }
}
