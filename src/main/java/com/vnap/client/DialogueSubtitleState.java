/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
 *  net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.DeltaTracker
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphicsExtractor
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.Identifier
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.npc.villager.Villager
 *  net.minecraft.world.entity.npc.villager.VillagerProfession
 */
package com.vnap.client;

import com.vnap.VillagerNewsAddonPort;
import com.vnap.client.VillagerNewsClientSettings;
import com.vnap.dialogue.DialogueCatalog;
import com.vnap.network.DialogueAnimationPayload;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;

public final class DialogueSubtitleState {
    private static final double RANGE = 16.0;
    private static final double RANGE_SQUARED = 256.0;
    private static final int MAX_LINES = 4;
    private static final Map<UUID, ActiveSubtitle> ACTIVE = new HashMap<UUID, ActiveSubtitle>();

    private DialogueSubtitleState() {
    }

    public static void register() {
        HudElementRegistry.attachElementAfter((Identifier)VanillaHudElements.OVERLAY_MESSAGE, (Identifier)VillagerNewsAddonPort.id("dialogue_subtitles"), DialogueSubtitleState::render);
    }

    public static void start(DialogueAnimationPayload payload) {
        if (payload.groupId().isEmpty() || payload.durationTicks() <= 0) {
            ACTIVE.remove(payload.entityId());
            return;
        }
        DialogueCatalog.DialogueGroup group = DialogueCatalog.byId(payload.groupId());
        if (group == null) {
            return;
        }
        DialogueCatalog.DialogueVariant variant = group.variants().stream().filter(candidate -> candidate.index() == payload.variantIndex()).findFirst().orElse(null);
        if (variant == null || variant.subtitles().isEmpty()) {
            return;
        }
        long startNanos = System.nanoTime();
        ACTIVE.put(payload.entityId(), new ActiveSubtitle(startNanos, startNanos + (long)payload.durationTicks() * 50000000L, variant.subtitles()));
    }

    public static void tick(Minecraft minecraft) {
        if (minecraft.level == null || minecraft.player == null) {
            DialogueSubtitleState.clear();
            return;
        }
        long now = System.nanoTime();
        Iterator<Map.Entry<UUID, ActiveSubtitle>> iterator = ACTIVE.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, ActiveSubtitle> entry = iterator.next();
            Entity entity = minecraft.level.getEntity(entry.getKey());
            if (now < entry.getValue().endNanos() && (entity == null || entity.isAlive())) continue;
            iterator.remove();
        }
    }

    public static void clear() {
        ACTIVE.clear();
    }

    private static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null || !VillagerNewsClientSettings.showSubtitles()) {
            return;
        }
        long now = System.nanoTime();
        ArrayList<VisibleSubtitle> visible = new ArrayList<VisibleSubtitle>();
        for (Map.Entry<UUID, ActiveSubtitle> entry : ACTIVE.entrySet()) {
            int frame;
            double distanceSquared;
            Entity entity;
            ActiveSubtitle active = entry.getValue();
            if (now >= active.endNanos() || (entity = minecraft.level.getEntity(entry.getKey())) == null || !entity.isAlive() || (distanceSquared = minecraft.player.distanceToSqr(entity)) > 256.0 || (frame = active.frame(now)) < 0) continue;
            MutableComponent transcript = Component.translatable((String)active.subtitles().get(frame).key());
            visible.add(new VisibleSubtitle(distanceSquared, DialogueSubtitleState.subtitleLine(entity, (Component)transcript)));
        }
        visible.sort(Comparator.comparingDouble(VisibleSubtitle::distanceSquared));
        float y = (float)graphics.guiHeight() - 59.0f;
        for (int index = 0; index < Math.min(4, visible.size()); ++index) {
            VisibleSubtitle subtitle = (VisibleSubtitle)visible.get(index);
            float scale = DialogueSubtitleState.subtitleScale(index, subtitle.distanceSquared());
            DialogueSubtitleState.drawCentered(graphics, minecraft, subtitle.text(), y, scale);
            Objects.requireNonNull(minecraft.font);
            y -= 9.0f + 3.0f;
        }
    }

    private static Component subtitleLine(Entity entity, Component transcript) {
        Villager villager;
        Component name = entity.getName();
        if (entity instanceof Villager && !(villager = (Villager)entity).hasCustomName()) {
            name = ((VillagerProfession)villager.getVillagerData().profession().value()).name();
        }
        MutableComponent line = Component.empty();
        line.append((Component)name.copy().withStyle(ChatFormatting.YELLOW));
        line.append((Component)Component.literal((String)": ").withStyle(ChatFormatting.YELLOW));
        line.append((Component)transcript.copy().withStyle(ChatFormatting.WHITE));
        return line;
    }

    private static float subtitleScale(int index, double distanceSquared) {
        if (index == 0) {
            return 1.0f;
        }
        double distance = Math.sqrt(distanceSquared);
        return (float)Math.max(0.65, Math.min(0.9, 0.95 - distance / 16.0 * 0.3));
    }

    private static void drawCentered(GuiGraphicsExtractor graphics, Minecraft minecraft, Component text, float y, float scale) {
        int width = minecraft.font.width((FormattedText)text);
        graphics.pose().pushMatrix();
        graphics.pose().translate((float)graphics.guiWidth() / 2.0f, y);
        graphics.pose().scale(scale, scale);
        graphics.text(minecraft.font, text, -width / 2, 0, -1, true);
        graphics.pose().popMatrix();
    }

    private record ActiveSubtitle(long startNanos, long endNanos, List<DialogueCatalog.SubtitleFrame> subtitles) {
        int frame(long now) {
            double elapsed = (double)(now - this.startNanos) / 1.0E9;
            int frame = -1;
            int index = 0;
            while (index < this.subtitles.size() && !(this.subtitles.get(index).time() > elapsed)) {
                frame = index++;
            }
            return frame;
        }
    }

    private record VisibleSubtitle(double distanceSquared, Component text) {
    }
}

