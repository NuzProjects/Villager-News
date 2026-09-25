/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.fabricmc.loader.api.FabricLoader
 */
package com.vnap.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vnap.VillagerNewsAddonPort;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import net.fabricmc.loader.api.FabricLoader;

public final class VillagerNewsSettings {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("villager-news-addon-port.json");
    private static int chattiness = 2;
    private static int rareVoicelines = 1;
    private static boolean spawnSpecialVillagers = true;

    private VillagerNewsSettings() {
    }

    public static synchronized void load() {
        if (!Files.exists(PATH, new LinkOption[0])) {
            VillagerNewsSettings.save();
            return;
        }
        try {
            JsonObject root = JsonParser.parseString((String)Files.readString(PATH, StandardCharsets.UTF_8)).getAsJsonObject();
            chattiness = VillagerNewsSettings.clamp(root.has("chattiness") ? root.get("chattiness").getAsInt() : 2, 0, 3);
            rareVoicelines = VillagerNewsSettings.clamp(root.has("rareVoicelines") ? root.get("rareVoicelines").getAsInt() : 1, 0, 2);
            spawnSpecialVillagers = !root.has("spawnSpecialVillagers") || root.get("spawnSpecialVillagers").getAsBoolean();
        }
        catch (IOException | RuntimeException exception) {
            VillagerNewsAddonPort.LOGGER.warn("Could not load Villager News settings; using defaults", (Throwable)exception);
            chattiness = 2;
            rareVoicelines = 1;
            spawnSpecialVillagers = true;
            VillagerNewsSettings.save();
        }
    }

    public static synchronized void update(int newChattiness, int newRareVoicelines, boolean newSpawnSpecialVillagers) {
        chattiness = VillagerNewsSettings.clamp(newChattiness, 0, 3);
        rareVoicelines = VillagerNewsSettings.clamp(newRareVoicelines, 0, 2);
        spawnSpecialVillagers = newSpawnSpecialVillagers;
        VillagerNewsSettings.save();
    }

    public static int chattiness() {
        return chattiness;
    }

    public static int rareVoicelines() {
        return rareVoicelines;
    }

    public static boolean spawnSpecialVillagers() {
        return spawnSpecialVillagers;
    }

    public static boolean dialogueEnabled() {
        return chattiness > 0;
    }

    public static long scaleCooldown(long cooldown) {
        return switch (chattiness) {
            case 1 -> {
                if (cooldown > 0x3FFFFFFFFFFFFFFFL) {
                    yield Long.MAX_VALUE;
                }
                yield cooldown * 2L;
            }
            case 3 -> Math.max(1L, Math.round((double)cooldown / 5.0));
            default -> cooldown;
        };
    }

    private static synchronized void save() {
        JsonObject root = new JsonObject();
        root.addProperty("chattiness", (Number)chattiness);
        root.addProperty("rareVoicelines", (Number)rareVoicelines);
        root.addProperty("spawnSpecialVillagers", Boolean.valueOf(spawnSpecialVillagers));
        try {
            Files.createDirectories(PATH.getParent(), new FileAttribute[0]);
            Files.writeString(PATH, (CharSequence)(GSON.toJson((JsonElement)root) + System.lineSeparator()), StandardCharsets.UTF_8, new OpenOption[0]);
        }
        catch (IOException exception) {
            VillagerNewsAddonPort.LOGGER.warn("Could not save Villager News settings", (Throwable)exception);
        }
    }

    private static int clamp(int value, int minimum, int maximum) {
        return Math.max(minimum, Math.min(maximum, value));
    }
}

