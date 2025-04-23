package dev.fire.screens;

import dev.fire.Mod;
import dev.fire.features.plot.CTPState;
import dev.fire.features.plot.CTPTracker;
import dev.fire.helper.CommandQueue;
import dev.fire.helper.CommandQueueHelper;
import dev.fire.render.ARGB;
import dev.fire.render.Point2i;
import dev.fire.render.Scaler;
import dev.fire.render.hudElements.ColorRect;
import dev.fire.render.screenElements.Rect;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.widget.EditBoxWidget;
import org.lwjgl.glfw.GLFW;

import static java.util.Map.entry;

public class CTPScreen extends Screen {
    public final Screen parentScreen;
    private ArrayList<Rect> clickList;
    private EditBoxWidget searchBox;
    private int numLines;
    private static double scrollAmount = 0, scrollTarget = 0;

    private final Point2i size;
    private final int xSize = 5;
    private final int ySize = 22;
    private final Point2i baseSize;
    private final Point2i base;

    private static final ArrayList<String> eventOrder = new ArrayList<>(List.of("event", "function", "process", "entity"));

    public CTPScreen(Text title, Screen parentScreen) {
        super(title);
        this.parentScreen = parentScreen;
        CTPTracker.sendCTPSuggestion();

        numLines = 0;
        size = new Scaler(0.135, 0.025).getScreenPosition();
        baseSize = new Point2i(size.x * xSize, size.y * ySize);
        Point2i middle = new Scaler(0.5, 0.5).getScreenPosition();
        base = middle.subtract((size.x*xSize)/2, (size.y*ySize) / 2);
    }

    @Override
    protected void init() {
        int height = textRenderer.fontHeight * 2;
        searchBox = new EditBoxWidget(Mod.MC.textRenderer, base.x, base.y - height, baseSize.x, height, Text.empty(), Text.empty());
        addDrawable(searchBox);
        setInitialFocus(searchBox);
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        record CTPPrototype(ArrayList<String> content, String eventId) {}
        Point2i mouse = new Point2i(mouseX, mouseY);

        clickList = new ArrayList<>();

        ARGB rectColor = new ARGB(0.5, 0x000000);
        record ColorTuple(ARGB borderColor, ARGB borderHighlightColor) {}

        double oD = 0.60;
        double hD = 1;
        HashMap<String, ColorTuple> colorTupleHashMap = new HashMap<>(Map.ofEntries(
                entry("event", new ColorTuple(new ARGB(oD, 0x48d5e8), new ARGB(hD, 0x8fe6f2))),
                entry("entity", new ColorTuple(new ARGB(oD, 0xf0ed46), new ARGB(hD, 0xf2f085))),
                entry("process", new ColorTuple(new ARGB(oD, 0x5bf056), new ARGB(hD, 0x98f595))),
                entry("function", new ColorTuple(new ARGB(oD, 0x699cf5), new ARGB(hD, 0x9dbffa)))
        ));

        boolean enabled = true;

        Rect baseRect = new Rect(base, baseSize, rectColor);
        if (CTPTracker.ctpState == CTPState.NO_PERM) {
            baseRect.setText(Text.literal("Error: You may not use /ctp right now.").withColor(0xed6c4c));
            enabled = false;
        }

        context.enableScissor(baseRect.getX1(), baseRect.getY1(), baseRect.getX2(), baseRect.getY2());
        baseRect.render(context, mouse);

        ArrayList<CTPPrototype> displayPrototypes = new ArrayList<>();
        /*
        Converts hashmap into an array
         */
        searchBox.setText(searchBox.getText().replace("\n", ""));
        for (String eventId : eventOrder) {
            ArrayList<String> dataList = new ArrayList<>();
            for (String data : CTPTracker.ctpResult.getOrDefault(eventId, new ArrayList<>())) {
                if (searchBox.getText().isEmpty() || data.toLowerCase().contains(searchBox.getText().toLowerCase())) dataList.add(data);
            }
            if (!dataList.isEmpty()) displayPrototypes.add(new CTPPrototype(dataList, eventId));
        }

        /*
        Draw each code line
         */
        numLines = 0;
        if (!displayPrototypes.isEmpty() && enabled) {
            int protoTypeIndex = 0;
            int eachIndex = 0;

            boolean finished = false;
            int ny = 0;
            while (!finished) {
                numLines++;
                for (int nx = 0; nx < xSize; nx++) {
                    Point2i position = base.add(size.multiply(nx, ny)).add(0, (int) -scrollAmount);
                    CTPPrototype prototype = displayPrototypes.get(protoTypeIndex);
                    String text = prototype.content.get(eachIndex);
                    String funcText = text;
                    if (funcText.length() > 27) funcText = funcText.substring(0, 24) + "...";

                    Rect drawRect = new Rect(position, size, new ARGB(0.3,0x000000), colorTupleHashMap.get(prototype.eventId).borderColor, colorTupleHashMap.get(prototype.eventId).borderHighlightColor);
                    drawRect.setText(Text.literal(funcText).withColor(colorTupleHashMap.get(prototype.eventId).borderHighlightColor.getRGB()));
                    String eventName = (prototype.eventId.equals("entity")) ? "event" : prototype.eventId;
                    drawRect.setClickEffect((m) -> CommandQueueHelper.addCommand(new CommandQueue("/ctp " + eventName + " " + text)));
                    clickList.add(drawRect);
                    drawRect.render(context, mouse);

                    if (eachIndex == prototype.content.size()-1) {
                        if (protoTypeIndex == displayPrototypes.size()-1) finished = true;
                        eachIndex = 0;
                        protoTypeIndex++;
                        break;
                    }
                    eachIndex++;
                }
                ny++;
            }
        }
        context.disableScissor();
        scrollAmount = scrollAmount + ((scrollTarget - scrollAmount) * (1.0 / 3.0));
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Point2i mouse = new Point2i((int) mouseX, (int) mouseY);

        for (Rect rect : clickList) {
            if (rect.containsPoint(mouse) && new Rect(base, baseSize).containsPoint(mouse)) {
                rect.onClick(mouse);
                close();
                break;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollTarget += verticalAmount * 5.0;
        scrollTarget = Math.clamp(scrollTarget, 0, Math.max(0, ((numLines*size.y) - (ySize*size.y))+1));
        return super.mouseScrolled(mouseX,mouseY,horizontalAmount,verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);
        String searchText = searchBox.getText().replace("\n", "");
        if (keyCode == GLFW.GLFW_KEY_ENTER) {
            for (String eventId : eventOrder) {
                for (String data : CTPTracker.ctpResult.getOrDefault(eventId, new ArrayList<>())) {
                    if (searchText.isEmpty() || data.toLowerCase().contains(searchText.toLowerCase())) {
                        String eventName = (eventId.equals("entity")) ? "event" : eventId;
                        CommandQueueHelper.addCommand(new CommandQueue("/ctp " + eventName + " " + data));
                        close();
                        return true;
                    }
                }
            }
        }
        return false;
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

    public static boolean isOpen() { return Mod.getCurrentScreen() instanceof CTPScreen; }

}
