/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.server.permissions.Permissions
 */
package com.vnap.network;

import com.vnap.config.VillagerNewsSettings;
import com.vnap.network.VillagerNewsSettingsPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;

public final class VillagerNewsSettingsNetwork {
    private VillagerNewsSettingsNetwork() {
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(VillagerNewsSettingsPayload.TYPE, (payload, context) -> {
            if (!VillagerNewsSettingsNetwork.canEdit(context.player())) {
                VillagerNewsSettingsNetwork.send(context.player());
                return;
            }
            VillagerNewsSettings.update(payload.chattiness(), payload.rareVoicelines(), payload.spawnSpecialVillagers());
            VillagerNewsSettingsNetwork.send(context.player());
        });
        ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> VillagerNewsSettingsNetwork.send(listener.getPlayer()));
    }

    public static void send(ServerPlayer player) {
        if (ServerPlayNetworking.canSend((ServerPlayer)player, VillagerNewsSettingsPayload.TYPE)) {
            ServerPlayNetworking.send((ServerPlayer)player, (CustomPacketPayload)new VillagerNewsSettingsPayload(VillagerNewsSettings.chattiness(), VillagerNewsSettings.rareVoicelines(), VillagerNewsSettings.spawnSpecialVillagers(), VillagerNewsSettingsNetwork.canEdit(player)));
        }
    }

    private static boolean canEdit(ServerPlayer player) {
        return player.level().getServer().isSingleplayerOwner(player.nameAndId()) || player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER);
    }
}

