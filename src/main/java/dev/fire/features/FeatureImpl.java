package dev.fire.features;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.security.auth.callback.Callback;
import java.util.List;

public interface FeatureImpl {
    String getName();
    boolean isEnabled();
    void setIsEnabled(boolean enabled);

    void tick();
    void tooltip(ItemStack item, Item.TooltipContext context, TooltipType type, List<Text> textList);
    void renderWorld(WorldRenderContext worldRenderContext);

    void renderHUD(DrawContext context, RenderTickCounter tickCounter);
    void handlePacket(Packet<?> packet, CallbackInfo ci);
    void sentPacket(Packet<?> packet, CallbackInfo ci);

    Text modifyChatMessage(Text base, Text modified);
    void clientStart(MinecraftClient minecraftClient);
    void clientStop(MinecraftClient minecraftClient);
}
