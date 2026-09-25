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
import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record DialogueAnimationPayload(UUID entityId, String groupId, int variantIndex, int durationTicks) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<DialogueAnimationPayload> TYPE = new CustomPacketPayload.Type(VillagerNewsAddonPort.id("dialogue_animation"));
    public static final StreamCodec<RegistryFriendlyByteBuf, DialogueAnimationPayload> CODEC = new StreamCodec<RegistryFriendlyByteBuf, DialogueAnimationPayload>(){

        public DialogueAnimationPayload decode(RegistryFriendlyByteBuf buffer) {
            return new DialogueAnimationPayload(buffer.readUUID(), buffer.readUtf(64), buffer.readVarInt(), buffer.readVarInt());
        }

        public void encode(RegistryFriendlyByteBuf buffer, DialogueAnimationPayload payload) {
            buffer.writeUUID(payload.entityId());
            buffer.writeUtf(payload.groupId(), 64);
            buffer.writeVarInt(payload.variantIndex());
            buffer.writeVarInt(payload.durationTicks());
        }
    };

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

