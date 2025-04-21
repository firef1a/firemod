package dev.fire.features.player;

import com.google.gson.JsonObject;
import dev.fire.config.Config;
import dev.fire.features.Feature;
import dev.fire.features.FeatureHudObjects;
import dev.fire.render.ARGB;
import dev.fire.render.Alignment;
import dev.fire.render.Scaler;
import dev.fire.render.hudElements.ColorRectFeatureContainer;
import dev.fire.render.hudElements.RenderPaperDoll;

public class PaperDollFeature extends Feature {
    private static ColorRectFeatureContainer hudContainer;
    private static RenderPaperDoll paperDoll;
    public PaperDollFeature() {
        init("paperdoll", "Player Paper Doll", "Paper doll of your minecraft avatar!");
        Scaler hudPosition = Scaler.fromJsonOrDefault(getFeatureID() + ".paperdoll", Config.configJSON, new Scaler(0.015,0.015));

        hudContainer = new ColorRectFeatureContainer(hudPosition, 0, new ARGB(0,0), 0, Alignment.NONE, Alignment.NONE, this);
        paperDoll = new RenderPaperDoll(new Scaler(0,0), new Scaler(0.12527314814, 0.14503703703), 0, Alignment.NONE, Alignment.NONE, true);
        hudContainer.addSibling(paperDoll);

        FeatureHudObjects.registerObject(hudContainer);
    }


    @Override
    public void saveConfig(JsonObject jsonObject) {
        hudContainer.position.saveConfig(getFeatureID() + ".paperdoll", jsonObject);
    }
}
