package dev.fire.features.item;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import dev.fire.features.Feature;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ItemTagViewer extends Feature {
    private static int keyColor, valueColor, flagCmdColor, flagCmdColorValue;

    public ItemTagViewer() {
        init("itemtagviewer", "Item Tag Viewer", "Shows hypercube item tags in item lore, when combined with Item Lore Viewer, long item lores may be shortened.");
        this.keyColor = 0xdbb0f5;
        this.valueColor = 0xb3ddff;
        this.flagCmdColor = 0x959eed;
        this.flagCmdColorValue = 0xb2b8ed;
    }

    private static NbtCompound encodeStack(ItemStack stack, DynamicOps<NbtElement> ops) {
        DataResult<NbtElement> result = ComponentChanges.CODEC.encodeStart(ops, stack.getComponentChanges());
        NbtElement nbtElement = result.getOrThrow();
        return (NbtCompound) nbtElement;
    }

    @Override
    public void tooltip(ItemStack item, Item.TooltipContext context, TooltipType type, List<Text> textList) {
        textList = getTagTooltip(item, context, textList);
    }

    public List<Text> getTagTooltip(ItemStack item, Item.TooltipContext context, List<Text> textList) {
        if (context.getRegistryLookup() == null) return textList;
        NbtCompound nbt = encodeStack(item, context.getRegistryLookup().getOps(NbtOps.INSTANCE));
        //Mod.log(nbt.toString());

        NbtCompound mcData = nbt.getCompound("minecraft:custom_data");
        NbtCompound bukkitValues = mcData.getCompound("PublicBukkitValues");
        boolean hasTags = false;
        if (bukkitValues != null) {
            Set<String> keys = bukkitValues.getKeys();
            if (!keys.isEmpty()) {
                ArrayList<Text> appendText = new ArrayList<>();
                for (String key : keys) {
                    if (key.equals("hypercube:codetemplatedata") || key.equals("hypercube:varitem")) continue;
                    hasTags = true;
                    int keyColor = this.keyColor;//0xb785d6;
                    int valueColor = this.valueColor;//0x96d0ff;//0x6fd6f2;
                    NbtElement element = bukkitValues.get(key);
                    if (element != null) {
                        String value = element.toString();
                        if ((!(value.startsWith("\"") && value.endsWith("\""))) && !(value.startsWith("'") && value.endsWith("'"))) {
                            valueColor = 0xeb4b4b;
                        }
                        Text addText = Text.literal(key.substring(10) + ": ").withColor(keyColor).append(Text.literal(value).withColor(valueColor));
                        appendText.add(addText);
                    }
                }
                if (hasTags) {
                    appendText.addFirst(Text.empty());
                }
                textList.addAll(appendText);
            }
        }
        ArrayList<String> tags = new ArrayList<>(List.of(
                "custom_model_data",
                "max_stack_size",
                "enchantment_glint_override"
        ));
        ArrayList<Text> extTags = new ArrayList<>();
        for (String tag : tags) {
            NbtElement element = nbt.get("minecraft:" + tag);
            if (element != null) { extTags.add(Text.literal(tag + ": ").withColor(flagCmdColor).append(Text.literal(element.toString()).withColor(flagCmdColorValue))); }
        }
        if (!hasTags && !extTags.isEmpty()) {
            extTags.addFirst(Text.empty());
            textList.addAll(extTags);
        }

        return textList;
    }
}
