package dev.fire;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import dev.fire.features.Features;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Mod implements ClientModInitializer {
	public static final String MOD_NAME = "pyscript";
	public static final String MOD_ID = "pyscript";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final MinecraftClient MC = MinecraftClient.getInstance();
	public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	private static KeyBinding keyBinding;

	@Override
	public void onInitializeClient() {
		LOGGER.info("making it 50x easier to macro since when i wrote this garbage");


		//hClientLifecycleEvents.CLIENT_STOPPING.register(client -> {clientStop();});
		//ClientTickEvents.END_CLIENT_TICK.register((minecraftClient) -> {Features.onTick(minecraftClient);});
		//ItemTooltipCallback.EVENT.register(((itemStack, tooltipContext, tooltipType, list) -> (Features.tooltip(itemStack, tooltipContext, tooltipType, list))));

	}


	public static void consoleLog(String txt) {
		Mod.LOGGER.info(txt);
	}

	public static void clientStop() {
		Mod.consoleLog("Client Stopping!");
	}
}