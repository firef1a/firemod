package dev.fire.features;

import dev.fire.Mod;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

public abstract class Feature implements FeatureImpl {
    private static String featureName;
    private static boolean isEnabled = true;

    public String getName() { return featureName; }
    public boolean isEnabled() { return isEnabled; }
    public void setIsEnabled(boolean enabled) { isEnabled = enabled; }
    public void tick() { }
    public void tooltip(ItemStack item, Item.TooltipContext context, TooltipType type, List<Text> textList) {}
    public void renderWorld(WorldRenderContext worldRenderContext) { }
    public void renderHUD(DrawContext context, RenderTickCounter tickCounter) { }
    public void handlePacket(Packet<?> packet, CallbackInfo ci) { }
    public void sentPacket(Packet<?> packet, CallbackInfo ci) { }
    public Text modifyChatMessage(Text base, Text modified) { return modified;}
    public void clientStart(MinecraftClient minecraftClient) { }
    public void clientStop(MinecraftClient minecraftClient) { }

    private void log(String msg) { Mod.log(msg); }
}
