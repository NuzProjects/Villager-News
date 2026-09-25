package com.vnap.mixin;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.npc.villager.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Villager.class)
public abstract class MayorVillagerMixin {

    @Shadow
    private static EntityDimensions BABY_DIMENSIONS;

    @Inject(method = "getDefaultDimensions", at = @At("HEAD"), cancellable = true)
    private void vnap$mayorDimensions(Pose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        Villager villager = (Villager)(Object)this;
        if (villager.hasCustomName()) {
            String name = villager.getCustomName().getString().toLowerCase();
            if (name.contains("mayor")) {
                cir.setReturnValue(BABY_DIMENSIONS);
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void vnap$checkMayorDimensions(CallbackInfo ci) {
        Villager villager = (Villager)(Object)this;
        if (villager.hasCustomName()) {
            String name = villager.getCustomName().getString().toLowerCase();
            if (name.contains("mayor")) {
                if (villager.getDimensions(villager.getPose()).height() > 1.5F) {
                    villager.refreshDimensions();
                }
                return;
            }
        }
        if (!villager.isBaby() && villager.getDimensions(villager.getPose()).height() < 1.0F) {
            villager.refreshDimensions();
        }
    }
}
