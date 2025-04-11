package dev.fire;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import dev.fire.features.Features;
import dev.fire.features.FeatureImpl;
import dev.fire.features.Features;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.KeyBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Mod implements ClientModInitializer {
	public static final String MOD_NAME = "firemod";
	public static final String MOD_ID = "firemod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final MinecraftClient MC = MinecraftClient.getInstance();
	public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	public static final TextRenderer textRenderer = Mod.MC.textRenderer;
	private static KeyBinding keyBinding;

	@Override
	public void onInitializeClient() {
		Features.init();

		ClientTickEvents.START_CLIENT_TICK.register(client -> { Features.implement(FeatureImpl::tick); });
		ItemTooltipCallback.EVENT.register(((itemStack, tooltipContext, tooltipType, list) -> Features.implement(feature -> feature.tooltip(itemStack, tooltipContext, tooltipType, list))));
		HudRenderCallback.EVENT.register((draw, tickCounter) -> {Features.implement(feature -> feature.renderHUD(draw, tickCounter));});
		WorldRenderEvents.LAST.register(event -> {Features.implement(feature -> {feature.renderWorld(event);});});
		ClientLifecycleEvents.CLIENT_STARTED.register(client -> { Features.implement(feature -> {feature.clientStart(client);});});
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
			Features.implement(feature -> {feature.clientStop(client);});
			Mod.clientStopping();
		});

		LOGGER.info("making it 50x easier to macro since when i wrote this garbage");
	}
	public static void clientStopping() {
		log("stopping");
	}

	public static void log(String msg) {
		Mod.LOGGER.info(msg);
	}

}