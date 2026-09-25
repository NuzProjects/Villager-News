/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.resources.Identifier
 *  net.minecraft.util.Mth
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyVariable
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  traben.entity_model_features.models.parts.EMFModelPart
 *  traben.entity_model_features.models.parts.EMFModelPartCustom
 */
package com.vnap.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vnap.client.RainbowNoseRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import traben.entity_model_features.models.parts.EMFModelPart;
import traben.entity_model_features.models.parts.EMFModelPartCustom;

@Mixin(value={EMFModelPart.class}, remap=false)
public abstract class EMFModelPartMixin {
    @Shadow
    public Identifier textureOverride;
    @Unique
    private static final Identifier VNAP$RAINBOW_TEXTURE = Identifier.fromNamespaceAndPath((String)"villager-news-addon-port", (String)"textures/entity/rainbow_nose.png");
    @Unique
    private Identifier vnap$previousTexture;
    @Unique
    private boolean vnap$rainbowTextureActive;

    @Inject(method={"render"}, at={@At(value="HEAD")})
    private void vnap$beginRainbowTexture(PoseStack poseStack, VertexConsumer vertices, int light, int overlay, int color, CallbackInfo ci) {
        if (!this.vnap$isRainbowNose()) {
            return;
        }
        this.vnap$previousTexture = this.textureOverride;
        this.textureOverride = VNAP$RAINBOW_TEXTURE;
        this.vnap$rainbowTextureActive = true;
    }

    @Inject(method={"render"}, at={@At(value="RETURN")})
    private void vnap$endRainbowTexture(PoseStack poseStack, VertexConsumer vertices, int light, int overlay, int color, CallbackInfo ci) {
        if (!this.vnap$rainbowTextureActive) {
            return;
        }
        this.textureOverride = this.vnap$previousTexture;
        this.vnap$previousTexture = null;
        this.vnap$rainbowTextureActive = false;
    }

    @ModifyVariable(method={"compile"}, at={@At(value="HEAD")}, argsOnly=true, ordinal=0)
    private int vnap$rainbowLight(int light) {
        return this.vnap$isRainbowNose() ? 0xF000F0 : light;
    }

    @ModifyVariable(method={"compile"}, at={@At(value="HEAD")}, argsOnly=true, ordinal=2)
    private int vnap$rainbowNose(int color) {
        float green;
        float red;
        if (!this.vnap$isRainbowNose()) {
            return color;
        }
        float phase = Mth.positiveModulo((float)RainbowNoseRenderState.cycleSeconds(), (float)6.0f);
        float x = 1.0f - Math.abs(phase % 2.0f - 1.0f);
        float f = phase < 1.0f ? 1.0f : (phase < 2.0f ? x : (phase < 4.0f ? 0.0f : (red = phase < 5.0f ? x : 1.0f)));
        float f2 = phase < 1.0f ? x : (phase < 3.0f ? 1.0f : (green = phase < 4.0f ? x : 0.0f));
        float blue = phase < 2.0f ? 0.0f : (phase < 3.0f ? x : (phase < 5.0f ? 1.0f : x));
        int alpha = color >>> 24;
        int tintedRed = Math.round((float)(color >>> 16 & 0xFF) * red);
        int tintedGreen = Math.round((float)(color >>> 8 & 0xFF) * green);
        int tintedBlue = Math.round((float)(color & 0xFF) * blue);
        return alpha << 24 | tintedRed << 16 | tintedGreen << 8 | tintedBlue;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Unique
    private boolean vnap$isRainbowNose() {
        EMFModelPartMixin eMFModelPartMixin = this;
        if (!(eMFModelPartMixin instanceof EMFModelPartCustom)) return false;
        EMFModelPartCustom part = (EMFModelPartCustom)eMFModelPartMixin;
        if (!part.id.contains("villager_news_base_fgk6")) return false;
        if (!RainbowNoseRenderState.active()) return false;
        return true;
    }
}

