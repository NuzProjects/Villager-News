/*
 * Decompiled with CFR 0.152.
 */
package com.vnap.config;

import com.vnap.VillagerNewsAddonPort;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class VillagerNewsBuildSettings {
    private static final boolean DIALOGUE_TEST_COMMAND = VillagerNewsBuildSettings.loadDialogueTestCommand();

    private VillagerNewsBuildSettings() {
    }

    public static boolean dialogueTestCommand() {
        return DIALOGUE_TEST_COMMAND;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static boolean loadDialogueTestCommand() {
        Properties properties = new Properties();
        try (InputStream stream = VillagerNewsBuildSettings.class.getResourceAsStream("/villager-news-addon-port-build.properties");){
            if (stream == null) {
                boolean bl2 = false;
                return bl2;
            }
            properties.load(stream);
            boolean bl = Boolean.parseBoolean(properties.getProperty("dialogue_test_command", "false"));
            return bl;
        }
        catch (IOException exception) {
            VillagerNewsAddonPort.LOGGER.warn("Could not load Villager News build settings", (Throwable)exception);
            return false;
        }
    }
}

