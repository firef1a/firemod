package dev.fire.features.item;

import com.google.gson.JsonObject;
import dev.fire.Mod;
import dev.fire.config.Config;
import dev.fire.features.Feature;
import dev.fire.features.FeatureHudObjects;
import dev.fire.render.ARGB;
import dev.fire.render.Alignment;
import dev.fire.render.Scaler;
import dev.fire.render.impl.ColorRectFeatureContainer;
import dev.fire.render.impl.TextList;
import dev.fire.render.impl.TooltipObject;
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
    private static TooltipObject itemLoreViewerTooltip;
    private static ColorRectFeatureContainer hudContainer;

    public ItemLoreViewer() {
        init("itemloreviewer", "Item Lore Viewer", "Lets you view tooltips of items on your hud.");

        Scaler hudPosition = Scaler.fromJsonOrDefault(getFeatureID() + ".loreviewer", Config.configJSON, new Scaler(0.0390625, 0.041666666666666664));

        hudContainer = new ColorRectFeatureContainer(hudPosition, 0, new ARGB(0,0), 0, Alignment.NONE, Alignment.NONE, this);
        itemLoreViewerTooltip = new TooltipObject(new Scaler(0,0), 0, Alignment.NONE, Alignment.NONE, true);
        hudContainer.addSibling(itemLoreViewerTooltip);
        FeatureHudObjects.registerObject(hudContainer);
    }

    @Override
    public void saveConfig(JsonObject jsonObject) {
        hudContainer.position.saveConfig(getFeatureID() + ".loreviewer", jsonObject);
    }

    @Override
    public void renderHUD(DrawContext context, RenderTickCounter tickCounter) {
        if (Mod.MC.player == null) {
            itemLoreViewerTooltip.setEnabled(false);
            return;
        };
        PlayerInventory inventory = Mod.MC.player.getInventory();
        ItemStack main = inventory.getMainHandStack();

        if (main.isEmpty() || Mod.MC.textRenderer == null) {
            itemLoreViewerTooltip.setEnabled(false);
            return;
        };
        itemLoreViewerTooltip.setEnabled(isEnabled());

        List<Text> tooltip = Screen.getTooltipFromItem(Mod.MC.gameRenderer.getClient(), main);
        ArrayList<Text> modTooltip = new ArrayList<>();

        for (int i = 0; i < tooltip.size(); i++) {
            if (i < 20) {
                modTooltip.add(tooltip.get(i));
            } else {
                modTooltip.add(Text.literal(tooltip.size()-20 + " more lines...").withColor(Colors.GRAY));
                break;
            }
        }

        itemLoreViewerTooltip.setTextList(modTooltip);
        //context.drawTooltip(Mod.MC.textRenderer, modTooltip, position.getScreenX(), position.getScreenY());
        itemLoreViewerTooltip.setEnabled(true);
    }

}
