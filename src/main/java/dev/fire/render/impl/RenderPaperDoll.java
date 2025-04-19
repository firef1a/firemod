package dev.fire.render.impl;

import dev.fire.Mod;
import dev.fire.render.Alignment;
import dev.fire.render.Scaler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RenderPaperDoll extends RenderObject {
    public RenderPaperDoll(Scaler position, Scaler size, double zIndex, Alignment alignment, Alignment parentAlignment, boolean enabled) {
        super(position, size, zIndex, alignment, parentAlignment, enabled);
    }

    @Override
    public void internalRender(DrawContext context) {
        Entity playerEntity = Mod.MC.getCameraEntity() != null ? Mod.MC.getCameraEntity() : Mod.MC.player;
        if (playerEntity == null) return;
        try {
            drawEntity(context, getX1(),getY1(),getX2(), getY2(),30,1f,500f,500f, (LivingEntity) playerEntity);
        } catch (Exception ignored) { }
    }

    public static void drawEntity(DrawContext context, int x1, int y1, int x2, int y2, int size, float f, float mouseX, float mouseY, LivingEntity entity) {
        float g = (float)(x1 + x2) / 2.0F;
        float h = (float)(y1 + y2) / 2.0F;
        //context.enableScissor(x1, y1, x2, y2);
        float i = (float)Math.atan((double)((g-35) / 40.0F));
        float j = (float)Math.atan((double)((h-25) / 40.0F));
        Quaternionf quaternionf = (new Quaternionf()).rotateZ(3.1415927F);
        Quaternionf quaternionf2 = (new Quaternionf()).rotateX(j * 20.0F * 0.017453292F);
        quaternionf.mul(quaternionf2);
        float k = entity.bodyYaw;
        float l = entity.getYaw();
        float m = entity.getPitch();
        float n = entity.prevHeadYaw;
        float o = entity.headYaw;

        float y = 165F;
        entity.bodyYaw = y;
        float setYaw = (o - k) + y;

        entity.setYaw(setYaw);
        entity.setHeadYaw(setYaw);
        //entity.setPitch(-j * 20.0F);
        entity.headYaw = setYaw;
        entity.prevHeadYaw = setYaw;
        float p = entity.getScale();
        Vector3f vector3f = new Vector3f(0.0F, entity.getHeight() / 2.0F + f * p, 0.0F);
        float q = (float)size / p;
        drawEntity(context, g, h, q, vector3f, quaternionf, quaternionf2, entity);
        entity.bodyYaw = k;
        entity.setYaw(l);
        entity.setPitch(m);
        entity.prevHeadYaw = n;
        entity.headYaw = o;
        //context.disableScissor();
    }

    public static void drawEntity(DrawContext context, float x, float y, float size, Vector3f vector3f, Quaternionf quaternionf, @Nullable Quaternionf quaternionf2, LivingEntity entity) {
        context.getMatrices().push();
        context.getMatrices().translate((double)x, (double)y, 50.0);
        context.getMatrices().scale(size, size, -size);
        context.getMatrices().translate(vector3f.x, vector3f.y, vector3f.z);
        context.getMatrices().multiply(quaternionf);
        context.draw();
        DiffuseLighting.method_34742();
        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        if (quaternionf2 != null) {
            entityRenderDispatcher.setRotation(quaternionf2.conjugate(new Quaternionf()).rotateY(3.1415927F));
        }

        entityRenderDispatcher.setRenderShadows(false);
        boolean shouldRenderHitboxes = entityRenderDispatcher.shouldRenderHitboxes();
        entityRenderDispatcher.setRenderHitboxes(false);
        context.draw((vertexConsumers) -> {
            entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 1.0F, context.getMatrices(), vertexConsumers, 15728880);
        });

        context.draw();
        entityRenderDispatcher.setRenderHitboxes(shouldRenderHitboxes);
        entityRenderDispatcher.setRenderShadows(true);
        context.getMatrices().pop();
        DiffuseLighting.enableGuiDepthLighting();
    }
}
