/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.ClientModInitializer
 *  net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
 *  net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
 *  net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
 *  net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback
 *  net.fabricmc.fabric.api.event.player.UseItemCallback
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.model.npc.VillagerModel
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.VillagerRenderer
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.client.renderer.entity.state.VillagerRenderState
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.EntityTypes
 *  traben.entity_model_features.EMFAnimationApi
 */
package com.vnap.client;

import com.vnap.VillagerNewsAddonPort;
import com.vnap.client.DialogueAnimationState;
import com.vnap.client.DialogueSoundState;
import com.vnap.client.DialogueSubtitleState;
import com.vnap.client.HandbookScreen;
import com.vnap.client.VillagerNewsClientSettings;
import com.vnap.client.VillagerNewsSettingsState;
import com.vnap.client.VillagerNewsSignLayer;
import com.vnap.item.VillagerNewsItems;
import com.vnap.network.DialogueAnimationPayload;
import com.vnap.network.VillagerNewsSettingsPayload;
import java.io.IOException;
import java.util.function.Supplier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.npc.VillagerModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityTypes;
import traben.entity_model_features.EMFAnimationApi;

public final class VillagerNewsAddonPortClient
implements ClientModInitializer {
    public void onInitializeClient() {
        VillagerNewsClientSettings.load();
        try {
            DialogueAnimationState.load();
            VillagerNewsAddonPortClient.registerFloat("vnap_speaking", DialogueAnimationState::speaking, "Whether the Villager News character is speaking");
            VillagerNewsAddonPortClient.registerFloat("vnap_mouth_open", DialogueAnimationState::mouthOpen, "Current Villager News mouth opening");
            VillagerNewsAddonPortClient.registerFloat("vnap_mouth_width", DialogueAnimationState::mouthWidth, "Current Villager News mouth width");
            VillagerNewsAddonPortClient.registerFloat("vnap_mouth_closed", DialogueAnimationState::mouthClosed, "Current Villager News closed-mouth layer");
            VillagerNewsAddonPortClient.registerFloat("vnap_has_nose", DialogueAnimationState::hasNose, "Villager News nose visibility");
            VillagerNewsAddonPortClient.registerFloat("vnap_cosmetic_mayor_hat", () -> Float.valueOf(DialogueAnimationState.cosmetic(1)), "Villager News mayor hat visibility");
            VillagerNewsAddonPortClient.registerFloat("vnap_cosmetic_helmet", () -> Float.valueOf(DialogueAnimationState.cosmetic(2)), "Villager News helmet visibility");
            VillagerNewsAddonPortClient.registerFloat("vnap_cosmetic_microphone", () -> Float.valueOf(DialogueAnimationState.cosmetic(3)), "Villager News microphone visibility");
            VillagerNewsAddonPortClient.registerFloat("vnap_cosmetic_moustache", () -> Float.valueOf(DialogueAnimationState.cosmetic(4)), "Villager News moustache visibility");
            for (String variable : DialogueAnimationState.animationVariables()) {
                VillagerNewsAddonPortClient.registerFloat(variable, () -> Float.valueOf(DialogueAnimationState.transform(variable)), "Synchronized Villager News dialogue transform");
            }
        }
        catch (IOException | RuntimeException exception) {
            throw new IllegalStateException("Could not load Villager News animations", exception);
        }
        catch (Exception exception) {
            throw new IllegalStateException("Could not register Villager News EMF animation variables", exception);
        }
        LivingEntityRenderLayerRegistrationCallback.EVENT.register((entityType, entityRenderer, helper, context) -> {
            if (entityType == EntityTypes.VILLAGER && entityRenderer instanceof VillagerRenderer) {
                VillagerRenderer villagerRenderer = (VillagerRenderer)entityRenderer;
                helper.register((RenderLayer)new VillagerNewsSignLayer((RenderLayerParent<VillagerRenderState, VillagerModel>)villagerRenderer));
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(DialogueAnimationPayload.TYPE, (payload, context) -> context.client().execute(() -> {
            DialogueSoundState.start(payload);
            DialogueAnimationState.start(payload);
            DialogueSubtitleState.start(payload);
        }));
        ClientPlayNetworking.registerGlobalReceiver(VillagerNewsSettingsPayload.TYPE, (payload, context) -> context.client().execute(() -> VillagerNewsSettingsState.apply(payload)));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            DialogueSoundState.clear(client);
            DialogueAnimationState.clear();
            DialogueSubtitleState.clear();
            VillagerNewsSettingsState.reset();
        });
        UseItemCallback.EVENT.register((player, level, hand) -> {
            if (!level.isClientSide()) {
                return InteractionResult.PASS;
            }
            if (player.getItemInHand(hand).getItem() != VillagerNewsItems.HANDBOOK) {
                return InteractionResult.PASS;
            }
            Minecraft.getInstance().setScreenAndShow((Screen)new HandbookScreen());
            return InteractionResult.SUCCESS;
        });
        DialogueSubtitleState.register();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            DialogueSoundState.tick(client);
            DialogueAnimationState.tick(client);
            DialogueSubtitleState.tick(client);
        });
        VillagerNewsAddonPort.LOGGER.info("Registered synchronized EMF facial and dialogue animations");
    }

    private static void registerFloat(String name, Supplier<Float> supplier, String description) throws Exception {
        EMFAnimationApi.registerSingletonAnimationVariable((String)"villager-news-addon-port", (String)name, (String)description, supplier);
    }
}

