/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.state.VillagerRenderState
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 */
package com.vnap.mixin.client;

import com.vnap.client.VillagerNewsRenderState;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value={VillagerRenderState.class})
public abstract class VillagerRenderStateMixin
implements VillagerNewsRenderState {
    @Unique
    private int vnap$signMessage = -1;
    @Unique
    private int vnap$signType = -1;

    @Override
    public int vnap$signMessage() {
        return this.vnap$signMessage;
    }

    @Override
    public void vnap$setSignMessage(int value) {
        this.vnap$signMessage = value;
    }

    @Override
    public int vnap$signType() {
        return this.vnap$signType;
    }

    @Override
    public void vnap$setSignType(int value) {
        this.vnap$signType = value;
    }
}

