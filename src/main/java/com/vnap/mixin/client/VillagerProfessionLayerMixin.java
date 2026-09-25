/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package com.vnap.mixin.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={VillagerProfessionLayer.class})
abstract class VillagerProfessionLayerMixin {
    VillagerProfessionLayerMixin() {
    }

    @Redirect(method={"submit"}, at={@At(value="FIELD", target="Lnet/minecraft/client/renderer/entity/layers/VillagerProfessionLayer;noHatModel:Lnet/minecraft/client/model/EntityModel;")})
    private EntityModel vnap$alignAdultClothingWithEmfModel(VillagerProfessionLayer layer) {
        return layer.getParentModel();
    }
}

