package dev.fire.event;

import dev.fire.Mod;
import dev.fire.screens.HudFeatureMoveScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY = Mod.MOD_NAME;
    public static KeyBinding openMenuKeybinding;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openMenuKeybinding.wasPressed()) {
                if (!(Mod.getCurrentScreen() instanceof HudFeatureMoveScreen)) {
                    HudFeatureMoveScreen screen = new HudFeatureMoveScreen(Text.literal("HUD Config Screen"), Mod.getCurrentScreen());
                    Mod.setCurrentScreen(screen);
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
        /*
        acceptLatestSupport = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Accept Latest Support", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_GRAVE_ACCENT, // The keycode of the key
                KEY_CATEGORY // The translation key of the keybinding's category.
        ));

         */
        registerKeyInputs();

    }
}