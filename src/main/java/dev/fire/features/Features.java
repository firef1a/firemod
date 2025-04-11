package dev.fire.features;

import dev.fire.features.chat.SupportAccept;
import dev.fire.features.item.ItemTagViewer;
import dev.fire.features.plot.CPUDisplay;
import dev.fire.features.serverjoin.QueueOnJoin;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Features {
    public static Map<String, FeatureImpl> featureMap = new HashMap<>();

    public static void init() {
        add(new ItemTagViewer());
        //add(new CPUDisplay());
        //add(new SupportAccept());
        add(new QueueOnJoin());
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
