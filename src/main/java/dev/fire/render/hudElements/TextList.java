package dev.fire.render.hudElements;

import dev.fire.Mod;
import dev.fire.render.Alignment;
import dev.fire.render.Scaler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class TextList extends RenderObject {
    protected ArrayList<Text> textList;

    public TextList(Scaler position, double zIndex, Alignment alignment, Alignment parentAlignment, boolean enabled) {
        super(position, new Scaler(0,0), zIndex, alignment, parentAlignment, enabled);
        textList = new ArrayList<>();
    }

    public void setTextList(ArrayList<Text> textList1) {
        textList = textList1;
    }

    public int getLineHeight() {
        return Mod.MC.textRenderer.fontHeight;
    }

    @Override
    public void internalRender(DrawContext context) {
        int x = getX1();
        int y = getY1();
        int i = 0;
        int h = getLineHeight();

        for (Text text : textList) {
            int color = 0xFFFFF;
            if (text.getStyle().getColor() != null) color = text.getStyle().getColor().getRgb();

            int dy = y + (h * i);
            context.drawText(Mod.MC.textRenderer, text, x, dy, color, false);
            i++;
        }
    }

    @Override
    public Scaler getScalerSize() {
        int maxWidth = 0;
        int maxHeight = (getLineHeight() * (textList.size() - 1)) + Mod.MC.textRenderer.fontHeight;
        for (Text text : textList) {
            int width = Mod.MC.textRenderer.getWidth(text);
            if (width > maxWidth) maxWidth = width;
        }
        //return Scaler.fromPosition(200, 200);
        return Scaler.fromPosition(maxWidth, maxHeight);
    }

}
