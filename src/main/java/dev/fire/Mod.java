package dev.fire;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.fire.config.Config;
import dev.fire.event.KeyInputHandler;
import dev.fire.features.FeatureImpl;
import dev.fire.features.Features;
import dev.fire.helper.CommandQueueHelper;
import dev.fire.utils.ChatUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;


public class Mod implements ClientModInitializer {
	public static final String MOD_NAME = "SupportUtils";
	public static final String MOD_ID = "supportutils";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final MinecraftClient MC = MinecraftClient.getInstance();
	public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static String MOD_VERSION;

	@Override
	public void onInitializeClient() {
		Config.loadConfig();
		Features.init();
		KeyInputHandler.register();

		ClientTickEvents.START_CLIENT_TICK.register(client -> {
			Features.implement(FeatureImpl::tick);
			CommandQueueHelper.tick();
		});
		ItemTooltipCallback.EVENT.register(((itemStack, tooltipContext, tooltipType, list) -> Features.implement(feature -> feature.tooltip(itemStack, tooltipContext, tooltipType, list))));
		HudRenderCallback.EVENT.register((draw, tickCounter) -> Features.implement(feature -> feature.renderHUD(draw, tickCounter)));
		WorldRenderEvents.LAST.register(event -> Features.implement(feature -> feature.renderWorld(event)));
		ClientLifecycleEvents.CLIENT_STARTED.register(client -> Features.implement(feature -> feature.clientStart(client)));
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
			Features.implement(feature -> feature.clientStop(client));
			Mod.clientStopping();
		});

		ServerPlayConnectionEvents.INIT.register((networkHandler, minecraftServer) -> Features.implement(feature -> feature.serverConnectInit(networkHandler, minecraftServer)));
		ServerPlayConnectionEvents.JOIN.register((event, sender, minecraftServer) -> Features.implement(feature -> feature.serverConnectJoin(event, sender, minecraftServer)));
		ServerPlayConnectionEvents.DISCONNECT.register((networkHandler, minecraftServer) -> Features.implement(feature -> feature.serverConnectDisconnect(networkHandler, minecraftServer)));

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("queue").executes(Mod::sendQueueCommand));
			dispatcher.register(ClientCommandManager.literal("stats").then(ClientCommandManager.argument("player", StringArgumentType.string()).executes(Mod::sendStatsCommand)));
		});

		CommandRegistrationCallback.EVENT.register(((CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) -> {

		}));

		System.setProperty("java.awt.headless", "false");

		MOD_VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).isPresent() ? FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString() : null;

		LOGGER.info(getPlayerName() + " is in the sudoers file, sending nukes now... 3... 2... 1...");
	}

	public static String getPlayerName() { return Mod.MC.getSession().getUsername(); }
	public static UUID getPlayerUUID() { return Mod.MC.getSession().getUuidOrNull(); }

	private static int sendQueueCommand(CommandContext<FabricClientCommandSource> context) {
		ChatUtils.sendMessage("/support queue");
		return 1;
	}

	private static int sendStatsCommand(CommandContext<FabricClientCommandSource> context) {
		String player_name = StringArgumentType.getString(context, "player");
		ChatUtils.sendMessage("/support stats " + player_name);
		return 1;
	}

	public static Screen getCurrentScreen() { return Mod.MC.currentScreen; }

    public static void setCurrentScreen(Screen screen) { Mod.MC.setScreen(screen); }
	public static int getWindowWidth() {
		return Mod.MC.getWindow().getScaledWidth();
	}
	public static int getWindowHeight() {
		return Mod.MC.getWindow().getScaledHeight();
	}

	public static void clientStopping() {
		log("stopping");
	}

	public static void log(String msg) { Mod.LOGGER.info(msg); }

}