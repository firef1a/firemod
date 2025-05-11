package dev.fire.event;

import dev.fire.Mod;
import dev.fire.screens.CTPScreen;
import dev.fire.screens.HudFeatureMoveScreen;
import dev.fire.screens.PTPScreen;
import dev.fire.utils.ChatUtils;
import dev.fire.utils.ServerVerifier;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY = Mod.MOD_NAME;
    public static KeyBinding openMenuKeybinding, openPTPKeybinding, openCTPKeybinding;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Screen cScreen = Mod.getCurrentScreen();

            if (openMenuKeybinding.wasPressed()) {
                if (!HudFeatureMoveScreen.isOpen()) {
                    Mod.setCurrentScreen(new HudFeatureMoveScreen(Text.literal("HUD Config Screen"), cScreen));
                }
            }

            if (openPTPKeybinding.wasPressed()) {
                if (!ServerVerifier.isPlayingDiamondfire())  {
                    ChatUtils.displayMessage(Text.literal("You are not on mcdiamondfire.com."));
                } else if (!PTPScreen.isOpen()) {
                    Mod.setCurrentScreen(new PTPScreen(Text.literal("PTP Screen"), cScreen));
                }
            }
            if (openCTPKeybinding.wasPressed()) {
                if (!ServerVerifier.isPlayingDiamondfire())  {
                    ChatUtils.displayMessage(Text.literal("You are not on mcdiamondfire.com."));
                } else if (!(CTPScreen.isOpen())) {
                    Mod.setCurrentScreen(new CTPScreen(Text.literal("CTP Screen"), cScreen));
                }
            }
        });
    }


    public static void register() {
        openMenuKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Open HUD Config Screen", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_Y, // The keycode of the key
                KEY_CATEGORY // The translation key of the keybinding's category.
        ));

        openPTPKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Open PTP Menu", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_G, // The keycode of the key
                KEY_CATEGORY // The translation key of the keybinding's category.
        ));
        openCTPKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Open CTP Menu", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_H, // The keycode of the key
                KEY_CATEGORY // The translation key of the keybinding's category.
        ));

        registerKeyInputs();

    }
}