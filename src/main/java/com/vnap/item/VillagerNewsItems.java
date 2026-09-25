/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.DynamicOps
 *  net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab
 *  net.minecraft.core.Registry
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.NbtOps
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.ComponentSerialization
 *  net.minecraft.resources.Identifier
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EntityTypes
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.item.CreativeModeTab
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.SpawnEggItem
 *  net.minecraft.world.item.component.TypedEntityData
 *  net.minecraft.world.level.ItemLike
 */
package com.vnap.item;

import com.mojang.serialization.DynamicOps;
import com.vnap.VillagerNewsAddonPort;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.ItemLike;

public final class VillagerNewsItems {
    public static final ResourceKey<CreativeModeTab> CREATIVE_TAB_KEY = ResourceKey.create((ResourceKey)Registries.CREATIVE_MODE_TAB, (Identifier)VillagerNewsAddonPort.id("items"));
    public static final Item HANDBOOK = VillagerNewsItems.register("handbook", properties -> new Item(properties.stacksTo(1)));
    public static final Item MAYOR_HAT = VillagerNewsItems.register("mayor_hat", properties -> new Item(properties.stacksTo(1).equippable(EquipmentSlot.HEAD)));
    public static final Item MICROPHONE = VillagerNewsItems.register("microphone", properties -> new Item(properties.stacksTo(1)));
    public static final Item MOUSTACHE = VillagerNewsItems.register("moustache", properties -> new Item(properties.stacksTo(1).equippable(EquipmentSlot.HEAD)));
    public static final Item TESTIFICATE_MAN_HELMET = VillagerNewsItems.register("testificate_man_helmet", properties -> new Item(properties.stacksTo(1).equippable(EquipmentSlot.HEAD)));
    public static final Item VILLAGER_NOSE = VillagerNewsItems.register("villager_nose", properties -> new Item(properties.stacksTo(1).equippable(EquipmentSlot.HEAD)));
    public static final Item MAYOR_VILLAGER_SPAWN_EGG = VillagerNewsItems.registerSpawnEgg("mayor_villager_spawn_egg", EntityTypes.VILLAGER, "Mayor Villager");
    public static final Item TESTIFICATE_MAN_SPAWN_EGG = VillagerNewsItems.registerSpawnEgg("testificate_man_spawn_egg", EntityTypes.VILLAGER, "Testificate Man");
    public static final Item VILLAGER_5_SPAWN_EGG = VillagerNewsItems.registerSpawnEgg("villager_5_spawn_egg", EntityTypes.VILLAGER, "Villager #5");
    public static final Item VILLAGER_9_SPAWN_EGG = VillagerNewsItems.registerSpawnEgg("villager_9_spawn_egg", EntityTypes.VILLAGER, "Villager #9");
    public static final Item UNTOUCHABLE_VILLAGER_SPAWN_EGG = VillagerNewsItems.registerSpawnEgg("untouchable_villager_spawn_egg", EntityTypes.VILLAGER, "Villager Unreachable");
    public static final Item WOOLY_SPAWN_EGG = VillagerNewsItems.registerSpawnEgg("wooly_spawn_egg", EntityTypes.SHEEP, "Wooly The Sheep");
    private static final Map<Item, Integer> COSMETICS = new LinkedHashMap<Item, Integer>();

    private VillagerNewsItems() {
    }

    public static void register() {
        Registry.register((Registry)BuiltInRegistries.CREATIVE_MODE_TAB, CREATIVE_TAB_KEY, (Object)FabricCreativeModeTab.builder().title((Component)Component.translatable((String)"itemGroup.villager-news-addon-port.items")).icon(() -> new ItemStack((ItemLike)HANDBOOK)).displayItems((parameters, output) -> {
            output.accept((ItemLike)HANDBOOK);
            output.accept((ItemLike)MAYOR_HAT);
            output.accept((ItemLike)TESTIFICATE_MAN_HELMET);
            output.accept((ItemLike)MICROPHONE);
            output.accept((ItemLike)MOUSTACHE);
            output.accept((ItemLike)VILLAGER_NOSE);
            output.accept((ItemLike)MAYOR_VILLAGER_SPAWN_EGG);
            output.accept((ItemLike)TESTIFICATE_MAN_SPAWN_EGG);
            output.accept((ItemLike)VILLAGER_5_SPAWN_EGG);
            output.accept((ItemLike)VILLAGER_9_SPAWN_EGG);
            output.accept((ItemLike)UNTOUCHABLE_VILLAGER_SPAWN_EGG);
            output.accept((ItemLike)WOOLY_SPAWN_EGG);
        }).build());
    }

    public static int cosmetic(Item item) {
        return COSMETICS.getOrDefault(item, 0);
    }

    public static Item cosmeticItem(int cosmetic) {
        return COSMETICS.entrySet().stream().filter(entry -> (Integer)entry.getValue() == cosmetic).map(Map.Entry::getKey).findFirst().orElse(null);
    }

    private static Item register(String path, Function<Item.Properties, Item> factory) {
        ResourceKey key = ResourceKey.create((ResourceKey)Registries.ITEM, (Identifier)VillagerNewsAddonPort.id(path));
        Item item = factory.apply(new Item.Properties().setId(key));
        return (Item)Registry.register((Registry)BuiltInRegistries.ITEM, (ResourceKey)key, (Object)item);
    }

    private static Item registerSpawnEgg(String path, EntityType<?> type, String entityName) {
        CompoundTag tag = new CompoundTag();
        tag.put("CustomName", (Tag)ComponentSerialization.CODEC.encodeStart((DynamicOps)NbtOps.INSTANCE, (Object)Component.literal((String)entityName)).getOrThrow());
        tag.putBoolean("PersistenceRequired", true);
        TypedEntityData data = TypedEntityData.of(type, (CompoundTag)tag);
        return VillagerNewsItems.register(path, properties -> new SpawnEggItem(properties.spawnEgg(type).component(DataComponents.ENTITY_DATA, (Object)data)));
    }

    static {
        COSMETICS.put(MAYOR_HAT, 1);
        COSMETICS.put(TESTIFICATE_MAN_HELMET, 2);
        COSMETICS.put(MICROPHONE, 3);
        COSMETICS.put(MOUSTACHE, 4);
    }
}

