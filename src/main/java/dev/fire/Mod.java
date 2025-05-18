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
import dev.fire.features.chat.SessionEntry;
import dev.fire.features.chat.SessionQuestionHud;
import dev.fire.features.chat.chathud.SChatHud;
import dev.fire.features.chat.chathud.SupportChatHud;
import dev.fire.helper.CommandQueue;
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
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static com.ibm.icu.text.PluralRules.Operand.f;


public class Mod implements ClientModInitializer {
	public static final String MOD_NAME = "SupportUtils";
	public static final String MOD_ID = "supportutils";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final MinecraftClient MC = MinecraftClient.getInstance();
	public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static final int LRED = 0xf5675b;

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
			dispatcher.register(ClientCommandManager.literal("clearqueue").then(ClientCommandManager.argument("player", StringArgumentType.string()).executes(Mod::clearQueue)));
			dispatcher.register(ClientCommandManager.literal("clearquestion").then(ClientCommandManager.argument("player", StringArgumentType.string()).executes(Mod::clearQuestions)));
			dispatcher.register(ClientCommandManager.literal("stats").then(ClientCommandManager.argument("player", StringArgumentType.string()).executes(Mod::sendStatsCommand)));
		});

		CommandRegistrationCallback.EVENT.register(((CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) -> {

		}));

		System.setProperty("java.awt.headless", "false");

		MOD_VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).isPresent() ? FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString() : null;

        LOGGER.info("{} is not in the sudoers file, sending nukes now... 3... 2... 1...", getPlayerName());
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

	private static int clearQuestions(CommandContext<FabricClientCommandSource> context) {
		String player_name = StringArgumentType.getString(context, "player");
		if (SessionQuestionHud.supportQuestions.containsKey(player_name )) {
			SessionQuestionHud.supportQuestions.remove(player_name);
			Mod.displayMessage(Text.literal("Removed " + player_name + " from support questions cache"), true, true);
		} else {
			Mod.displayMessage(Text.literal( player_name + " is not in the support questions cache").withColor(LRED), true, true);
		}


		return 1;
	}

	private static int clearQueue(CommandContext<FabricClientCommandSource> context) {
		String player_name = StringArgumentType.getString(context, "player");
		SessionEntry remove = null;
		for (SessionEntry sessionEntry : SessionQuestionHud.sessionQueue) {
			if (sessionEntry.name == player_name) {
				remove = sessionEntry;
				break;
			}
		}
		if (remove != null) {
			SessionQuestionHud.sessionQueue.remove(remove);
			Mod.displayMessage(Text.literal("Removed " + player_name + " from support session cache"), true, true);
		} else {
			Mod.displayMessage(Text.literal(player_name + " is not in the support session cache").withColor(LRED), true, true);
		}
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
	public static void displayMessage(Text content) {
		if (Mod.MC.player != null) {
			Mod.MC.player.sendMessage(
					Text.literal("[SUPUTIL]").withColor(0x63a4f2)
							.append(
									Text.literal(" ")
											.append(content)
											.withColor(Colors.LIGHT_GRAY)
							), false);
		}
	}

	public static void displayMessage(Text content, boolean isSupport, boolean prefix) {
		if (isSupport) {
			if (prefix) {
				SupportChatHud.hud.addMessage(Text.literal("[SUPUTIL] ").withColor(0x63a4f2).append((content.copy().withColor(Colors.LIGHT_GRAY))));
			} else {
				SupportChatHud.hud.addMessage(content);
			}
		} else {
			if (Mod.MC.player != null) {
				Text t = Text.empty();
				if (prefix) Text.literal("[SUPUTIL]").withColor(0x63a4f2);
				Mod.MC.player.sendMessage(
						t.copy().append(
								Text.literal(" ")
										.append(content)
										.withColor(Colors.LIGHT_GRAY)
						), false);
			}
		}
	}

	public static void log(String msg) { Mod.LOGGER.info(msg); }

}