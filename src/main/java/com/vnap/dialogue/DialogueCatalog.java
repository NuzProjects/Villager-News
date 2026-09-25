/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.minecraft.core.Registry
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.resources.Identifier
 *  net.minecraft.sounds.SoundEvent
 */
package com.vnap.dialogue;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vnap.VillagerNewsAddonPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public final class DialogueCatalog {
    private static final String CATALOG_PATH = "/assets/villager-news-addon-port/dialogues.json";
    private static final Map<String, DialogueGroup> GROUPS = new LinkedHashMap<String, DialogueGroup>();
    private static final Map<String, List<DialogueGroup>> TITLES = new LinkedHashMap<String, List<DialogueGroup>>();

    private DialogueCatalog() {
    }

    public static void register() {
        try (InputStream stream = DialogueCatalog.class.getResourceAsStream(CATALOG_PATH);){
            if (stream == null) {
                throw new IOException("Missing /assets/villager-news-addon-port/dialogues.json");
            }
            JsonObject root = JsonParser.parseReader((Reader)new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
            int variantCount = 0;
            for (Map.Entry entry : root.getAsJsonObject("groups").entrySet()) {
                String groupId = (String)entry.getKey();
                JsonObject value = ((JsonElement)entry.getValue()).getAsJsonObject();
                ArrayList<DialogueVariant> variants = new ArrayList<DialogueVariant>();
                for (JsonElement variantElement : value.getAsJsonArray("variants")) {
                    JsonObject variantValue = variantElement.getAsJsonObject();
                    int index = variantValue.get("index").getAsInt();
                    ArrayList<SubtitleFrame> subtitles = new ArrayList<SubtitleFrame>();
                    for (JsonElement subtitleElement : variantValue.getAsJsonArray("subtitles")) {
                        JsonObject subtitleValue = subtitleElement.getAsJsonObject();
                        subtitles.add(new SubtitleFrame(subtitleValue.get("time").getAsDouble(), subtitleValue.get("key").getAsString()));
                    }
                    Identifier soundId = VillagerNewsAddonPort.id("dialogue." + groupId + "." + index);
                    SoundEvent sound = (SoundEvent)Registry.register((Registry)BuiltInRegistries.SOUND_EVENT, (Identifier)soundId, (Object)SoundEvent.createVariableRangeEvent((Identifier)soundId));
                    variants.add(new DialogueVariant(index, variantValue.get("duration").getAsDouble(), variantValue.get("weight").getAsInt(), variantValue.get("animation").getAsString(), sound, List.copyOf(subtitles)));
                    ++variantCount;
                }
                DialogueGroup group = new DialogueGroup(groupId, value.get("title").getAsString(), value.get("body").getAsString(), value.get("speaker").getAsString(), value.get("maximumDuration").getAsDouble(), List.copyOf(variants));
                GROUPS.put(groupId, group);
                if (group.title().isBlank()) continue;
                TITLES.computeIfAbsent(group.title(), ignored -> new ArrayList()).add(group);
            }
            VillagerNewsAddonPort.LOGGER.info("Registered {} contextual dialogue groups with {} synchronized variants", (Object)GROUPS.size(), (Object)variantCount);
        }
        catch (IOException | RuntimeException exception) {
            throw new IllegalStateException("Could not load Villager News dialogue catalog", exception);
        }
    }

    public static DialogueGroup byId(String id) {
        return GROUPS.get(id);
    }

    public static DialogueGroup byTitle(String title) {
        List<DialogueGroup> matches = TITLES.get(title);
        return matches == null || matches.isEmpty() ? null : matches.getFirst();
    }

    public static DialogueGroup byTitle(String title, String speaker) {
        List<DialogueGroup> matches = TITLES.get(title);
        if (matches == null) {
            return null;
        }
        return matches.stream().filter(group -> group.speaker().equals(speaker)).findFirst().orElse(null);
    }

    public static Map<String, DialogueGroup> groups() {
        return Collections.unmodifiableMap(GROUPS);
    }

    public record SubtitleFrame(double time, String key) {
    }

    public record DialogueVariant(int index, double duration, int weight, String animation, SoundEvent sound, List<SubtitleFrame> subtitles) {
        public long durationTicks() {
            return Math.max(20L, (long)Math.ceil(this.duration * 20.0));
        }
    }

    public record DialogueGroup(String id, String title, String body, String speaker, double maximumDuration, List<DialogueVariant> variants) {
        public long durationTicks() {
            return Math.max(20L, (long)Math.ceil(this.maximumDuration * 20.0));
        }

        public DialogueVariant chooseVariant() {
            return this.chooseVariant(1);
        }

        public DialogueVariant chooseVariant(int rareVoicelines) {
            return this.chooseVariant(rareVoicelines, Set.of());
        }

        public DialogueVariant chooseVariant(int rareVoicelines, Set<Integer> excludedVariants) {
            if (this.variants.isEmpty()) {
                return null;
            }
            int minimum = this.variants.stream().mapToInt(DialogueVariant::weight).min().orElse(1);
            int maximum = this.variants.stream().mapToInt(DialogueVariant::weight).max().orElse(1);
            int[] weights = new int[this.variants.size()];
            int totalWeight = 0;
            for (int index = 0; index < this.variants.size(); ++index) {
                int weight = this.variants.get(index).weight();
                if (rareVoicelines == 0 && (double)weight < (double)maximum * 0.8) {
                    weight = 0;
                } else if (rareVoicelines == 2) {
                    weight = maximum + minimum - weight;
                }
                if (excludedVariants.contains(this.variants.get(index).index())) {
                    weight = 0;
                }
                weights[index] = Math.max(0, weight);
                totalWeight += weights[index];
            }
            if (totalWeight <= 0) {
                if (!excludedVariants.isEmpty()) {
                    return this.chooseVariant(rareVoicelines, Set.of());
                }
                return this.variants.getFirst();
            }
            int choice = ThreadLocalRandom.current().nextInt(Math.max(1, totalWeight));
            for (int index = 0; index < this.variants.size(); ++index) {
                if ((choice -= weights[index]) >= 0) continue;
                return this.variants.get(index);
            }
            return this.variants.getLast();
        }
    }
}

