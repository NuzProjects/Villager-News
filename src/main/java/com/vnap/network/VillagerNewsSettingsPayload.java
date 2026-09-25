/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 */
package com.vnap.network;

import com.vnap.VillagerNewsAddonPort;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record VillagerNewsSettingsPayload(int chattiness, int rareVoicelines, boolean spawnSpecialVillagers, boolean canEdit) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<VillagerNewsSettingsPayload> TYPE = new CustomPacketPayload.Type(VillagerNewsAddonPort.id("settings"));
    public static final StreamCodec<RegistryFriendlyByteBuf, VillagerNewsSettingsPayload> CODEC = new StreamCodec<RegistryFriendlyByteBuf, VillagerNewsSettingsPayload>(){

        public VillagerNewsSettingsPayload decode(RegistryFriendlyByteBuf buffer) {
            return new VillagerNewsSettingsPayload(buffer.readVarInt(), buffer.readVarInt(), buffer.readBoolean(), buffer.readBoolean());
        }

        public void encode(RegistryFriendlyByteBuf buffer, VillagerNewsSettingsPayload payload) {
            buffer.writeVarInt(payload.chattiness());
            buffer.writeVarInt(payload.rareVoicelines());
            buffer.writeBoolean(payload.spawnSpecialVillagers());
            buffer.writeBoolean(payload.canEdit());
        }
    };

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

