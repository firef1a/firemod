package dev.fire.mixin.hud;

import dev.fire.Mod;
import dev.fire.features.Feature;
import dev.fire.features.Features;
import dev.fire.features.chat.chathud.SChatHud;
import dev.fire.features.chat.chathud.SupportChatHud;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.Toast;
import net.minecraft.text.Style;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class MChatScreen {
    @Inject(method = "mouseScrolled", at = @At("HEAD"), cancellable = true)
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount, CallbackInfoReturnable<Boolean> cir) {
        if (!Features.featureMap.get("supportchathud").isEnabled) return;
        if (mouseX > (double) Mod.getWindowWidth() /2) {
            SupportChatHud.hud.scroll((int) verticalAmount);
            cir.cancel();
        }
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!Features.featureMap.get("supportchathud").isEnabled) return;
        SChatHud hud = SupportChatHud.hud;
        hud.render(context, SupportChatHud.tick, mouseX, mouseY, true);
        MessageIndicator messageIndicator = hud.getIndicatorAt(mouseX, mouseY);
        if (messageIndicator != null && messageIndicator.text() != null) {
            context.drawOrderedTooltip(Mod.MC.textRenderer, Mod.MC.textRenderer.wrapLines(messageIndicator.text(), 210), mouseX, mouseY);
        } else {
            Style style = this.getTextStyleAt(mouseX, mouseY);
            if (style != null && style.getHoverEvent() != null) {
                context.drawHoverEvent(Mod.MC.textRenderer, style, mouseX, mouseY);
            }
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (!Features.featureMap.get("supportchathud").isEnabled) return;
        if (button == 0) {
            if (SupportChatHud.hud.mouseClicked(mouseX, mouseY)) {
                cir.setReturnValue(true);
                return;
            };
        }
    }

    @Unique
    @Nullable
    private Style getTextStyleAt(double x, double y) {
        return SupportChatHud.hud.getTextStyleAt(x, y);
    }

}
