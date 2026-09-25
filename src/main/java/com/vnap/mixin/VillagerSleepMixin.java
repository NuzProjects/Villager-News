/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.npc.villager.Villager
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.vnap.mixin;

import com.vnap.dialogue.ContextualDialogueController;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.villager.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={LivingEntity.class})
public abstract class VillagerSleepMixin {
    @Inject(method={"startSleeping"}, at={@At(value="HEAD")}, cancellable=true)
    private void vnap$delaySleepUntilBedtimeLineFinishes(BlockPos bedPos, CallbackInfo ci) {
        Villager villager;
        VillagerSleepMixin villagerSleepMixin = this;
        if (villagerSleepMixin instanceof Villager && ContextualDialogueController.delayVillagerSleep(villager = (Villager)villagerSleepMixin, bedPos)) {
            ci.cancel();
        }
    }
}

