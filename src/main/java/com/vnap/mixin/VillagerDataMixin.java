/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderGetter$Provider
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.npc.villager.Villager
 *  net.minecraft.world.entity.npc.villager.VillagerData
 *  net.minecraft.world.entity.npc.villager.VillagerProfession
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.trading.MerchantOffers
 *  net.minecraft.world.level.storage.ValueInput
 *  net.minecraft.world.level.storage.ValueOutput
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyVariable
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.vnap.mixin;

import com.vnap.dialogue.ContextualDialogueController;
import com.vnap.entity.VillagerNewsData;
import java.util.Collection;
import net.minecraft.core.HolderGetter;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerData;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Villager.class})
public abstract class VillagerDataMixin
implements VillagerNewsData {
    @Unique
    private static final EntityDataAccessor<Boolean> VNAP_HAS_NOSE = SynchedEntityData.defineId(Villager.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Integer> VNAP_COSMETIC = SynchedEntityData.defineId(Villager.class, (EntityDataSerializer)EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> VNAP_SIGN_MESSAGE = SynchedEntityData.defineId(Villager.class, (EntityDataSerializer)EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> VNAP_SIGN_TYPE = SynchedEntityData.defineId(Villager.class, (EntityDataSerializer)EntityDataSerializers.INT);
    @Unique
    private VillagerData vnap$originalVillagerData;
    @Unique
    private MerchantOffers vnap$originalVillagerOffers;

    @Inject(method={"defineSynchedData"}, at={@At(value="TAIL")})
    private void vnap$defineData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(VNAP_HAS_NOSE, (Object)true);
        builder.define(VNAP_COSMETIC, (Object)0);
        builder.define(VNAP_SIGN_MESSAGE, (Object)-1);
        builder.define(VNAP_SIGN_TYPE, (Object)-1);
    }

    @Inject(method={"addAdditionalSaveData"}, at={@At(value="TAIL")})
    private void vnap$saveData(ValueOutput output, CallbackInfo ci) {
        output.putBoolean("VillagerNewsHasNose", this.vnap$hasNose());
        output.putInt("VillagerNewsCosmetic", this.vnap$cosmetic());
        output.putInt("VillagerNewsSignMessage", this.vnap$signMessage());
        output.putInt("VillagerNewsSignType", this.vnap$signType());
        if (this.vnap$originalVillagerData != null && this.vnap$originalVillagerOffers != null) {
            output.store("VillagerNewsOriginalData", VillagerData.CODEC, (Object)this.vnap$originalVillagerData);
            output.store("VillagerNewsOriginalOffers", MerchantOffers.CODEC, (Object)this.vnap$originalVillagerOffers);
        }
    }

    @Inject(method={"readAdditionalSaveData"}, at={@At(value="TAIL")})
    private void vnap$loadData(ValueInput input, CallbackInfo ci) {
        this.vnap$setHasNose(input.getBooleanOr("VillagerNewsHasNose", true));
        this.vnap$setCosmetic(input.getIntOr("VillagerNewsCosmetic", 0));
        int signMessage = input.getIntOr("VillagerNewsSignMessage", -1);
        this.vnap$setSignMessage(signMessage);
        int equippedSign = ContextualDialogueController.signType(((Villager)this).getMainHandItem());
        this.vnap$setSignType(input.getIntOr("VillagerNewsSignType", equippedSign >= 0 ? equippedSign : (signMessage >= 0 ? 0 : -1)));
        if (equippedSign >= 0) {
            ((Villager)this).setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
        this.vnap$originalVillagerData = input.read("VillagerNewsOriginalData", VillagerData.CODEC).orElse(null);
        this.vnap$originalVillagerOffers = input.read("VillagerNewsOriginalOffers", MerchantOffers.CODEC).orElse(null);
        if (this.vnap$originalVillagerData == null || this.vnap$originalVillagerOffers == null) {
            this.vnap$originalVillagerData = null;
            this.vnap$originalVillagerOffers = null;
        }
    }

    @Redirect(method={"customServerAiStep"}, at={@At(value="INVOKE", target="Lnet/minecraft/world/entity/npc/villager/Villager;stopTrading()V")})
    private void vnap$keepSpecialTradeOpen(Villager villager) {
        if (!ContextualDialogueController.isSpecialTrader(villager)) {
            villager.setTradingPlayer(null);
        }
    }

    @ModifyVariable(method={"setVillagerData"}, at={@At(value="HEAD")}, argsOnly=true)
    private VillagerData vnap$preventSpecialProfession(VillagerData value) {
        Villager villager = (Villager)this;
        return ContextualDialogueController.isSpecialTrader(villager) ? value.withProfession((HolderGetter.Provider)villager.level().registryAccess(), VillagerProfession.NONE).withLevel(1) : value;
    }

    @Override
    public boolean vnap$hasOriginalVillagerState() {
        return this.vnap$originalVillagerData != null && this.vnap$originalVillagerOffers != null;
    }

    @Override
    public void vnap$captureOriginalVillagerState() {
        if (this.vnap$hasOriginalVillagerState()) {
            return;
        }
        Villager villager = (Villager)this;
        this.vnap$originalVillagerData = villager.getVillagerData();
        this.vnap$originalVillagerOffers = villager.getOffers().copy();
    }

    @Override
    public void vnap$restoreOriginalVillagerState() {
        if (!this.vnap$hasOriginalVillagerState()) {
            return;
        }
        Villager villager = (Villager)this;
        VillagerData originalData = this.vnap$originalVillagerData;
        MerchantOffers originalOffers = this.vnap$originalVillagerOffers.copy();
        this.vnap$originalVillagerData = null;
        this.vnap$originalVillagerOffers = null;
        villager.setVillagerData(originalData);
        villager.getOffers().clear();
        villager.getOffers().addAll((Collection)originalOffers);
    }

    @Override
    public boolean vnap$hasNose() {
        return (Boolean)((Villager)this).getEntityData().get(VNAP_HAS_NOSE);
    }

    @Override
    public void vnap$setHasNose(boolean value) {
        ((Villager)this).getEntityData().set(VNAP_HAS_NOSE, (Object)value);
    }

    @Override
    public int vnap$cosmetic() {
        return (Integer)((Villager)this).getEntityData().get(VNAP_COSMETIC);
    }

    @Override
    public void vnap$setCosmetic(int value) {
        ((Villager)this).getEntityData().set(VNAP_COSMETIC, (Object)Math.max(0, Math.min(4, value)));
    }

    @Override
    public int vnap$signMessage() {
        return (Integer)((Villager)this).getEntityData().get(VNAP_SIGN_MESSAGE);
    }

    @Override
    public void vnap$setSignMessage(int value) {
        ((Villager)this).getEntityData().set(VNAP_SIGN_MESSAGE, (Object)Math.max(-1, Math.min(86, value)));
    }

    @Override
    public int vnap$signType() {
        return (Integer)((Villager)this).getEntityData().get(VNAP_SIGN_TYPE);
    }

    @Override
    public void vnap$setSignType(int value) {
        ((Villager)this).getEntityData().set(VNAP_SIGN_TYPE, (Object)Math.max(-1, Math.min(11, value)));
    }
}

