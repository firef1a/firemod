package dev.fire.features.impl;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import dev.fire.Mod;
import dev.fire.features.Feature;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.text.Text;
import org.intellij.lang.annotations.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.ComponentKey;

import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class TestFeature extends Feature {
    public TestFeature() {

    }

    private static NbtCompound encodeStack(ItemStack stack, DynamicOps<NbtElement> ops) {
        DataResult<NbtElement> result = ComponentChanges.CODEC.encodeStart(ops, stack.getComponentChanges());
        result.ifError(e->{

        });
        NbtElement nbtElement = result.getOrThrow();
        // cast here, as soon as this breaks, the mod will need to update anyway
        return (NbtCompound) nbtElement;
    }

    @Override
    public void tooltip(ItemStack item, Item.TooltipContext context, TooltipType type, List<Text> textList) {
        if (context.getRegistryLookup() == null) return;
        NbtCompound nbt = encodeStack(item, context.getRegistryLookup().getOps(NbtOps.INSTANCE));

        int flagCmdColor = 0x858dd6;

        var mcdata = nbt.getCompound("minecraft:custom_data");
        var bukkitvalues = mcdata.getCompound("PublicBukkitValues");
        if (bukkitvalues != null) {
            Set<String> keys = bukkitvalues.getKeys();
            if (!keys.isEmpty()) {
                textList.add(Text.empty());
                for (String key : keys) {
                    int keyColor = 0xdbb0f5;//0xb785d6;
                    int valueColor = 0xb3ddff;//0x96d0ff;//0x6fd6f2;
                    String value = bukkitvalues.get(key).toString();
                    if ((!(value.startsWith("\"") && value.endsWith("\""))) && !(value.startsWith("'") && value.endsWith("'"))) {
                        valueColor = 0xeb4b4b;
                    }
                    Text addText = Text.literal(key.substring(10) + ": ").withColor(keyColor).append(Text.literal(value).withColor(valueColor));
                    textList.add(addText);
                }
            }
        }
        var cmd = nbt.get("CustomModelData");
        var flags = nbt.get("HideFlags");
        if (cmd != null || flags != null) {
            textList.add(Text.empty());
            if (cmd != null) { textList.add(Text.literal("CustomModelData: ").withColor(flagCmdColor).append(Text.literal(cmd.toString()).withColor(0xeb4b4b))); }
            if (flags != null) { textList.add(Text.literal("HideFlags: ").withColor(flagCmdColor).append(Text.literal(flags.toString()).withColor(0xeb4b4b))); }
        }

    }
}
