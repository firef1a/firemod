package dev.fire.screens;

import dev.fire.Mod;
import dev.fire.features.plot.PTPState;
import dev.fire.features.plot.PTPTracker;
import dev.fire.helper.CommandQueue;
import dev.fire.helper.CommandQueueHelper;
import dev.fire.render.*;
import dev.fire.render.hudElements.ColorRect;
import dev.fire.render.hudElements.RenderObject;
import dev.fire.render.screenElements.Rect;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.python.antlr.ast.Str;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Map.entry;

public class PTPScreen extends Screen {
    public final Screen parentScreen;
    private ColorRect rect;
    private ArrayList<Rect> clickList;

    public PTPScreen(Text title, Screen parentScreen) {
        super(title);
        this.parentScreen = parentScreen;
        PTPTracker.requestPTP();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        record ClickPrototype(Text text, Consumer<Point2i> clickEffect) {};
        Point2i mouse = new Point2i(mouseX, mouseY);

        clickList = new ArrayList<>();

        //super.render(context, mouseX, mouseY, delta);
        HashMap<String, Integer> colorText = new HashMap<>(Map.ofEntries(
                entry("dev", 0xac82f5),
                entry("play", 0x82d4f5),
                entry("build", 0xf7bb60)
        ));

        ArrayList<ClickPrototype> dev = new ArrayList<>();
        ArrayList<ClickPrototype> build = new ArrayList<>();
        ArrayList<ClickPrototype> play = new ArrayList<>();


        for (String name : PTPTracker.playerData.keySet()) {
            String val = PTPTracker.playerData.get(name);
            Text text = Text.literal(name + " - " + val).withColor(colorText.get(val));
            int w = Mod.MC.textRenderer.getWidth(name + " - " + val)+40;

            Consumer<Point2i> effect = (point2i -> { CommandQueueHelper.addCommand(new CommandQueue("/ptp " + name)); });

            if (val.equals("dev")) { dev.add(new ClickPrototype(text, effect)); }
            if (val.equals("build")) { build.add(new ClickPrototype(text, effect)); }
            if (val.equals("play")) { play.add(new ClickPrototype(text, effect)); }
        }


        ArrayList<ClickPrototype> playerData = new ArrayList<>();
        playerData.addAll(dev);
        playerData.addAll(build);
        playerData.addAll(play);

        Text text = null;
        if (PTPTracker.ptpState.equals(PTPState.VALID)) { text = Text.literal( "Players: " + playerData.size()).withColor(0x7cbbf2); }
        if (PTPTracker.ptpState.equals(PTPState.SPAWN)) { text = Text.literal( "Error: You are not on a plot.").withColor(0xed6c4c); }
        if (PTPTracker.ptpState.equals(PTPState.NO_PERM)) { text = Text.literal( "Error: You do not have /ptp perms.").withColor(0xed6c4c); }
        playerData.addFirst(new ClickPrototype(text, null));

        Point2i size = new Scaler(0.105, 0.0225).getScreenPosition();

        int maxWidth = size.x;
        for (ClickPrototype clickPrototype : playerData) {
            int w = Mod.MC.textRenderer.getWidth(clickPrototype.text)+20;
            if (w > maxWidth) maxWidth = w;
        }

        size.x = maxWidth;

        Point2i middle = new Scaler(0.5, 0.5).getScreenPosition();
        Point2i base = middle.subtract(size.x/2, (size.y*playerData.size()) / 2);

        ARGB rectColor = new ARGB(0.5, 0x00000);
        ARGB borderColor = new ARGB(0.3, 0x3b97d4);
        ARGB borderHighlightColor = new ARGB(0.65, 0x88c8f2);

        int i = 0;
        for (ClickPrototype clickPrototype : playerData) {
            Point2i position = base.add(0, size.y * i);

            Rect drawRect = new Rect(position, size, rectColor, clickPrototype.clickEffect != null ? borderColor: null, clickPrototype.clickEffect != null ? borderHighlightColor : null, clickPrototype.clickEffect);
            clickList.add(drawRect);
            drawRect.setText(clickPrototype.text);
            drawRect.render(context, mouse);
            i++;
        }

    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Point2i mouse = new Point2i((int) mouseX, (int) mouseY);

        for (Rect rect : clickList) {
            if (rect.containsPoint(mouse)) {
                rect.onClick(mouse);
                close();
                break;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
    @Override
    public void close() {
        Mod.setCurrentScreen(parentScreen);
    }

    public static boolean isOpen() { return Mod.getCurrentScreen() instanceof PTPScreen; }

}
