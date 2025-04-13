package dev.fire.features.item;

import dev.fire.Mod;
import dev.fire.features.Feature;
import dev.fire.render.Scaler;
import dev.fire.utils.ColorUtils;
import net.minecraft.block.Block;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.AirBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemLoreViewer extends Feature {
    public ItemLoreViewer() {
        init("itemloreviewer", "Item Lore Viewer");
    }
    private Scaler position = new Scaler(0.0390625, 0.041666666666666664);

    @Override
    public void renderHUD(DrawContext context, RenderTickCounter tickCounter) {
        if (Mod.MC.player == null) return;
        PlayerInventory inventory = Mod.MC.player.getInventory();
        ItemStack main = inventory.getMainHandStack();

        if (main.isEmpty()) return;
        if (Mod.MC.textRenderer == null) return;

        List<Text> tooltip = Screen.getTooltipFromItem(Mod.MC.gameRenderer.getClient(), main);
        List<Text> modTooltip = new ArrayList<>();

        for (int i = 0; i < tooltip.size(); i++) {
            if (i < 20) {
                modTooltip.add(tooltip.get(i));
            } else {
                modTooltip.add(Text.literal(tooltip.size()-20 + " more lines...").withColor(Colors.GRAY));
                break;
            }
        }
        context.drawTooltip(Mod.MC.textRenderer, modTooltip, position.getScreenX(), position.getScreenY());
    }
}
