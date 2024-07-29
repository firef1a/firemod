package dev.fire.firemod.screen.utils.screenWidgets;

import dev.fire.firemod.devutils.MathUtils;
import dev.fire.firemod.screen.utils.Point;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class RenderableCallbackIDButton extends RenderableRectangleObject implements ButtonObject {
    private TextRenderer textRenderer;
    public Text text;
    private RenderableRectangleObject listRect;
    private int hightlightColor;
    public int clickID;
    public boolean isCentered;

    public RenderableCallbackIDButton(TextRenderer textRenderer, Text text, int x, int y, int width, int height, int color, int hightlightColor, int clickID, boolean isCentered) {
        super(x, y, width, height, color, clickID);
        this.textRenderer = textRenderer;
        this.text = text;
        this.listRect = new RenderableRectangleObject(0,0,0,0);
        this.clickID = clickID;
        this.hightlightColor = hightlightColor;
        this.isCentered = isCentered;
    }
    public RenderableCallbackIDButton(TextRenderer textRenderer, Text text, int x, int y, int width, int height, int color, int hightlightColor, boolean isCentered) {
        super(x, y, width, height, color);
        this.textRenderer = textRenderer;
        this.text = text;
        this.listRect = new RenderableRectangleObject(0,0,0,0);
        this.hightlightColor = hightlightColor;
        this.isCentered = isCentered;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, int parentx, int parenty, int parentWidth, int parentHeight) {
        int dx = getScreenPosition().x;
        int dy = getScreenPosition().y;
        Point center = new Point(dx+(width/2),dy+(height/2));


        preSiblings.forEach(obj -> obj.render(context, mouseX, mouseY, dx,dy,dx+width,dy+height));

        int drawColor;
        Point mouse = new Point(mouseX,mouseY);
        if (mouseX > dx && mouseX < dx+width && mouseY > dy && mouseY < dy+height) {
            drawColor = this.hightlightColor;
        } else {
            drawColor = this.color;
        }


        context.fill(dx, dy, dx+width, dy+height, drawColor);
        if (isCentered) {
            context.drawText(textRenderer,text,(center.x-(textRenderer.getWidth(text.getString())/2))+1,(center.y-(textRenderer.fontHeight/2))+1,0xffffff, false);
        } else {
            context.drawText(textRenderer,text,dx+5,(center.y-(textRenderer.fontHeight/2))+1,0xffffff, false);
        }


        siblings.forEach(obj -> obj.render(context, mouseX, mouseY, dx,dy,dx+width,dy+height));

        if (this.topBorder.enabled) { context.fill(dx, dy-this.topBorder.size, dx+width, dy, this.topBorder.color); }
        if (this.bottomBorder.enabled) { context.fill(dx, dy+height, dx+width, dy+height+this.bottomBorder.size, this.bottomBorder.color); }
        if (this.rightBorder.enabled) { context.fill(dx+width, dy, dx+width+this.rightBorder.size, dy+height, this.rightBorder.color); }
        if (this.leftBorder.enabled) { context.fill(dx-this.leftBorder.size, dy, dx, dy+height, this.leftBorder.color); }

        this.scrollingX = MathUtils.lerp(this.scrollingX, this.lerpcrollingX, this.lerpScrollAmount);
        this.scrollingY = MathUtils.lerp(this.scrollingY, this.lerpcrollingY, this.lerpScrollAmount);
    }



    @Override
    public void onClickCallback(double mouseX, double mouseY, int button) { }
}