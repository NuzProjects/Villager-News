/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.npc.villager.Villager
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.vnap.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.npc.villager.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Villager.class})
public abstract class VillagerSoundMixin {
    @Inject(method={"getAmbientSound"}, at={@At(value="HEAD")}, cancellable=true)
    private void vnap$removeVanillaAmbientSound(CallbackInfoReturnable<SoundEvent> cir) {
        cir.setReturnValue((Object)SoundEvents.EMPTY);
    }

    @Inject(method={"getHurtSound"}, at={@At(value="HEAD")}, cancellable=true)
    private void vnap$removeVanillaHurtSound(DamageSource source, CallbackInfoReturnable<SoundEvent> cir) {
        cir.setReturnValue((Object)SoundEvents.EMPTY);
    }

    @Inject(method={"getDeathSound"}, at={@At(value="HEAD")}, cancellable=true)
    private void vnap$removeVanillaDeathSound(CallbackInfoReturnable<SoundEvent> cir) {
        cir.setReturnValue((Object)SoundEvents.EMPTY);
    }
}

