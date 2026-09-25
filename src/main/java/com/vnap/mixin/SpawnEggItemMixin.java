/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.npc.villager.Villager
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.SpawnEggItem
 *  net.minecraft.world.phys.Vec3
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.vnap.mixin;

import com.vnap.dialogue.ContextualDialogueController;
import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={SpawnEggItem.class})
public abstract class SpawnEggItemMixin {
    @Inject(method={"spawnOffspringFromSpawnEgg"}, at={@At(value="RETURN")})
    private static void vnap$spawnOffspringFromSpawnEgg(Player player, Mob parent, EntityType<? extends Mob> type, ServerLevel level, Vec3 position, ItemStack stack, CallbackInfoReturnable<Optional<Mob>> cir) {
        ((Optional)cir.getReturnValue()).ifPresent(entity -> {
            if (entity instanceof Villager) {
                Villager villager = (Villager)entity;
                ContextualDialogueController.onBabySpawnedFromEgg(villager, player);
            }
        });
    }
}

