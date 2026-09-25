/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 */
package com.vnap.network;

import com.vnap.network.DialogueAnimationPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public final class DialogueAnimationNetwork {
    private static final double TRACKING_RANGE_SQUARED = 9216.0;

    private DialogueAnimationNetwork() {
    }

    public static void send(ServerLevel level, LivingEntity speaker, String groupId, int variantIndex, int durationTicks) {
        DialogueAnimationPayload payload = new DialogueAnimationPayload(speaker.getUUID(), groupId, variantIndex, durationTicks);
        for (ServerPlayer player : level.players()) {
            if (!(player.distanceToSqr((Entity)speaker) <= 9216.0) || !ServerPlayNetworking.canSend((ServerPlayer)player, DialogueAnimationPayload.TYPE)) continue;
            ServerPlayNetworking.send((ServerPlayer)player, (CustomPacketPayload)payload);
        }
    }

    public static void stop(ServerLevel level, LivingEntity speaker) {
        DialogueAnimationNetwork.send(level, speaker, "", 0, 0);
    }
}

