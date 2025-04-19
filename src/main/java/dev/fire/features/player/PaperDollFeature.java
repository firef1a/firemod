package dev.fire.features.player;

import com.google.gson.JsonObject;
import dev.fire.config.Config;
import dev.fire.features.Feature;
import dev.fire.features.FeatureHudObjects;
import dev.fire.render.Alignment;
import dev.fire.render.Scaler;
import dev.fire.render.impl.RenderPaperDoll;
import dev.fire.screens.HudFeatureMoveScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.util.ArrayList;

public class PaperDollFeature extends Feature {
    private static RenderPaperDoll paperDoll;
    public PaperDollFeature() {
        init("paperdoll", "Player Paper Doll", "Paper doll of your minecraft avatar!");
        Scaler hudPosition = Scaler.fromJsonOrDefault(getFeatureID() + ".paperdollContainer", Config.configJSON, new Scaler(0.015,0.015));

        paperDoll = new RenderPaperDoll(hudPosition, new Scaler(0.12527314814, 0.14503703703), 0, Alignment.NONE, Alignment.NONE, true);
        FeatureHudObjects.registerObject(paperDoll);
    }

    @Override
    public void renderHUD(DrawContext context, RenderTickCounter tickCounter) {
        paperDoll.setEnabled(isEnabled());
    }

    @Override
    public void saveConfig(JsonObject jsonObject) {
        paperDoll.position.saveConfig(getFeatureID() + ".paperdollContainer", jsonObject);
    }
}
