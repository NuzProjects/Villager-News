/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.ai.goal.EatBlockGoal
 *  net.minecraft.world.item.DyeColor
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.vnap.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Sheep.class})
public abstract class SheepSoundMixin {
    @Shadow
    private int eatAnimationTick;

    @Inject(method={"getAmbientSound"}, at={@At(value="HEAD")}, cancellable=true)
    private void vnap$removeWoolyAmbientSound(CallbackInfoReturnable<SoundEvent> callbackInfoReturnable) {
        if (this.vnap$isWooly()) {
            callbackInfoReturnable.setReturnValue((Object)SoundEvents.EMPTY);
        }
    }

    @Inject(method={"getHurtSound"}, at={@At(value="HEAD")}, cancellable=true)
    private void vnap$removeWoolyHurtSound(DamageSource damageSource, CallbackInfoReturnable<SoundEvent> callbackInfoReturnable) {
        if (this.vnap$isWooly()) {
            callbackInfoReturnable.setReturnValue((Object)SoundEvents.EMPTY);
        }
    }

    @Inject(method={"getDeathSound"}, at={@At(value="HEAD")}, cancellable=true)
    private void vnap$removeWoolyDeathSound(CallbackInfoReturnable<SoundEvent> callbackInfoReturnable) {
        if (this.vnap$isWooly()) {
            callbackInfoReturnable.setReturnValue((Object)SoundEvents.EMPTY);
        }
    }

    @Inject(method={"getColor"}, at={@At(value="HEAD")}, cancellable=true)
    private void vnap$forceWoolyWhite(CallbackInfoReturnable<DyeColor> callbackInfoReturnable) {
        if (this.vnap$isWooly()) {
            callbackInfoReturnable.setReturnValue((Object)DyeColor.WHITE);
        }
    }

    @Inject(method={"ate"}, at={@At(value="HEAD")}, cancellable=true)
    private void vnap$cancelWoolyAte(CallbackInfo callbackInfo) {
        if (this.vnap$isWooly()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"handleEntityEvent"}, at={@At(value="HEAD")}, cancellable=true)
    private void vnap$cancelWoolyEatAnimation(byte by, CallbackInfo callbackInfo) {
        if (by == 10 && this.vnap$isWooly()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"getHeadEatPositionScale"}, at={@At(value="HEAD")}, cancellable=true)
    private void vnap$cancelWoolyHeadEatPos(float f, CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (this.vnap$isWooly()) {
            callbackInfoReturnable.setReturnValue((Object)Float.valueOf(0.0f));
        }
    }

    @Inject(method={"getHeadEatAngleScale"}, at={@At(value="HEAD")}, cancellable=true)
    private void vnap$cancelWoolyHeadEatAngle(float f, CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (this.vnap$isWooly()) {
            callbackInfoReturnable.setReturnValue((Object)Float.valueOf(0.0f));
        }
    }

    @Inject(method={"customServerAiStep"}, at={@At(value="HEAD")})
    private void vnap$stopWoolyEatingGrass(ServerLevel serverLevel, CallbackInfo callbackInfo) {
        if (this.vnap$isWooly()) {
            Sheep sheep = (Sheep)((Object)this);
            if (sheep.getGoalSelector() != null) {
                sheep.getGoalSelector().removeAllGoals(goal -> goal instanceof EatBlockGoal);
            }
            this.eatAnimationTick = 0;
        }
    }

    private boolean vnap$isWooly() {
        String string = ((Sheep)((Object)this)).getName().getString();
        return string.equalsIgnoreCase("Wooly") || string.equalsIgnoreCase("Wooly The Sheep") || string.equalsIgnoreCase("Woolly") || string.equalsIgnoreCase("Woolly The Sheep");
    }
}

