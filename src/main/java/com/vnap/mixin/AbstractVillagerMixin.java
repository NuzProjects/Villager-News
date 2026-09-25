/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.npc.villager.AbstractVillager
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.trading.MerchantOffer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.vnap.mixin;

import com.vnap.dialogue.ContextualDialogueController;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={AbstractVillager.class})
public abstract class AbstractVillagerMixin {
    @Inject(method={"notifyTrade"}, at={@At(value="TAIL")})
    private void vnap$onTradeCompleted(MerchantOffer offer, CallbackInfo ci) {
        AbstractVillager trader = (AbstractVillager)this;
        Player player = trader.getTradingPlayer();
        if (player != null) {
            ContextualDialogueController.onTradeCompleted((LivingEntity)trader, player);
        }
    }

    @Inject(method={"getNotifyTradeSound"}, at={@At(value="HEAD")}, cancellable=true)
    private void vnap$removeVanillaTradeSound(CallbackInfoReturnable<SoundEvent> cir) {
        cir.setReturnValue((Object)SoundEvents.EMPTY);
    }

    @Inject(method={"getTradeUpdatedSound"}, at={@At(value="HEAD")}, cancellable=true)
    private void vnap$removeVanillaTradeUpdatedSound(boolean sold, CallbackInfoReturnable<SoundEvent> cir) {
        cir.setReturnValue((Object)SoundEvents.EMPTY);
    }

    @Inject(method={"playCelebrateSound"}, at={@At(value="HEAD")}, cancellable=true)
    private void vnap$removeVanillaCelebrateSound(CallbackInfo ci) {
        ci.cancel();
    }
}

