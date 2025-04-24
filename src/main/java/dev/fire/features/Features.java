package dev.fire.features;

import dev.fire.features.chat.ModVanishTracker;
import dev.fire.features.chat.SessionQuestionHud;
import dev.fire.features.chat.chathud.SupportChatHud;
import dev.fire.features.commands.CommandAliaser;
import dev.fire.features.commands.CommandHider;
import dev.fire.features.item.ItemLoreViewer;
import dev.fire.features.item.ItemTagViewer;
import dev.fire.features.commands.QueueOnJoin;
import dev.fire.features.player.PaperDollFeature;
import dev.fire.features.plot.CTPTracker;
import dev.fire.features.plot.PTPTracker;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Features {
    public static Map<String, Feature> featureMap = new HashMap<>();

    public static void init() {
        add(new FeatureHudObjects());
        add(new ItemTagViewer());
        //add(new CPUDisplay());
        add(new SessionQuestionHud());
        add(new QueueOnJoin());
        add(new ItemLoreViewer());
        //add(new ModVanishTracker());
        add(new CommandHider());
        //add(new PaperDollFeature());
        add(new PTPTracker());
        add(new CTPTracker());
        add(new SupportChatHud());
        add(new CommandAliaser());
    }

    private static void add(Feature feature) { featureMap.put(feature.getFeatureID(), feature); }
    public static void implement(Consumer<FeatureImpl> consumer) { featureMap.values().forEach((feature -> {if (feature.isEnabled()) consumer.accept(feature);})); }
    public static Text editChatMessage(Text base) {
        Text modified = base;
        for (Feature feature : featureMap.values()) {
            if (feature.isEnabled()) {
                modified = feature.modifyChatMessage(base, modified);
            }
        }
        return modified;
    }
}
