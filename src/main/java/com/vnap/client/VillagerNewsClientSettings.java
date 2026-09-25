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
package com.vnap.client;

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

public final class VillagerNewsClientSettings {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("villager-news-addon-port-client.json");
    private static boolean showSubtitles = true;

    private VillagerNewsClientSettings() {
    }

    public static synchronized void load() {
        if (!Files.exists(PATH, new LinkOption[0])) {
            VillagerNewsClientSettings.save();
            return;
        }
        try {
            JsonObject root = JsonParser.parseString((String)Files.readString(PATH, StandardCharsets.UTF_8)).getAsJsonObject();
            showSubtitles = !root.has("showSubtitles") || root.get("showSubtitles").getAsBoolean();
        }
        catch (IOException | RuntimeException exception) {
            VillagerNewsAddonPort.LOGGER.warn("Could not load Villager News client settings; using defaults", (Throwable)exception);
            showSubtitles = true;
            VillagerNewsClientSettings.save();
        }
    }

    public static boolean showSubtitles() {
        return showSubtitles;
    }

    public static synchronized void setShowSubtitles(boolean enabled) {
        showSubtitles = enabled;
        VillagerNewsClientSettings.save();
    }

    private static void save() {
        JsonObject root = new JsonObject();
        root.addProperty("showSubtitles", Boolean.valueOf(showSubtitles));
        try {
            Files.createDirectories(PATH.getParent(), new FileAttribute[0]);
            Files.writeString(PATH, (CharSequence)(GSON.toJson((JsonElement)root) + System.lineSeparator()), StandardCharsets.UTF_8, new OpenOption[0]);
        }
        catch (IOException exception) {
            VillagerNewsAddonPort.LOGGER.warn("Could not save Villager News client settings", (Throwable)exception);
        }
    }
}

