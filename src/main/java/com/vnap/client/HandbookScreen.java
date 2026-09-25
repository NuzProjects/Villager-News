/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphicsExtractor
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.EditBox
 *  net.minecraft.client.gui.components.MultiLineTextWidget
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.input.MouseButtonEvent
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 */
package com.vnap.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vnap.client.VillagerNewsClientSettings;
import com.vnap.client.VillagerNewsSettingsState;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public final class HandbookScreen
extends Screen {
    private static final HandbookData DATA = HandbookScreen.load();
    private final Screen parent;
    private final boolean settingsOnly;
    private Page page;
    private Page returnPage = Page.TRIGGERS;
    private int pageIndex;
    private int categoryIndex;
    private int sectionIndex;
    private String search = "";
    private Entry detail;
    private double scrollOffset;
    private int maxScroll;

    public HandbookScreen() {
        this(null, Page.HOME, false);
    }

    private HandbookScreen(Screen screen, Page page, boolean bl) {
        super((Component)Component.literal((String)"Villager News"));
        this.parent = screen;
        this.page = page;
        this.settingsOnly = bl;
    }

    public static HandbookScreen settingsScreen(Screen screen) {
        VillagerNewsSettingsState.prepareConfigScreen();
        return new HandbookScreen(screen, Page.SETTINGS, true);
    }

    protected void init() {
        int n = 228;
        int n2 = 218;
        int n3 = (this.width - n) / 2;
        int n4 = Math.max(8, (this.height - n2) / 2);
        int n5 = n - 28;
        int n6 = n3 + 14;
        if (this.page == Page.HOME) {
            this.buildHome(n6, n5, n4);
            return;
        }
        switch (this.page.ordinal()) {
            case 1: {
                this.buildGuide(n6, n5, n4, n2);
                break;
            }
            case 2: {
                this.buildScrollablePage(n6, n5, n4, n2, Page.GUIDE);
                break;
            }
            case 3: {
                this.buildScrollablePage(n6, n5, n4, n2, Page.GUIDE);
                break;
            }
            case 4: {
                this.buildScrollablePage(n6, n5, n4, n2, Page.GUIDE);
                break;
            }
            case 5: {
                this.buildScrollablePage(n6, n5, n4, n2, Page.TRIGGERS);
                break;
            }
            case 6: {
                this.buildSettings(n6, n5, n4, n2);
                break;
            }
            case 7: {
                this.buildScrollablePage(n6, n5, n4, n2, Page.HOME);
                break;
            }
            case 8: {
                this.buildScrollablePage(n6, n5, n4, n2, Page.HOME);
                break;
            }
            case 9: {
                this.buildTriggers(n6, n5, n4, n2);
                break;
            }
            case 10: {
                this.buildCategory(n6, n5, n4, n2);
                break;
            }
            case 11: {
                this.buildSection(n6, n5, n4, n2);
                break;
            }
            case 12: {
                this.buildScrollablePage(n6, n5, n4, n2, this.returnPage);
                break;
            }
        }
    }

    private void buildHome(int n, int n2, int n3) {
        int n4 = n2;
        int n5 = 20;
        int n6 = n3 + 98;
        int n7 = 24;
        this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal((String)"Guide"), button -> this.navigate(Page.GUIDE)).bounds(n, n6, n4, n5).build());
        this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal((String)"Settings"), button -> this.navigate(Page.SETTINGS)).bounds(n, n6 + n7, n4, n5).build());
        this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal((String)"Socials"), button -> this.navigate(Page.SOCIALS)).bounds(n, n6 + n7 * 2, n4, n5).build());
        this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal((String)"Support"), button -> this.navigate(Page.SUPPORT)).bounds(n, n6 + n7 * 3, n4, n5).build());
    }

    private void buildScrollablePage(int n, int n2, int n3, int n4, Page page) {
        this.scrollOffset = 0.0;
        int n5 = n3 + n4 - 24;
        this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal((String)"Back"), button -> this.navigate(page)).bounds(n, n5, n2, 18).build());
    }

    public boolean mouseScrolled(double d, double d2, double d3, double d4) {
        if (this.isScrollablePage() && this.maxScroll > 0) {
            this.scrollOffset = Math.max(0.0, Math.min((double)this.maxScroll, this.scrollOffset - d4 * 16.0));
            return true;
        }
        return super.mouseScrolled(d, d2, d3, d4);
    }

    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
        int n = 228;
        int n2 = 218;
        int n3 = (this.width - n) / 2;
        int n4 = Math.max(8, (this.height - n2) / 2);
        if (mouseButtonEvent.x() >= (double)(n3 + n - 24) && mouseButtonEvent.x() <= (double)(n3 + n - 4) && mouseButtonEvent.y() >= (double)(n4 + 4) && mouseButtonEvent.y() <= (double)(n4 + 22)) {
            this.onClose();
            return true;
        }
        return super.mouseClicked(mouseButtonEvent, bl);
    }

    public void onClose() {
        Minecraft.getInstance().getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
        super.onClose();
    }

    private boolean isScrollablePage() {
        return switch (this.page.ordinal()) {
            case 2, 3, 4, 5, 7, 8, 12 -> true;
            default -> false;
        };
    }

    public void extractBackground(GuiGraphicsExtractor guiGraphicsExtractor, int n, int n2, float f) {
        this.extractTransparentBackground(guiGraphicsExtractor);
    }

    public void extractRenderState(GuiGraphicsExtractor guiGraphicsExtractor, int n, int n2, float f) {
        int n3 = 228;
        int n4 = 218;
        int n5 = (this.width - n3) / 2;
        int n6 = Math.max(8, (this.height - n4) / 2);
        int n7 = n3 - 28;
        int n8 = n5 + 14;
        guiGraphicsExtractor.fill(n5 + 1, n6 + 1, n5 + n3 - 1, n6 + n4 - 1, -11908534);
        guiGraphicsExtractor.fill(n5 + 1, n6, n5 + n3 - 1, n6 + 1, -14803426);
        guiGraphicsExtractor.fill(n5 + 1, n6 + n4 - 1, n5 + n3 - 1, n6 + n4, -14803426);
        guiGraphicsExtractor.fill(n5, n6 + 1, n5 + 1, n6 + n4 - 1, -14803426);
        guiGraphicsExtractor.fill(n5 + n3 - 1, n6 + 1, n5 + n3, n6 + n4 - 1, -14803426);
        if (this.page == Page.HOME) {
            guiGraphicsExtractor.text(this.font, (Component)Component.literal((String)"\u00a7lVILLAGER NEWS"), n5 + 14, n6 + 13, -1, true);
            guiGraphicsExtractor.text(this.font, (Component)Component.literal((String)"\u00a7e\u00a7lBreaking News!"), n5 + 14, n6 + 34, -8909, true);
            guiGraphicsExtractor.text(this.font, (Component)Component.literal((String)"Villagers are now judging your"), n5 + 14, n6 + 52, -2039584, true);
            guiGraphicsExtractor.text(this.font, (Component)Component.literal((String)"every move with over 2,000+"), n5 + 14, n6 + 64, -2039584, true);
            guiGraphicsExtractor.text(this.font, (Component)Component.literal((String)"voiced reactions."), n5 + 14, n6 + 76, -2039584, true);
        } else {
            guiGraphicsExtractor.text(this.font, (Component)Component.literal((String)("\u00a7l" + this.titleForPage().toUpperCase(Locale.ROOT))), n5 + 14, n6 + 13, -1, true);
        }
        boolean bl = n >= n5 + n3 - 24 && n <= n5 + n3 - 4 && n2 >= n6 + 4 && n2 <= n6 + 22;
        guiGraphicsExtractor.text(this.font, (Component)Component.literal((String)"x"), n5 + n3 - 15, n6 + 12, bl ? -1 : -5197648, true);
        if (this.isScrollablePage()) {
            this.renderScrollableContent(guiGraphicsExtractor, n8, n7, n6, n4);
        }
        super.extractRenderState(guiGraphicsExtractor, n, n2, f);
    }

    private void renderScrollableContent(GuiGraphicsExtractor guiGraphicsExtractor, int n, int n2, int n3, int n4) {
        List<Object> list;
        int n5 = n3 + 28;
        int n6 = n3 + n4 - 30;
        int n7 = n6 - n5;
        int n8 = n2 - 8;
        guiGraphicsExtractor.enableScissor(n, n5, n + n2, n6);
        int n9 = (int)((double)n5 - this.scrollOffset);
        switch (this.page.ordinal()) {
            case 2: {
                List<Object> list2 = HandbookScreen.DATA.overview;
                break;
            }
            case 3: {
                List<Object> list2 = HandbookScreen.DATA.specialVillagers;
                break;
            }
            case 4: {
                List<Object> list2 = HandbookScreen.DATA.cosmetics;
                break;
            }
            case 5: {
                List<Object> list2 = HandbookScreen.DATA.generalInformation;
                break;
            }
            case 7: {
                List<Object> list2 = HandbookScreen.DATA.socials;
                break;
            }
            case 12: {
                List<Object> list2;
                if (this.detail != null) {
                    list2 = List.of(this.detail);
                    break;
                }
                list2 = List.of();
                break;
            }
            default: {
                List<Object> list2 = list = List.of();
            }
        }
        if (this.page == Page.SUPPORT) {
            List<String> list3 = this.wrapText(HandbookScreen.DATA.support, n8);
            Object object = list3.iterator();
            while (object.hasNext()) {
                var14_16 = (String)object.next();
                if (n9 + 9 >= n5 && n9 <= n6) {
                    guiGraphicsExtractor.text(this.font, (Component)Component.literal((String)var14_16), n, n9, -2039584, true);
                }
                n9 += var14_16.isEmpty() ? 6 : 10;
            }
        } else {
            for (Object object : list) {
                var14_16 = HandbookScreen.clean(((Entry)object).title());
                if (!var14_16.isEmpty()) {
                    if (n9 + 9 >= n5 && n9 <= n6) {
                        guiGraphicsExtractor.text(this.font, (Component)Component.literal((String)("\u00a7e\u00a7l" + var14_16)), n, n9, -8909, true);
                    }
                    n9 += 13;
                }
                List<String> list4 = this.wrapText(HandbookScreen.clean(((Entry)object).body()), n8);
                for (String string : list4) {
                    if (n9 + 9 >= n5 && n9 <= n6) {
                        guiGraphicsExtractor.text(this.font, (Component)Component.literal((String)string), n, n9, -2039584, true);
                    }
                    n9 += 10;
                }
                n9 += 24;
            }
        }
        int n10 = n9 + (int)this.scrollOffset - n5;
        this.maxScroll = Math.max(0, n10 - n7);
        guiGraphicsExtractor.disableScissor();
        if (this.maxScroll > 0) {
            int n11 = n7;
            int n12 = Math.max(16, (int)((float)n7 * (float)n7 / (float)n10));
            int n13 = n5 + (int)((float)(n11 - n12) * (float)(this.scrollOffset / (double)this.maxScroll));
            guiGraphicsExtractor.fill(n + n2 - 3, n5, n + n2, n6, 0x33000000);
            guiGraphicsExtractor.fill(n + n2 - 3, n13, n + n2, n13 + n12, -7829368);
        }
    }

    private List<String> wrapText(String string, int n) {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String string2 : string.split("\n")) {
            if (string2.isEmpty()) {
                arrayList.add("");
                continue;
            }
            String[] stringArray = string2.split(" ");
            StringBuilder stringBuilder = new StringBuilder();
            for (String string3 : stringArray) {
                String string4;
                String string5 = string4 = stringBuilder.isEmpty() ? string3 : String.valueOf(stringBuilder) + " " + string3;
                if (this.font.width(string4) <= n) {
                    stringBuilder = new StringBuilder(string4);
                    continue;
                }
                if (!stringBuilder.isEmpty()) {
                    arrayList.add(stringBuilder.toString());
                }
                stringBuilder = new StringBuilder(string3);
            }
            if (stringBuilder.isEmpty()) continue;
            arrayList.add(stringBuilder.toString());
        }
        return arrayList;
    }

    private void buildGuide(int n, int n2, int n3, int n4) {
        String string = HandbookScreen.DATA.guideIntro;
        if (string.contains("\n")) {
            string = string.substring(0, string.indexOf("\n")).trim();
        }
        this.addText(n, n3 + 30, n2, (Component)Component.literal((String)string), true);
        int n5 = n3 + 68;
        this.addMenuButton(n, n5, n2, "Overview", Page.OVERVIEW);
        this.addMenuButton(n, n5 + 24, n2, "Special Villagers", Page.SPECIALS);
        this.addMenuButton(n, n5 + 48, n2, "Cosmetics", Page.COSMETICS);
        this.addMenuButton(n, n5 + 72, n2, "Triggers & Reactions", Page.TRIGGERS);
        this.addBackButton(n, n3 + n4 - 24, n2, Page.HOME);
    }

    private void buildTriggers(int n, int n2, int n3, int n4) {
        int n5;
        EditBox editBox = new EditBox(this.font, n, n3 + 28, n2 - 52, 18, (Component)Component.literal((String)"Search Triggers"));
        editBox.setValue(this.search);
        editBox.setMaxLength(80);
        editBox.setHint((Component)Component.literal((String)"Search..."));
        this.addRenderableWidget((GuiEventListener)editBox);
        this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal((String)"Go"), button -> {
            this.search = editBox.getValue().trim();
            this.pageIndex = 0;
            this.rebuildWidgets();
        }).bounds(n + n2 - 48, n3 + 28, 48, 18).build());
        if (!this.search.isBlank()) {
            this.buildSearchResults(n, n2, n3, n4);
            return;
        }
        this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal((String)"General Information"), button -> this.navigate(Page.GENERAL)).bounds(n, n3 + 50, n2, 20).build());
        List<Category> list = HandbookScreen.DATA.categories;
        int n6 = 4;
        for (int i = n5 = this.pageIndex * n6; i < Math.min(list.size(), n5 + n6); ++i) {
            int n7 = i;
            this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal((String)list.get(i).title()), button -> {
                this.categoryIndex = n7;
                this.pageIndex = 0;
                this.page = Page.CATEGORY;
                this.rebuildWidgets();
            }).bounds(n, n3 + 74 + (i - n5) * 24, n2, 20).build());
        }
        this.addPager(n, n3 + n4 - 24, n2, list.size(), n6, Page.GUIDE);
    }

    private void buildSearchResults(int n, int n2, int n3, int n4) {
        int n5;
        String string = this.search.toLowerCase(Locale.ROOT);
        List<Entry> list = HandbookScreen.DATA.searchable.stream().filter(entry -> HandbookScreen.clean(entry.title()).toLowerCase(Locale.ROOT).contains(string) || HandbookScreen.clean(entry.body()).toLowerCase(Locale.ROOT).contains(string)).sorted(Comparator.comparing(Entry::title, String.CASE_INSENSITIVE_ORDER)).toList();
        this.addText(n, n3 + 50, n2, (Component)Component.literal((String)(list.size() + " matching triggers")), true);
        int n6 = 4;
        for (int i = n5 = this.pageIndex * n6; i < Math.min(list.size(), n5 + n6); ++i) {
            Entry entry2 = list.get(i);
            this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal((String)HandbookScreen.clean(entry2.title())), button -> this.openDetail(entry2, Page.TRIGGERS)).bounds(n, n3 + 68 + (i - n5) * 24, n2, 20).build());
        }
        if (list.isEmpty()) {
            this.addText(n, n3 + 80, n2, (Component)Component.literal((String)"\u00a7cNo triggers match your search."), true);
        }
        this.addPager(n, n3 + n4 - 24, n2, list.size(), n6, Page.GUIDE);
    }

    private void buildCategory(int n, int n2, int n3, int n4) {
        int n5;
        Category category = HandbookScreen.DATA.categories.get(this.categoryIndex);
        int n6 = 5;
        for (int i = n5 = this.pageIndex * n6; i < Math.min(category.sections().size(), n5 + n6); ++i) {
            int n7 = i;
            this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal((String)category.sections().get(i).title()), button -> {
                this.sectionIndex = n7;
                this.pageIndex = 0;
                this.page = Page.SECTION;
                this.rebuildWidgets();
            }).bounds(n, n3 + 32 + (i - n5) * 24, n2, 20).build());
        }
        this.addPager(n, n3 + n4 - 24, n2, category.sections().size(), n6, Page.TRIGGERS);
    }

    private void buildSection(int n, int n2, int n3, int n4) {
        int n5;
        Section section = HandbookScreen.DATA.categories.get(this.categoryIndex).sections().get(this.sectionIndex);
        ArrayList<Entry> arrayList = new ArrayList<Entry>();
        for (String string : section.groups()) {
            Entry entry = HandbookScreen.DATA.contexts.get(string);
            if (entry == null || entry.title().isBlank()) continue;
            arrayList.add(entry);
        }
        arrayList.addAll(section.entries());
        int n6 = 5;
        for (int i = n5 = this.pageIndex * n6; i < Math.min(arrayList.size(), n5 + n6); ++i) {
            Entry entry = (Entry)arrayList.get(i);
            this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal((String)HandbookScreen.clean(entry.title())), button -> this.openDetail(entry, Page.SECTION)).bounds(n, n3 + 32 + (i - n5) * 24, n2, 20).build());
        }
        if (arrayList.isEmpty()) {
            this.addText(n, n3 + 60, n2, (Component)Component.literal((String)"This section is covered by the general guide entries."), true);
        }
        this.addPager(n, n3 + n4 - 24, n2, arrayList.size(), n6, Page.CATEGORY);
    }

    private void buildSettings(int n, int n2, int n3, int n4) {
        boolean bl = VillagerNewsSettingsState.canEdit();
        int n5 = 118;
        int n6 = n + n5;
        int n7 = n2 - n5;
        int n8 = n3 + 32;
        this.addText(n, n8 + 4, n5 - 4, (Component)Component.literal((String)"Subtitles"), false);
        this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal((String)HandbookScreen.toggleLabel(VillagerNewsClientSettings.showSubtitles())), button -> {
            boolean bl = !VillagerNewsClientSettings.showSubtitles();
            VillagerNewsClientSettings.setShowSubtitles(bl);
            button.setMessage((Component)Component.literal((String)HandbookScreen.toggleLabel(bl)));
        }).bounds(n6, n8, n7, 18).build());
        this.addText(n, (n8 += 24) + 4, n5 - 4, (Component)Component.literal((String)"Chattiness"), false);
        Button button2 = Button.builder((Component)Component.literal((String)HandbookScreen.chattinessLabel(VillagerNewsSettingsState.chattiness())), button -> {
            VillagerNewsSettingsState.setChattiness(VillagerNewsSettingsState.chattiness() + 1);
            button.setMessage((Component)Component.literal((String)HandbookScreen.chattinessLabel(VillagerNewsSettingsState.chattiness())));
        }).bounds(n6, n8, n7, 18).build();
        button2.active = bl;
        this.addRenderableWidget((GuiEventListener)button2);
        this.addText(n, (n8 += 24) + 4, n5 - 4, (Component)Component.literal((String)"Rare Voicelines"), false);
        Button button3 = Button.builder((Component)Component.literal((String)HandbookScreen.rareLabel(VillagerNewsSettingsState.rareVoicelines())), button -> {
            VillagerNewsSettingsState.setRareVoicelines(VillagerNewsSettingsState.rareVoicelines() + 1);
            button.setMessage((Component)Component.literal((String)HandbookScreen.rareLabel(VillagerNewsSettingsState.rareVoicelines())));
        }).bounds(n6, n8, n7, 18).build();
        button3.active = bl;
        this.addRenderableWidget((GuiEventListener)button3);
        this.addText(n, (n8 += 24) + 4, n5 - 4, (Component)Component.literal((String)"Special Villagers"), false);
        Button button4 = Button.builder((Component)Component.literal((String)HandbookScreen.toggleLabel(VillagerNewsSettingsState.spawnSpecialVillagers())), button -> {
            VillagerNewsSettingsState.setSpawnSpecialVillagers(!VillagerNewsSettingsState.spawnSpecialVillagers());
            button.setMessage((Component)Component.literal((String)HandbookScreen.toggleLabel(VillagerNewsSettingsState.spawnSpecialVillagers())));
        }).bounds(n6, n8, n7, 18).build();
        button4.active = bl;
        this.addRenderableWidget((GuiEventListener)button4);
        this.addText(n, (n8 += 24) + 4, n5 - 4, (Component)Component.literal((String)"Villager Style"), false);
        Button button5 = Button.builder((Component)Component.literal((String)"Villager News"), button -> {}).bounds(n6, n8, n7, 18).build();
        button5.active = false;
        this.addRenderableWidget((GuiEventListener)button5);
        if (this.settingsOnly) {
            this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal((String)"Done"), button -> this.onClose()).bounds(n, n3 + n4 - 24, n2, 18).build());
        } else {
            this.addBackButton(n, n3 + n4 - 24, n2, Page.HOME);
        }
    }

    private static String toggleLabel(boolean bl) {
        return bl ? "On" : "Off";
    }

    private static String chattinessLabel(int n) {
        return switch (n) {
            case 0 -> "Muted";
            case 1 -> "Shy";
            case 3 -> "Super Chatty";
            default -> "Chatty";
        };
    }

    private static String rareLabel(int n) {
        return switch (n) {
            case 0 -> "Never";
            case 2 -> "Often";
            default -> "Default";
        };
    }

    private void addPager(int n, int n2, int n3, int n4, int n5, Page page) {
        int n6 = Math.max(1, (n4 + n5 - 1) / n5);
        int n7 = (n3 - 12) / 3;
        Button button2 = Button.builder((Component)Component.literal((String)"Prev"), button -> {
            --this.pageIndex;
            this.rebuildWidgets();
        }).bounds(n, n2, n7, 18).build();
        button2.active = this.pageIndex > 0;
        this.addRenderableWidget((GuiEventListener)button2);
        this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal((String)"Back"), button -> this.navigate(page)).bounds(n + n7 + 6, n2, n7, 18).build());
        Button button3 = Button.builder((Component)Component.literal((String)"Next"), button -> {
            ++this.pageIndex;
            this.rebuildWidgets();
        }).bounds(n + (n7 + 6) * 2, n2, n7, 18).build();
        button3.active = this.pageIndex + 1 < n6;
        this.addRenderableWidget((GuiEventListener)button3);
    }

    private void addMenuButton(int n, int n2, int n3, String string, Page page) {
        this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal((String)string), button -> this.navigate(page)).bounds(n, n2, n3, 20).build());
    }

    private void addBackButton(int n, int n2, int n3, Page page) {
        this.addRenderableWidget((GuiEventListener)Button.builder((Component)Component.literal((String)"Back"), button -> this.navigate(page)).bounds(n, n2, n3, 18).build());
    }

    private void navigate(Page page) {
        this.page = page;
        this.pageIndex = 0;
        this.scrollOffset = 0.0;
        if (page != Page.TRIGGERS) {
            this.search = "";
        }
        this.rebuildWidgets();
    }

    private void openDetail(Entry entry, Page page) {
        this.detail = entry;
        this.returnPage = page;
        this.scrollOffset = 0.0;
        this.page = Page.DETAIL;
        this.rebuildWidgets();
    }

    private MultiLineTextWidget addText(int n, int n2, int n3, Component component, boolean bl) {
        MultiLineTextWidget multiLineTextWidget = new MultiLineTextWidget(n, n2, component, this.font).setMaxWidth(n3).setCentered(bl);
        this.addRenderableWidget((GuiEventListener)multiLineTextWidget);
        return multiLineTextWidget;
    }

    private String titleForPage() {
        return switch (this.page.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> "Villager News";
            case 1 -> "Guide";
            case 2 -> "Overview";
            case 3 -> "Special Villagers";
            case 4 -> "Cosmetics";
            case 5 -> "General Information";
            case 6 -> "Settings";
            case 7 -> "Socials";
            case 8 -> "Support";
            case 9 -> "Triggers & Reactions";
            case 10 -> HandbookScreen.DATA.categories.get(this.categoryIndex).title();
            case 11 -> HandbookScreen.DATA.categories.get(this.categoryIndex).sections().get(this.sectionIndex).title();
            case 12 -> this.detail == null ? "Trigger" : HandbookScreen.clean(this.detail.title());
        };
    }

    private static String clean(String string) {
        return string == null ? "" : string.replace("\u00a7l", "").replace("\u00a7o", "").replace("\u00a7r", "").trim();
    }

    private static HandbookData load() {
        HandbookData handbookData;
        try (InputStream inputStream = HandbookScreen.class.getResourceAsStream("/assets/villager-news-addon-port/handbook.json");){
            if (inputStream == null) {
                throw new IOException("Missing handbook data");
            }
            JsonObject jsonObject = JsonParser.parseReader((Reader)new InputStreamReader(inputStream, StandardCharsets.UTF_8)).getAsJsonObject();
            ArrayList<Category> arrayList = new ArrayList<Category>();
            LinkedHashMap<String, Entry> linkedHashMap = new LinkedHashMap<String, Entry>();
            ArrayList<Entry> arrayList2 = new ArrayList<Entry>();
            for (JsonElement jsonElement : jsonObject.getAsJsonArray("categories")) {
                JsonObject jsonObject2 = jsonElement.getAsJsonObject();
                ArrayList<Section> arrayList3 = new ArrayList<Section>();
                for (JsonElement jsonElement2 : jsonObject2.getAsJsonArray("sections")) {
                    JsonElement jsonElement32;
                    JsonObject jsonObject3 = jsonElement2.getAsJsonObject();
                    ArrayList<String> arrayList4 = new ArrayList<String>();
                    if (jsonObject3.has("groups")) {
                        for (JsonElement jsonElement32 : jsonObject3.getAsJsonArray("groups")) {
                            arrayList4.add(jsonElement32.getAsString());
                        }
                    }
                    List<Entry> list = HandbookScreen.entries(jsonObject3.getAsJsonArray("entries"));
                    jsonElement32 = list.iterator();
                    while (jsonElement32.hasNext()) {
                        Entry entry = (Entry)jsonElement32.next();
                        linkedHashMap.put(entry.title(), entry);
                        arrayList2.add(entry);
                    }
                    arrayList2.addAll(list);
                    arrayList3.add(new Section(jsonObject3.get("title").getAsString(), List.copyOf(arrayList4), list));
                }
                arrayList.add(new Category(jsonObject2.get("title").getAsString(), List.copyOf(arrayList3)));
            }
            handbookData = new HandbookData(jsonObject.get("headline").getAsString(), jsonObject.get("guideIntro").getAsString(), HandbookScreen.entries(jsonObject.getAsJsonArray("overview")), HandbookScreen.entries(jsonObject.getAsJsonArray("specialVillagers")), HandbookScreen.entries(jsonObject.getAsJsonArray("cosmetics")), HandbookScreen.entries(jsonObject.getAsJsonArray("generalInformation")), HandbookScreen.entries(jsonObject.getAsJsonArray("socials")), HandbookScreen.entries(jsonObject.getAsJsonArray("settings")), jsonObject.get("support").getAsString(), List.copyOf(arrayList), Map.copyOf(linkedHashMap), List.copyOf(arrayList2));
        }
        catch (IOException | RuntimeException exception) {
            throw new IllegalStateException("Could not load the Villager News handbook", exception);
        }
        return handbookData;
    }

    private static List<Entry> entries(JsonArray jsonArray) {
        ArrayList<Entry> arrayList = new ArrayList<Entry>();
        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            arrayList.add(new Entry(jsonObject.get("title").getAsString(), jsonObject.get("body").getAsString()));
        }
        return List.copyOf(arrayList);
    }

    public static enum Page {
        HOME,
        GUIDE,
        OVERVIEW,
        SPECIALS,
        COSMETICS,
        GENERAL,
        SETTINGS,
        SOCIALS,
        SUPPORT,
        TRIGGERS,
        CATEGORY,
        SECTION,
        DETAIL;

    }

    public record HandbookData(String headline, String guideIntro, List<Entry> overview, List<Entry> specialVillagers, List<Entry> cosmetics, List<Entry> generalInformation, List<Entry> socials, List<Entry> settings, String support, List<Category> categories, Map<String, Entry> contexts, List<Entry> searchable) {
    }

    public record Entry(String title, String body) {
    }

    public record Category(String title, List<Section> sections) {
    }

    public record Section(String title, List<String> groups, List<Entry> entries) {
    }
}

