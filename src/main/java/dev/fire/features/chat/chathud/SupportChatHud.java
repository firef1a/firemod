package dev.fire.features.chat.chathud;

import dev.fire.Mod;
import dev.fire.features.Feature;
import dev.fire.features.Features;
import dev.fire.features.chat.SessionEntry;
import dev.fire.features.chat.SupportQuestion;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SupportChatHud extends Feature {
    public static SChatHud hud;
    public static int tick;
    public SupportChatHud() {
        init("supportchathud", "Support Chat Hud", "Seperate HUD element for support staff");

    }

    @Override
    public void onChatMessage(Text message, CallbackInfo ci) {
        String text = message.getString();

        ArrayList<Pattern> patterns = new ArrayList<>(List.of(
            Pattern.compile("^» Support Question: \\(Click to answer\\)\\nAsked by (.{3,16}) (.{3,16})\\n(.*)"),
            Pattern.compile("^ {39}\\n» (.{3,16}) has answered (.{3,16})'(?:s|) question:\\n\\n.*\\n {39}"),
            Pattern.compile("^#\\d* (.{3,16}) ▶ (\\d*):(\\d*):(\\d*)"),
            Pattern.compile("^ {2}▶ Reason: (.*)"),
            Pattern.compile("^\\[SUPPORT]"),
            Pattern.compile("^» Current Queue:"),
            Pattern.compile("^» \\d*\\. .{3,16}\\(\\d* sessions\\)")
        ));

        for (Pattern p : patterns) {
            if (p.matcher(text).find()) {
                ci.cancel();
                hud.addMessage(message);
            }
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
