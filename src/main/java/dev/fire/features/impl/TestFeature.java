package dev.fire.features.impl;

import dev.fire.features.Feature;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

public class TestFeature extends Feature {
    public TestFeature() {

    }
    @Override
    public void tooltip(ItemStack item, Item.TooltipContext context, TooltipType type, List<Text> textList) {
        textList.add(Text.literal("real"));
    }
}
