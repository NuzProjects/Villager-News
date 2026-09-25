/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.VillagerRenderer
 *  net.minecraft.client.renderer.entity.state.VillagerRenderState
 *  net.minecraft.world.entity.npc.villager.Villager
 *  net.minecraft.world.entity.npc.villager.VillagerData
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.vnap.mixin.client;

import com.vnap.client.DialogueAnimationState;
import com.vnap.client.VillagerNewsRenderState;
import com.vnap.entity.VillagerNewsData;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={VillagerRenderer.class})
public abstract class VillagerRendererMixin {
    private static final Map<Villager, StableVillagerData> VNAP$STABLE_DATA = new WeakHashMap<Villager, StableVillagerData>();

    @Inject(method={"extractRenderState(Lnet/minecraft/world/entity/npc/villager/Villager;Lnet/minecraft/client/renderer/entity/state/VillagerRenderState;F)V"}, at={@At(value="TAIL")})
    private void vnap$extractRenderState(Villager villager, VillagerRenderState state, float partialTick, CallbackInfo ci) {
        StableVillagerData stableData = VNAP$STABLE_DATA.computeIfAbsent(villager, ignored -> new StableVillagerData(state.villagerData));
        state.villagerData = stableData.resolve(state.villagerData, villager.tickCount);
        DialogueAnimationState.trackBodyRotation(villager, state.bodyRot, (float)villager.tickCount + partialTick);
        VillagerNewsData data = (VillagerNewsData)villager;
        VillagerNewsRenderState renderState = (VillagerNewsRenderState)state;
        renderState.vnap$setSignMessage(data.vnap$signMessage());
        renderState.vnap$setSignType(data.vnap$signType());
    }

    private static final class StableVillagerData {
        private VillagerData displayed;
        private VillagerData pending;
        private int pendingSince;

        private StableVillagerData(VillagerData displayed) {
            this.displayed = displayed;
        }

        private VillagerData resolve(VillagerData current, int tick) {
            if (current.equals((Object)this.displayed)) {
                this.pending = null;
                return this.displayed;
            }
            if (!current.equals((Object)this.pending)) {
                this.pending = current;
                this.pendingSince = tick;
                return this.displayed;
            }
            if (tick - this.pendingSince >= 2) {
                this.displayed = current;
                this.pending = null;
            }
            return this.displayed;
        }
    }
}

