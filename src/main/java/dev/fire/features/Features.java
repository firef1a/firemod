package dev.fire.features;

import dev.fire.features.chat.SupportFeatures;
import dev.fire.features.item.ItemLoreViewer;
import dev.fire.features.item.ItemTagViewer;
import dev.fire.features.commands.QueueOnJoin;
import dev.fire.render.Scaler;
import dev.fire.render.impl.RenderObject;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Features {
    public static Map<String, FeatureImpl> featureMap = new HashMap<>();

    public static void init() {
        add(new FeatureHudObjects());
        add(new ItemTagViewer());
        //add(new CPUDisplay());
        add(new SupportFeatures());
        add(new QueueOnJoin());
        add(new ItemLoreViewer());
    }

    private static void add(Feature feature) { featureMap.put(feature.getFeatureID(), feature); }
    public static void implement(Consumer<FeatureImpl> consumer) { featureMap.values().forEach((feature -> {if (feature.isEnabled()) consumer.accept(feature);})); }
    public static Text editChatMessage(Text base) {
        Text modified = base;
        for (FeatureImpl feature : featureMap.values()) {
            if (feature.isEnabled()) {
                modified = feature.modifyChatMessage(base, modified);
            }
        }
        return modified;
    }
}
