/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.item.ItemStack
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.vnap.mixin;

import com.vnap.item.VillagerNewsItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ServerPlayer.class})
public abstract class ServerPlayerMixin {
    @Inject(method={"tick"}, at={@At(value="HEAD")})
    private void vnap$giveHandbookOnFirstJoin(CallbackInfo callbackInfo) {
        ServerPlayer serverPlayer = (ServerPlayer)this;
        if (serverPlayer.entityTags() != null && !serverPlayer.entityTags().contains("vnap$received_handbook")) {
            serverPlayer.addTag("vnap$received_handbook");
            Inventory inventory = serverPlayer.getInventory();
            if (inventory != null && VillagerNewsItems.HANDBOOK != null) {
                ItemStack itemStack = inventory.getItem(0);
                ItemStack itemStack2 = VillagerNewsItems.HANDBOOK.getDefaultInstance();
                if (itemStack == null || itemStack.isEmpty()) {
                    inventory.setItem(0, itemStack2);
                } else if (itemStack.getItem() != VillagerNewsItems.HANDBOOK) {
                    inventory.setItem(0, itemStack2);
                    if (!inventory.add(itemStack)) {
                        serverPlayer.drop(itemStack, false, false);
                    }
                }
            }
        }
    }
}

