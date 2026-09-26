/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents
 *  net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
 *  net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
 *  net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
 *  net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
 *  net.fabricmc.fabric.api.event.player.AttackEntityCallback
 *  net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
 *  net.fabricmc.fabric.api.event.player.UseBlockCallback
 *  net.fabricmc.fabric.api.event.player.UseEntityCallback
 *  net.fabricmc.fabric.api.event.player.UseItemCallback
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.HolderGetter$Provider
 *  net.minecraft.core.Vec3i
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.ServerScoreboard
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.Difficulty
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.attribute.EnvironmentAttributes
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.damagesource.DamageTypes
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.AgeableMob
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntitySpawnReason
 *  net.minecraft.world.entity.EntityTypes
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.TamableAnimal
 *  net.minecraft.world.entity.ai.memory.MemoryModuleType
 *  net.minecraft.world.entity.ai.util.DefaultRandomPos
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.npc.villager.Villager
 *  net.minecraft.world.entity.npc.villager.VillagerProfession
 *  net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.MerchantMenu
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.DyeColor
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.trading.ItemCost
 *  net.minecraft.world.item.trading.MerchantOffer
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.scores.Objective
 *  net.minecraft.world.scores.ScoreHolder
 *  net.minecraft.world.scores.Scoreboard
 *  net.minecraft.world.scores.criteria.ObjectiveCriteria
 *  net.minecraft.world.scores.criteria.ObjectiveCriteria$RenderType
 */
package com.vnap.dialogue;

import com.vnap.config.VillagerNewsSettings;
import com.vnap.dialogue.DialogueCatalog;
import com.vnap.entity.VillagerNewsData;
import com.vnap.item.VillagerNewsItems;
import com.vnap.network.DialogueAnimationNetwork;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

public final class ContextualDialogueController {
    public static final String DIALOGUE_TEST_TAG = "vnap_dialogue_test";
    private static final double OBSERVER_RANGE = 16.0;
    private static final double NEARBY_SUBJECT_RANGE = 8.0;
    private static final long SHORT_COOLDOWN = 120L;
    private static final long LONG_COOLDOWN = 400L;
    private static final Map<String, Long> COOLDOWNS = new HashMap<String, Long>();
    private static final Map<UUID, Long> BUSY_UNTIL = new HashMap<UUID, Long>();
    private static final Map<UUID, ActiveSound> ACTIVE_SOUNDS = new HashMap<UUID, ActiveSound>();
    private static final Map<UUID, PlayerObservation> PLAYER_OBSERVATIONS = new HashMap<UUID, PlayerObservation>();
    private static final Map<UUID, Integer> PLAYER_DEATHS = new HashMap<UUID, Integer>();
    private static final Map<UUID, Boolean> LAST_SLEEPING = new HashMap<UUID, Boolean>();
    private static final Map<UUID, Boolean> LAST_TRADER_INVISIBLE = new HashMap<UUID, Boolean>();
    private static final Map<UUID, VillagerSnapshot> VILLAGER_STATES = new HashMap<UUID, VillagerSnapshot>();
    private static final Map<UUID, Set<String>> CONDITION_HISTORY = new HashMap<UUID, Set<String>>();
    private static final Map<UUID, Integer> CONDITION_CURSORS = new HashMap<UUID, Integer>();
    private static final Map<UUID, PendingConditionRelief> PENDING_CONDITION_RELIEF = new HashMap<UUID, PendingConditionRelief>();
    private static final Map<UUID, Map<String, Integer>> VILLAGER_INVENTORIES = new HashMap<UUID, Map<String, Integer>>();
    private static final Map<UUID, SpeechTarget> SPEECH_TARGETS = new HashMap<UUID, SpeechTarget>();
    private static final Map<String, List<Integer>> SHARED_RECENT_VARIANTS = new HashMap<String, List<Integer>>();
    private static final Map<UUID, Long> NO_WORKSTATION_SINCE = new HashMap<UUID, Long>();
    private static final Map<UUID, Long> LAST_DANGER = new HashMap<UUID, Long>();
    private static final Map<UUID, Long> NO_BELL_SINCE = new HashMap<UUID, Long>();
    private static final Map<UUID, TradeSession> ACTIVE_TRADES = new HashMap<UUID, TradeSession>();
    private static final Map<UUID, UnreachableState> UNREACHABLE_STATES = new HashMap<UUID, UnreachableState>();
    private static final Set<UUID> ACTIVE_PLAYER_ENCOUNTERS = new HashSet<UUID>();
    private static final Map<UUID, PendingSleep> PENDING_SLEEP = new HashMap<UUID, PendingSleep>();
    private static final Map<UUID, PendingWake> PENDING_WAKE = new HashMap<UUID, PendingWake>();
    private static final List<PendingBell> PENDING_BELLS = new ArrayList<PendingBell>();
    private static final List<PendingBellReaction> PENDING_BELL_REACTIONS = new ArrayList<PendingBellReaction>();
    private static final Map<UUID, UUID> WAKE_SOURCES = new HashMap<UUID, UUID>();
    private static final Set<UUID> SLEEP_BYPASS = new HashSet<UUID>();
    private static final Set<UUID> INTERRUPTED_SLEEP = new HashSet<UUID>();
    private static final List<PendingSpeech> PENDING_SPEECH = new ArrayList<PendingSpeech>();
    private static final Map<String, Long> LAST_LEVEL_TIME = new HashMap<String, Long>();
    private static final Map<String, Difficulty> LAST_DIFFICULTY = new HashMap<String, Difficulty>();
    private static final Map<String, Integer> PAIR_TICKS = new HashMap<String, Integer>();
    private static final String NATURAL_SPECIAL_TAG = "vnap_natural_special";
    private static final String SPECIAL_OBJECTIVE = "vnap_special";
    private static final String SPECIAL_X_OBJECTIVE = "vnap_special_x";
    private static final String SPECIAL_Z_OBJECTIVE = "vnap_special_z";
    private static final Map<String, String> NATURAL_SPECIAL_NAMES = Map.of("mayor", "The Mayor", "testificate", "Testificate Man", "number_5", "Villager #5", "number_9", "Villager #9", "unreachable", "Can't Catch Me!", "wooly", "Wooly The Sheep");
    private static final Set<String> SPECIAL_NAMES = Set.of("Mayor Villager", "The Mayor", "Testificate Man", "Villager #5", "Villager #9", "Villager Unreachable", "Can't Catch Me!", "Wooly The Sheep");
    private static final Set<String> MOBILE_DIALOGUES = Set.of("huhcbd", "gesjov", "gacgtq", "hmadgp", "nkcoqb", "qhpyaw", "uveohs", "caykki", "swomdw", "rtikom", "hfmwvf", "mytmrk", "ikrwzy", "fcbygh", "etkxko", "elryje", "rogpvp", "igebly", "vnaodx", "yzqpvi", "nsxmkr", "cifbit", "wyvzhk", "rueszy", "yjctyw", "qqyjjg", "hpnsfu", "vevdkl", "ahcvzd", "ecslqo", "ssbhiv", "ltdnvy", "fzoqwd", "behifz", "wrbvvp", "asuufu", "eyiraw", "ncyeaw", "uzdxum", "lpuocy", "slbqfwbayahw");
    private static final Set<String> BABY_DIALOGUES = Set.of("abfwiv", "aezdiy", "ahcvzd", "cmrqhw", "durjjd", "ecslqo", "fzyrfm", "ggitzq", "gotjxf", "gzsztp", "hbalps", "hcdvqm", "jfuftm", "lgjtnf", "mqnapy", "msemoe", "nxalcz", "qrdzmt", "rfnirh", "saxuwk", "svdjdk", "vbclem", "vhwksn", "wkwcrf", "wsxfok", "wtuguc", "zeykfp", "cxeziv", "riezum", "rlkdqd");
    private static final Set<String> COSMETIC_RECIPIENT_DIALOGUES = Set.of("wurmgu", "inirxg", "ozxzla", "cxeziv", "riezum", "rlkdqd");
    private static final Set<String> ONGOING_DAMAGE_DIALOGUES = Set.of("elryje", "rogpvp", "etkxko", "igebly", "vnaodx");
    private static final Set<String> DAMAGE_LOCK_DIALOGUES = Set.of("huhcbd", "gesjov", "gacgtq", "hmadgp", "nkcoqb", "qhpyaw", "uveohs", "caykki", "swomdw", "rtikom", "hfmwvf", "mytmrk", "ikrwzy", "fcbygh", "elryje", "rogpvp", "etkxko", "igebly", "vnaodx", "yzqpvi", "nsxmkr", "cifbit", "wyvzhk", "rueszy", "yjctyw", "qqyjjg", "hpnsfu", "vevdkl", "ahcvzd", "ecslqo", "ssbhiv", "ltdnvy", "fzoqwd", "behifz", "wrbvvp", "asuufu", "eyiraw", "ncyeaw", "onindz", "xemyaj", "yebifs");
    private static final List<List<String>> WANDERING_CONVERSATIONS = List.of(List.of("gmrypkswxeva", "gmrypkbayahw", "gmrypkmudlec"), List.of("gmrypkoallbt", "gmrypkfobzlt", "gmrypkcljvls"), List.of("gmrypkhiqnpi", "gmrypkvkuidc", "gmrypkhnvsiu", "gmrypkvswnrg"), List.of("gmrypkmwtiaf", "gmrypkgougka"));
    private static final List<String> CAMPFIRE_CONVERSATION = List.of("wrswgiswxeva", "wrswgibayahw", "wrswgimudlec", "wrswgitvewwu", "wrswgisrlwzw", "wrswgicsmkgk");
    private static final List<String> GOSSIP_CONVERSATION = List.of("wrjbddswxeva", "wrjbddbayahw", "wrjbddmudlec", "wrjbddtvewwu", "wrjbddsrlwzw");
    private static final List<List<String>> ONE_MISSING_NOSE_CONVERSATIONS = List.of(List.of("bygaxwswxeva", "bygaxwbayahw"), List.of("bygaxwoallbt", "bygaxwfobzlt", "bygaxwcljvls"), List.of("bygaxwhiqnpi"), List.of("bygaxwmwtiaf"));
    private static final List<List<String>> TWO_MISSING_NOSES_CONVERSATIONS = List.of(List.of("loicswswxeva", "loicswbayahw"), List.of("loicswrotbcq"), List.of("loicswhiqnpi", "loicswvkuidc", "loicswhnvsiu"));
    private static final Map<String, String> NEARBY_ENTITY_DIALOGUES = Map.ofEntries(Map.entry("allay", "rnlher"), Map.entry("armor_stand", "ckniqq"), Map.entry("bat", "ozmthf"), Map.entry("bee", "rbkjsr"), Map.entry("cave_spider", "gtmfpl"), Map.entry("bogged", "nsosix"), Map.entry("camel", "turlrl"), Map.entry("cat", "ynxhfb"), Map.entry("chicken", "hggexx"), Map.entry("cow", "lvzfcv"), Map.entry("creaking", "nwlcij"), Map.entry("creeper", "odwhzm"), Map.entry("dolphin", "aqtshb"), Map.entry("drowned", "atwycp"), Map.entry("enderman", "yeqxvm"), Map.entry("frog", "pguaqp"), Map.entry("horse", "yazvzs"), Map.entry("husk", "gcoysc"), Map.entry("llama", "ysbfqu"), Map.entry("trader_llama", "ysbfqu"), Map.entry("panda", "swewsr"), Map.entry("parrot", "vapupl"), Map.entry("phantom", "nwzvkb"), Map.entry("pig", "jqdeef"), Map.entry("rabbit", "spfefr"), Map.entry("sheep", "vxycol"), Map.entry("skeleton", "lqzdqk"), Map.entry("slime", "rzvitn"), Map.entry("sniffer", "tqishj"), Map.entry("spider", "gtmfpl"), Map.entry("stray", "bxbibd"), Map.entry("turtle", "neoxpu"), Map.entry("warden", "jicosq"), Map.entry("witch", "lwcrnt"), Map.entry("wither", "satsrf"), Map.entry("wolf", "vvntcf"), Map.entry("zombie", "dortcb"), Map.entry("zombie_villager", "xtooxu"), Map.entry("zombified_piglin", "wboncy"), Map.entry("copper_golem", "ktdshy"), Map.entry("snow_golem", "kxjegd"), Map.entry("iron_golem", "cuchwi"), Map.entry("ender_dragon", "xxjkmo"), Map.entry("happy_ghast", "lxvofx"), Map.entry("polar_bear", "toolzx"), Map.entry("sulfur_cube", "dmcjmd"), Map.entry("cod", "trkugw"), Map.entry("salmon", "trkugw"), Map.entry("pufferfish", "trkugw"), Map.entry("tropical_fish", "trkugw"));
    private static final Map<String, String> BABY_ENTITY_DIALOGUES = Map.ofEntries(Map.entry("bee", "qqtnlm"), Map.entry("cat", "knjdbi"), Map.entry("chicken", "pjcwec"), Map.entry("cow", "hzahog"), Map.entry("drowned", "vakwgb"), Map.entry("horse", "ualabt"), Map.entry("husk", "hwltxk"), Map.entry("panda", "gggzar"), Map.entry("pig", "htibul"), Map.entry("sheep", "eccdga"), Map.entry("wolf", "hyzwpr"), Map.entry("zombie", "zvwapr"), Map.entry("zombified_piglin", "qltnkz"), Map.entry("zombie_villager", "nstwos"));
    private static long ticks;

    private ContextualDialogueController() {
    }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(ContextualDialogueController::tick);
        ServerEntityEvents.ENTITY_LOAD.register(ContextualDialogueController::onEntityLoad);
        ServerEntityEvents.ENTITY_UNLOAD.register((entity, level) -> {
            UUID id = entity.getUUID();
            String encodedId = id.toString();
            BUSY_UNTIL.remove(id);
            ACTIVE_SOUNDS.remove(id);
            SPEECH_TARGETS.remove(id);
            LAST_SLEEPING.remove(id);
            LAST_TRADER_INVISIBLE.remove(id);
            VILLAGER_STATES.remove(id);
            CONDITION_HISTORY.remove(id);
            CONDITION_CURSORS.remove(id);
            PENDING_CONDITION_RELIEF.remove(id);
            VILLAGER_INVENTORIES.remove(id);
            NO_WORKSTATION_SINCE.remove(id);
            LAST_DANGER.remove(id);
            NO_BELL_SINCE.remove(id);
            PLAYER_OBSERVATIONS.remove(id);
            PLAYER_DEATHS.remove(id);
            ACTIVE_TRADES.remove(id);
            ACTIVE_PLAYER_ENCOUNTERS.remove(id);
            ACTIVE_TRADES.entrySet().removeIf(entry -> ((TradeSession)entry.getValue()).traderId.equals(id));
            UNREACHABLE_STATES.remove(id);
            PENDING_SLEEP.remove(id);
            PENDING_WAKE.remove(id);
            WAKE_SOURCES.remove(id);
            SLEEP_BYPASS.remove(id);
            INTERRUPTED_SLEEP.remove(id);
            PENDING_SPEECH.removeIf(pending -> pending.speakerId.equals(id) || id.equals(pending.targetId));
            PAIR_TICKS.keySet().removeIf(pair -> pair.contains(encodedId));
        });
        PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
            if (level instanceof ServerLevel) {
                ServerLevel serverLevel = (ServerLevel)level;
                if (ContextualDialogueController.queueFreedSuffocationRelief(serverLevel, pos)) {
                    return;
                }
                PlayerObservation observation = PLAYER_OBSERVATIONS.computeIfAbsent(player.getUUID(), ignored -> new PlayerObservation());
                String title = ContextualDialogueController.selectBreakContext(state, observation);
                if (title.equals("Harvest Crops") && ContextualDialogueController.nearbyVillagers(serverLevel, Vec3.atCenterOf((Vec3i)pos), 16.0).stream().anyMatch(villager -> ContextualDialogueController.profession(villager).equals("farmer"))) {
                    title = "Harvest Crops Near a Farmer";
                }
                ContextualDialogueController.playObserved(serverLevel, player, Vec3.atCenterOf((Vec3i)pos), title, 900L);
            }
        });
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
            if (level instanceof ServerLevel) {
                boolean played;
                String heldPath;
                Item patt0$temp;
                ServerLevel serverLevel = (ServerLevel)level;
                ItemStack held = player.getItemInHand(hand);
                BlockState clicked = level.getBlockState(hitResult.getBlockPos());
                String clickedPath = BuiltInRegistries.BLOCK.getKey(clicked.getBlock()).getPath();
                if (clickedPath.equals("bell")) {
                    ContextualDialogueController.queueBell(serverLevel, hitResult.getLocation());
                    return InteractionResult.PASS;
                }
                String title = ContextualDialogueController.selectHeldBlockContext(held, clicked);
                if (title == null && (patt0$temp = held.getItem()) instanceof BlockItem) {
                    BlockItem blockItem = (BlockItem)patt0$temp;
                    title = ContextualDialogueController.selectPlaceContext(blockItem.getBlock(), serverLevel, hitResult.getBlockPos());
                } else if (title == null) {
                    title = ContextualDialogueController.selectUseBlockContext(clicked);
                }
                if (clickedPath.endsWith("_door") && clicked.hasProperty((Property)BlockStateProperties.OPEN) && ((Boolean)clicked.getValue((Property)BlockStateProperties.OPEN)).booleanValue() && !ContextualDialogueController.nearbyVillagers(serverLevel, hitResult.getLocation(), 3.0).isEmpty()) {
                    title = "Close a Door in a Villager's Face";
                }
                if (((heldPath = BuiltInRegistries.ITEM.getKey(held.getItem()).getPath()).equals("pumpkin") || heldPath.equals("carved_pumpkin")) && ContextualDialogueController.nearBlock(serverLevel, hitResult.getBlockPos(), "iron_block", 3)) {
                    title = "Build an Iron Golem Frame";
                }
                boolean bl = played = (clickedPath.equals("chest") || clickedPath.equals("trapped_chest")) && ContextualDialogueController.playHomeChestReaction(serverLevel, player, hitResult.getBlockPos());
                if (!played) {
                    played = title != null && ContextualDialogueController.playObserved(serverLevel, player, hitResult.getLocation(), title, 900L);
                }
            }
            return InteractionResult.PASS;
        });
        UseItemCallback.EVENT.register((player, level, hand) -> {
            if (level instanceof ServerLevel) {
                ServerLevel serverLevel = (ServerLevel)level;
                String title = ContextualDialogueController.selectUseItemContext(player.getItemInHand(hand));
                if (title != null) {
                    ContextualDialogueController.playObserved(serverLevel, player, player.position(), title, 900L);
                }
            }
            return InteractionResult.PASS;
        });
        UseEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> {
            if (level instanceof ServerLevel) {
                return ContextualDialogueController.onUseEntity(player, entity, hand);
            }
            return InteractionResult.PASS;
        });
        AttackEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> {
            Villager villager;
            if (entity.entityTags().contains(DIALOGUE_TEST_TAG)) {
                return InteractionResult.SUCCESS;
            }
            if (level instanceof ServerLevel && entity instanceof Villager && ContextualDialogueController.cast(villager = (Villager)entity) == CastProfile.UNREACHABLE) {
                ContextualDialogueController.repelPlayer(villager, player);
                return InteractionResult.SUCCESS;
            }
            if (level instanceof ServerLevel) {
                ContextualDialogueController.onAttackEntity(player, entity);
            }
            return InteractionResult.PASS;
        });
        EntitySleepEvents.STOP_SLEEPING.register((entity, sleepingPos) -> {
            if (entity instanceof ServerPlayer) {
                ServerPlayer player = (ServerPlayer)entity;
                boolean armored = List.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET).stream().anyMatch(slot -> !player.getItemBySlot(slot).isEmpty());
                ContextualDialogueController.playObserved(player.level(), (Player)player, player.position(), armored ? "Wake Up in Armor" : "Player Wakes Up", 3000L);
            } else if (entity instanceof Villager) {
                Villager villager = (Villager)entity;
                UUID id = villager.getUUID();
                boolean explicitlyInterrupted = INTERRUPTED_SLEEP.remove(id);
                PENDING_SLEEP.remove(id);
                SLEEP_BYPASS.remove(id);
                ContextualDialogueController.interrupt((LivingEntity)villager);
                Level patt0$temp = entity.level();
                if (patt0$temp instanceof ServerLevel) {
                    ServerLevel level = (ServerLevel)patt0$temp;
                    boolean interrupted = explicitlyInterrupted || ContextualDialogueController.isVillagerSleepTime(villager, level);
                    String dialogue = interrupted ? "viwaal" : "ctptjt";
                    UUID targetId = interrupted ? WAKE_SOURCES.remove(id) : null;
                    PENDING_WAKE.put(id, new PendingWake(level, id, dialogue, targetId, ticks + 4L));
                    LAST_SLEEPING.put(id, false);
                } else {
                    PENDING_WAKE.remove(id);
                    WAKE_SOURCES.remove(id);
                }
            }
        });
        ServerLivingEntityEvents.AFTER_DAMAGE.register(ContextualDialogueController::onDamage);
        ServerLivingEntityEvents.AFTER_DEATH.register(ContextualDialogueController::onDeath);
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> ContextualDialogueController.clearState());
    }

    private static void onEntityLoad(Entity entity, ServerLevel level) {
        if (entity.entityTags().contains(DIALOGUE_TEST_TAG)) {
            return;
        }
        ContextualDialogueController.normalizeSpecialEntity(entity);
        String entityPath = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).getPath();
        if (entityPath.equals("firework_rocket")) {
            ContextualDialogueController.playFireworkReactions(level, entity);
            return;
        }
        if (entityPath.equals("lightning_bolt")) {
            ContextualDialogueController.playLightningReaction(level, entity);
            return;
        }
        if (entity instanceof Sheep) {
            Sheep sheep = (Sheep)entity;
            if (ContextualDialogueController.isWooly(sheep) && sheep.getColor() == DyeColor.RED) {
                sheep.setColor(DyeColor.WHITE);
            }
            return;
        }
        if (!(entity instanceof Villager)) {
            return;
        }
        Villager villager = (Villager)entity;
        if (ContextualDialogueController.tryCreateNaturalSpecial(villager, level)) {
            return;
        }
        ContextualDialogueController.ensureSpecialTrade(villager);
        if (ContextualDialogueController.cast(villager) == CastProfile.UNREACHABLE) {
            UNREACHABLE_STATES.putIfAbsent(villager.getUUID(), new UnreachableState(ticks));
        }
        VILLAGER_STATES.put(villager.getUUID(), ContextualDialogueController.snapshot(villager, false));
        VILLAGER_INVENTORIES.put(villager.getUUID(), ContextualDialogueController.inventoryCounts(villager));
        EntitySpawnReason reason = villager.spawnReason();
        if (reason == EntitySpawnReason.SPAWN_ITEM_USE || reason == EntitySpawnReason.DISPENSER) {
            ServerPlayer player = ContextualDialogueController.nearestPlayer(level, villager.position(), 12.0);
            PENDING_SPEECH.add(new PendingSpeech(level, villager.getUUID(), villager.isBaby() ? "abfwiv" : "vskjkl", player == null ? null : player.getUUID(), ticks + 2L));
        } else if (reason == EntitySpawnReason.BREEDING) {
            Villager parent = ContextualDialogueController.nearbyVillagers(level, villager.position(), 12.0).stream().filter(other -> other != villager && !other.isBaby()).min(Comparator.comparingDouble(other -> other.distanceToSqr((Entity)villager))).orElse(null);
            if (parent != null) {
                PENDING_SPEECH.add(new PendingSpeech(level, parent.getUUID(), "fbuabj", villager.getUUID(), ticks + 2L, true));
                PENDING_SPEECH.add(new PendingSpeech(level, villager.getUUID(), "lgjtnf", parent.getUUID(), ticks + DialogueCatalog.byId("fbuabj").durationTicks() + 4L));
            } else {
                PENDING_SPEECH.add(new PendingSpeech(level, villager.getUUID(), "lgjtnf", null, ticks + 2L));
            }
        } else if (reason == EntitySpawnReason.CONVERSION) {
            PENDING_SPEECH.add(new PendingSpeech(level, villager.getUUID(), villager.isBaby() ? "ggitzq" : "ivumgm", null, ticks + 2L));
        }
    }

    public static void onBabySpawnedFromEgg(Villager villager, Player player) {
        ServerLevel level;
        block3: {
            block2: {
                Level level2 = villager.level();
                if (!(level2 instanceof ServerLevel)) break block2;
                level = (ServerLevel)level2;
                if (villager.isBaby() && !villager.entityTags().contains(DIALOGUE_TEST_TAG)) break block3;
            }
            return;
        }
        UUID id = villager.getUUID();
        PENDING_SPEECH.removeIf(pending -> pending.speakerId.equals(id) || id.equals(pending.targetId));
        PENDING_SPEECH.add(new PendingSpeech(level, id, "abfwiv", player.getUUID(), ticks + 2L));
    }

    private static void processPendingSpeech() {
        PENDING_SPEECH.removeIf(pending -> {
            LivingEntity living;
            Entity target;
            if (pending.dueTick > ticks) {
                return false;
            }
            Entity speaker = pending.level.getEntity(pending.speakerId);
            Entity entity = target = pending.targetId == null ? null : pending.level.getEntity(pending.targetId);
            if (speaker instanceof LivingEntity && (living = (LivingEntity)speaker).isAlive()) {
                boolean played;
                DialogueCatalog.DialogueGroup group = DialogueCatalog.byId(pending.dialogueId);
                played = group != null && (pending.sharedAdult ? ContextualDialogueController.playSharedId(living, pending.dialogueId, "queued:" + living.getUUID() + ":" + pending.dialogueId, 1L, target) : ContextualDialogueController.playId(living, pending.dialogueId, "queued:" + living.getUUID() + ":" + pending.dialogueId, 1L, target));
                if (played && target instanceof LivingEntity) {
                    LivingEntity listener = (LivingEntity)target;
                    ContextualDialogueController.holdListener(listener, (Entity)living, group.durationTicks());
                }
            }
            return true;
        });
    }

    private static void maintainSpeechTargets(MinecraftServer server) {
        SPEECH_TARGETS.entrySet().removeIf(entry -> {
            SpeechTarget speech = (SpeechTarget)entry.getValue();
            if (speech.untilTick <= ticks) {
                return true;
            }
            for (ServerLevel level : server.getAllLevels()) {
                Vec3 position;
                Mob mob;
                Entity speaker = level.getEntity((UUID)entry.getKey());
                if (!(speaker instanceof Mob) || !(mob = (Mob)speaker).isAlive()) continue;
                Entity target = speech.targetId == null ? null : level.getEntity(speech.targetId);
                Vec3 vec3 = position = target != null && target.isAlive() ? target.getEyePosition() : speech.position;
                if (speech.lockMovement) {
                    ContextualDialogueController.holdMob(mob, position);
                } else {
                    ContextualDialogueController.faceMob(mob, position);
                }
                return false;
            }
            return true;
        });
    }

    private static void processTradeSessions(MinecraftServer server) {
        ACTIVE_TRADES.entrySet().removeIf(entry -> {
            Villager villager;
            String id;
            Entity trader;
            ServerPlayer player = server.getPlayerList().getPlayer((UUID)entry.getKey());
            TradeSession session = (TradeSession)entry.getValue();
            if (player != null && player.containerMenu instanceof MerchantMenu) {
                if (!session.opened) {
                    session.opened = true;
                    Entity trader2 = session.level.getEntity(session.traderId);
                    if (trader2 instanceof Villager) {
                        Villager villager2 = (Villager)trader2;
                        String id2 = ContextualDialogueController.tradeOpeningId(villager2, (Player)player);
                        ContextualDialogueController.playId((LivingEntity)villager2, id2, "trade_open:" + String.valueOf(villager2.getUUID()) + ":" + id2, 900L, (Entity)player);
                    } else if (trader2 instanceof WanderingTrader) {
                        WanderingTrader wanderingTrader = (WanderingTrader)trader2;
                        ContextualDialogueController.playId((LivingEntity)wanderingTrader, "yubpbb", "trade_open:" + String.valueOf(wanderingTrader.getUUID()), 900L, (Entity)player);
                    }
                }
                return false;
            }
            if (!session.opened && ticks - session.createdTick <= 20L) {
                return false;
            }
            if (!session.opened && player != null && (trader = session.level.getEntity(session.traderId)) instanceof Villager && (id = ContextualDialogueController.unavailableTradeId(villager = (Villager)trader, (Player)player)) != null) {
                ContextualDialogueController.playId((LivingEntity)villager, id, "trade_unavailable:" + String.valueOf(villager.getUUID()) + ":" + id, 900L, (Entity)player);
            }
            if (session.opened) {
                trader = session.level.getEntity(session.traderId);
                if (trader instanceof Villager) {
                    villager = (Villager)trader;
                    CastProfile profile = ContextualDialogueController.cast(villager);
                    String id3 = switch (profile.ordinal()) {
                        case 1 -> {
                            if (session.completed) {
                                yield "shrrya";
                            }
                            yield "bgzmea";
                        }
                        case 2 -> {
                            if (session.completed) {
                                yield "xcjort";
                            }
                            yield "rdugrl";
                        }
                        case 3 -> {
                            if (session.completed) {
                                yield "msofrj";
                            }
                            yield "lilimm";
                        }
                        case 4 -> {
                            if (session.completed) {
                                yield "czvvwy";
                            }
                            yield "lilimm";
                        }
                        default -> session.completed ? "czvvwy" : (ticks % 2L == 0L ? "lilimm" : "laztau");
                    };
                    boolean played = ContextualDialogueController.playId((LivingEntity)villager, id3, "trade_close:" + String.valueOf(villager.getUUID()), 10L, (Entity)player);
                    if (!played) {
                        String fallback = profile == CastProfile.TESTIFICATE_MAN ? "ctzfzj" : (profile == CastProfile.NUMBER_5 ? "nfdery" : (profile == CastProfile.NUMBER_9 ? "hvjfnk" : null));
                        if (fallback != null) {
                            ContextualDialogueController.playId((LivingEntity)villager, fallback, "trade_close_fallback:" + String.valueOf(villager.getUUID()), 10L, (Entity)player);
                        }
                    }
                } else if (trader instanceof WanderingTrader) {
                    WanderingTrader wanderingTrader = (WanderingTrader)trader;
                    ContextualDialogueController.playId((LivingEntity)wanderingTrader, session.completed ? "uzdvsi" : "erbcfn", "trade_close:" + String.valueOf(wanderingTrader.getUUID()), 10L, (Entity)player);
                }
            }
            return true;
        });
    }

    private static String tradeOpeningId(Villager villager, Player player) {
        CastProfile profile = ContextualDialogueController.cast(villager);
        if (profile != CastProfile.VILLAGER) {
            return profile.trade;
        }
        String unavailable = ContextualDialogueController.unavailableTradeId(villager, player);
        if (unavailable != null) {
            return unavailable;
        }
        int reputation = villager.getPlayerReputation(player);
        if (reputation < -225) {
            return "xduuwm";
        }
        if (reputation < -75) {
            return "qmdvft";
        }
        if (reputation >= 75) {
            return "vlrsrn";
        }
        if (reputation >= 25) {
            return "kuhvdv";
        }
        return profile.trade;
    }

    private static String unavailableTradeId(Villager villager, Player player) {
        ServerLevel level;
        if (ContextualDialogueController.cast(villager) != CastProfile.VILLAGER) {
            return null;
        }
        String profession = ContextualDialogueController.profession(villager);
        if (profession.equals("nitwit")) {
            return "nukxsf";
        }
        if (profession.equals("none")) {
            return "nlbhku";
        }
        Level level2 = villager.level();
        if (level2 instanceof ServerLevel && (level = (ServerLevel)level2).isRaided(villager.blockPosition())) {
            return "klabhl";
        }
        if (villager.getOffers().isEmpty()) {
            return "zalmof";
        }
        if (villager.getPlayerReputation(player) <= -150) {
            return "lhdgsy";
        }
        return null;
    }

    private static ServerPlayer nearestPlayer(ServerLevel level, Vec3 position, double range) {
        return level.players().stream().filter(LivingEntity::isAlive).filter(player -> player.distanceToSqr(position) <= range * range).min(Comparator.comparingDouble(player -> player.distanceToSqr(position))).orElse(null);
    }

    private static void processTimeChange(ServerLevel level) {
        boolean isDay;
        long now;
        String key = level.dimension().identifier().toString();
        Long before = LAST_LEVEL_TIME.put(key, now = level.getOverworldClockTime());
        if (before == null || Math.abs(now - before) <= 40L || level.players().isEmpty()) {
            return;
        }
        ServerPlayer player = (ServerPlayer)level.players().getFirst();
        Villager speaker = ContextualDialogueController.nearbyVillagers(level, player.position(), 32.0).stream().filter(villager -> !villager.isSleeping()).min(Comparator.comparingDouble(villager -> villager.distanceToSqr((Entity)player))).orElse(null);
        if (speaker == null) {
            return;
        }
        boolean wasDay = Math.floorMod((long)before, 24000L) < 12000L;
        boolean bl = isDay = Math.floorMod(now, 24000L) < 12000L;
        String id = wasDay == isDay ? (speaker.isBaby() ? "durjjd" : "uqwdqn") : (isDay ? (speaker.isBaby() ? "wkwcrf" : "mgmzeh") : (speaker.isBaby() ? "msemoe" : "ohdwnz"));
        ContextualDialogueController.playSharedId((LivingEntity)speaker, id, "time_skip:" + key, 900L, (Entity)player);
    }

    private static void processDifficultyChange(ServerLevel level) {
        Difficulty difficulty;
        String key = level.dimension().identifier().toString();
        Difficulty previous = LAST_DIFFICULTY.put(key, difficulty = level.getDifficulty());
        if (previous == null || previous == difficulty || level.players().isEmpty()) {
            return;
        }
        ServerPlayer player = (ServerPlayer)level.players().getFirst();
        Villager speaker = ContextualDialogueController.nearbyVillagers(level, player.position(), 32.0).stream().filter(villager -> !villager.isBaby() && !villager.isSleeping()).min(Comparator.comparingDouble(villager -> villager.distanceToSqr((Entity)player))).orElse(null);
        if (speaker == null) {
            return;
        }
        String id = difficulty == Difficulty.HARD ? "arzojk" : (difficulty == Difficulty.PEACEFUL ? "xuyypm" : "ibcrvx");
        ContextualDialogueController.playSharedId((LivingEntity)speaker, id, "difficulty:" + key + ":" + difficulty.name(), 900L, (Entity)player);
    }

    private static void tick(MinecraftServer server) {
        if (!server.tickRateManager().runsNormally()) {
            ContextualDialogueController.stopActiveDialogue(server);
            return;
        }
        ++ticks;
        ContextualDialogueController.processUnreachableVillagers(server);
        if (!VillagerNewsSettings.dialogueEnabled()) {
            ContextualDialogueController.stopActiveDialogue(server);
            ACTIVE_PLAYER_ENCOUNTERS.clear();
            return;
        }
        ACTIVE_SOUNDS.entrySet().removeIf(entry -> ((ActiveSound)entry.getValue()).endTick <= ticks);
        ContextualDialogueController.maintainSpeechTargets(server);
        ContextualDialogueController.processPendingSleep();
        ContextualDialogueController.processPendingConditionRelief();
        ContextualDialogueController.processPendingWake();
        ContextualDialogueController.processPendingSpeech();
        ContextualDialogueController.processPendingBells();
        ContextualDialogueController.processTradeSessions(server);
        if (ticks % 10L != 0L) {
            return;
        }
        for (ServerLevel level : server.getAllLevels()) {
            ContextualDialogueController.processTimeChange(level);
            ContextualDialogueController.processDifficultyChange(level);
            for (ServerPlayer player : level.players()) {
                if (player.gameMode().getName().equals("spectator")) {
                    ACTIVE_PLAYER_ENCOUNTERS.remove(player.getUUID());
                    PlayerObservation observation = PLAYER_OBSERVATIONS.computeIfAbsent(player.getUUID(), ignored -> new PlayerObservation());
                    observation.lastPosition = player.position();
                    observation.lastGameMode = "spectator";
                    observation.lastPlayerContext = null;
                    observation.stillTicks = 0;
                    observation.stareTicks = 0;
                    continue;
                }
                ContextualDialogueController.processPlayer(level, player);
                ContextualDialogueController.processWanderingTrader(level, player);
                ContextualDialogueController.processWooly(level, player);
                if (ticks % 20L == 0L) {
                    ContextualDialogueController.processAutonomousPerception(level, player);
                }
            }
            if (ticks % 100L != 0L) continue;
            ContextualDialogueController.processConversations(level);
        }
        if (ticks % 1200L == 0L) {
            COOLDOWNS.entrySet().removeIf(entry -> (Long)entry.getValue() + 12000L < ticks);
            BUSY_UNTIL.entrySet().removeIf(entry -> (Long)entry.getValue() < ticks);
        }
    }

    private static void clearState() {
        COOLDOWNS.clear();
        BUSY_UNTIL.clear();
        ACTIVE_SOUNDS.clear();
        PLAYER_OBSERVATIONS.clear();
        PLAYER_DEATHS.clear();
        LAST_SLEEPING.clear();
        LAST_TRADER_INVISIBLE.clear();
        VILLAGER_STATES.clear();
        CONDITION_HISTORY.clear();
        CONDITION_CURSORS.clear();
        PENDING_CONDITION_RELIEF.clear();
        VILLAGER_INVENTORIES.clear();
        SPEECH_TARGETS.clear();
        SHARED_RECENT_VARIANTS.clear();
        NO_WORKSTATION_SINCE.clear();
        LAST_DANGER.clear();
        NO_BELL_SINCE.clear();
        ACTIVE_TRADES.clear();
        UNREACHABLE_STATES.clear();
        ACTIVE_PLAYER_ENCOUNTERS.clear();
        PENDING_SLEEP.clear();
        PENDING_WAKE.clear();
        PENDING_BELLS.clear();
        PENDING_BELL_REACTIONS.clear();
        WAKE_SOURCES.clear();
        SLEEP_BYPASS.clear();
        INTERRUPTED_SLEEP.clear();
        PENDING_SPEECH.clear();
        LAST_LEVEL_TIME.clear();
        LAST_DIFFICULTY.clear();
        PAIR_TICKS.clear();
        ticks = 0L;
    }

    private static void processPendingWake() {
        PENDING_WAKE.entrySet().removeIf(entry -> {
            Villager villager;
            PendingWake pending = (PendingWake)entry.getValue();
            if (pending.dueTick > ticks) {
                return false;
            }
            Entity entity = pending.level.getEntity(pending.villagerId);
            if (!(entity instanceof Villager) || !(villager = (Villager)entity).isAlive() || villager.isSleeping()) {
                return true;
            }
            if (ContextualDialogueController.isBusy((LivingEntity)villager)) {
                return false;
            }
            Entity target = pending.targetId == null ? null : pending.level.getEntity(pending.targetId);
            return ContextualDialogueController.playSharedId((LivingEntity)villager, pending.dialogueId, "wake:" + String.valueOf(pending.villagerId) + ":" + pending.dialogueId, 1L, target);
        });
    }

    private static void queueBell(ServerLevel level, Vec3 position) {
        boolean queued = PENDING_BELLS.stream().anyMatch(pending -> pending.level == level && pending.position.distanceToSqr(position) < 0.25 && pending.dueTick > ticks);
        if (!queued) {
            PENDING_BELLS.add(new PendingBell(level, position, ticks + 15L));
        }
    }

    private static void processPendingBells() {
        PENDING_BELLS.removeIf(pending -> {
            if (pending.dueTick > ticks) {
                return false;
            }
            for (Villager villager : ContextualDialogueController.nearbyVillagers(pending.level, pending.position, 50.0)) {
                if (villager.isSleeping() || ContextualDialogueController.cast(villager) != CastProfile.VILLAGER) continue;
                long dueTick = ticks + (long)ThreadLocalRandom.current().nextInt(5);
                PENDING_BELL_REACTIONS.add(new PendingBellReaction(pending.level, villager.getUUID(), pending.position, dueTick, dueTick + 10L));
            }
            return true;
        });
        PENDING_BELL_REACTIONS.removeIf(pending -> {
            Villager villager;
            if (pending.dueTick > ticks) {
                return false;
            }
            Entity entity = pending.level.getEntity(pending.villagerId);
            if (!(entity instanceof Villager) || !(villager = (Villager)entity).isAlive() || villager.isSleeping() || ContextualDialogueController.cast(villager) != CastProfile.VILLAGER) {
                return true;
            }
            if (ContextualDialogueController.isBusy((LivingEntity)villager)) {
                return ticks >= pending.expireTick;
            }
            String id = villager.isBaby() ? "nxalcz" : "kljgyu";
            return ContextualDialogueController.playId((LivingEntity)villager, id, "bell:" + pending.dueTick + ":" + String.valueOf(villager.getUUID()), 1L, pending.position) || ticks >= pending.expireTick;
        });
    }

    private static boolean isVillagerSleepTime(Villager villager, ServerLevel level) {
        long time = Math.floorMod(level.getOverworldClockTime(), 24000L);
        return ContextualDialogueController.profession(villager).equals("nitwit") ? time >= 14000L || time < 2000L : time >= 12000L;
    }

    private static void processPendingSleep() {
        PENDING_SLEEP.entrySet().removeIf(entry -> {
            Villager villager;
            PendingSleep pending = (PendingSleep)entry.getValue();
            ActiveSound active = ACTIVE_SOUNDS.get(entry.getKey());
            if (active != null && active.endTick >= pending.dueTick) {
                pending.dueTick = active.endTick + 1L;
            }
            if (pending.dueTick > ticks) {
                return false;
            }
            Entity entity = pending.level.getEntity((UUID)entry.getKey());
            if (entity instanceof Villager && (villager = (Villager)entity).isAlive() && !villager.isSleeping() && villager.distanceToSqr(Vec3.atCenterOf((Vec3i)pending.bedPos)) <= 16.0 && BuiltInRegistries.BLOCK.getKey(pending.level.getBlockState(pending.bedPos).getBlock()).getPath().endsWith("_bed")) {
                DialogueCatalog.DialogueGroup group;
                if (!pending.bedtimeStarted && (group = DialogueCatalog.byId("ioxtmt")) != null && ContextualDialogueController.play((LivingEntity)villager, group, "bed:" + String.valueOf(villager.getUUID()), 3000L, null, Vec3.atCenterOf((Vec3i)pending.bedPos))) {
                    ActiveSound bedtime = ACTIVE_SOUNDS.get(villager.getUUID());
                    pending.bedtimeStarted = true;
                    pending.dueTick = bedtime == null ? ticks + group.durationTicks() : bedtime.endTick + 1L;
                    return false;
                }
                SLEEP_BYPASS.add(villager.getUUID());
                try {
                    villager.startSleeping(pending.bedPos);
                }
                finally {
                    SLEEP_BYPASS.remove(villager.getUUID());
                }
            }
            return true;
        });
    }

    public static boolean delayVillagerSleep(Villager villager, BlockPos bedPos) {
        ServerLevel level;
        UUID id;
        block8: {
            block7: {
                id = villager.getUUID();
                if (SLEEP_BYPASS.remove(id)) {
                    return false;
                }
                Level level2 = villager.level();
                if (!(level2 instanceof ServerLevel)) break block7;
                level = (ServerLevel)level2;
                if (!villager.isSleeping()) break block8;
            }
            return false;
        }
        if (PENDING_SLEEP.containsKey(id)) {
            return true;
        }
        ActiveSound active = ACTIVE_SOUNDS.get(id);
        if (active != null && active.endTick > ticks) {
            PENDING_SLEEP.put(id, new PendingSleep(level, bedPos.immutable(), active.endTick + 1L, false));
            LAST_SLEEPING.put(id, false);
            return true;
        }
        DialogueCatalog.DialogueGroup group = DialogueCatalog.byId("ioxtmt");
        if (group == null || !ContextualDialogueController.play((LivingEntity)villager, group, "bed:" + String.valueOf(id), 3000L, null, Vec3.atCenterOf((Vec3i)bedPos))) {
            return false;
        }
        ActiveSound bedtime = ACTIVE_SOUNDS.get(id);
        long dueTick = bedtime == null ? ticks + group.durationTicks() : bedtime.endTick + 1L;
        PENDING_SLEEP.put(id, new PendingSleep(level, bedPos.immutable(), dueTick, true));
        LAST_SLEEPING.put(id, false);
        return true;
    }

    private static void stopActiveDialogue(MinecraftServer server) {
        for (UUID id : List.copyOf(ACTIVE_SOUNDS.keySet())) {
            LivingEntity speaker = null;
            for (ServerLevel level : server.getAllLevels()) {
                LivingEntity living;
                Entity entity = level.getEntity(id);
                if (!(entity instanceof LivingEntity)) continue;
                speaker = living = (LivingEntity)entity;
                break;
            }
            if (speaker != null) {
                ContextualDialogueController.interrupt(speaker);
                continue;
            }
            ACTIVE_SOUNDS.remove(id);
            BUSY_UNTIL.remove(id);
            SPEECH_TARGETS.remove(id);
        }
    }

    private static void processWanderingTrader(ServerLevel level, ServerPlayer player) {
        boolean wasInvisible;
        AABB area = AABB.ofSize((Vec3)player.position(), (double)32.0, (double)16.0, (double)32.0);
        WanderingTrader trader = level.getEntitiesOfClass(WanderingTrader.class, area, Entity::isAlive).stream().filter(candidate -> candidate.hasLineOfSight((Entity)player)).min(Comparator.comparingDouble(candidate -> candidate.distanceToSqr((Entity)player))).orElse(null);
        if (trader == null) {
            return;
        }
        String pair = String.valueOf(player.getUUID()) + ":" + String.valueOf(trader.getUUID());
        if (ContextualDialogueController.playId((LivingEntity)trader, "hxlyuc", "approach:" + pair, 3000L, (Entity)player)) {
            return;
        }
        boolean invisible = trader.hasEffect(MobEffects.INVISIBILITY);
        boolean bl = wasInvisible = LAST_TRADER_INVISIBLE.put(trader.getUUID(), invisible) == Boolean.TRUE;
        if (invisible) {
            long llamas = level.getEntitiesOfClass(LivingEntity.class, AABB.ofSize((Vec3)trader.position(), 24.0, 12.0, 24.0), Entity::isAlive).stream().filter(entity -> BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).getPath().equals("trader_llama")).count();
            String id = wasInvisible ? "dbzjqi" : (llamas >= 2L ? "myajyt" : (llamas == 1L ? "jkeahu" : "vggdrt"));
            if (ContextualDialogueController.playId((LivingEntity)trader, id, "invisible:" + trader.getUUID() + ":" + id, 400L, (Entity)player)) {
                return;
            }
        }
        if (level.isRainingAt(trader.blockPosition()) && ContextualDialogueController.playId((LivingEntity)trader, "kxoqky", "rain:" + String.valueOf(trader.getUUID()), 3000L)) {
            return;
        }
        if (ticks % 100L == 0L && trader.getDeltaMovement().horizontalDistanceSqr() > 4.0E-4) {
            ContextualDialogueController.playId((LivingEntity)trader, "stqafd", "idle:" + String.valueOf(trader.getUUID()), 3000L);
        }
    }

    private static void processWooly(ServerLevel level, ServerPlayer player) {
        AABB area = AABB.ofSize((Vec3)player.position(), (double)32.0, (double)16.0, (double)32.0);
        Sheep wooly = level.getEntitiesOfClass(Sheep.class, area, sheep -> sheep.isAlive() && ContextualDialogueController.isWooly(sheep) && !sheep.entityTags().contains(DIALOGUE_TEST_TAG)).stream().filter(candidate -> candidate.hasLineOfSight((Entity)player)).min(Comparator.comparingDouble(candidate -> candidate.distanceToSqr((Entity)player))).orElse(null);
        if (wooly == null) {
            return;
        }
        String pair = String.valueOf(player.getUUID()) + ":" + String.valueOf(wooly.getUUID());
        if (ContextualDialogueController.playId((LivingEntity)wooly, "uvtocs", "approach:" + pair, 3000L, (Entity)player)) {
            return;
        }
        if (ticks % 100L == 0L && wooly.getDeltaMovement().horizontalDistanceSqr() > 4.0E-4) {
            ContextualDialogueController.playId((LivingEntity)wooly, "vmohcm", "idle:" + String.valueOf(wooly.getUUID()), 3000L);
        }
    }

    private static void processUnreachableVillagers(MinecraftServer server) {
        HashSet<UUID> processed = new HashSet<UUID>();
        for (ServerLevel level : server.getAllLevels()) {
            for (ServerPlayer player : level.players()) {
                AABB area = player.getBoundingBox().inflate(16.0);
                for (Villager villager : level.getEntitiesOfClass(Villager.class, area, candidate -> candidate.isAlive() && ContextualDialogueController.cast(candidate) == CastProfile.UNREACHABLE)) {
                    ServerPlayer nearest;
                    if (processed.contains(villager.getUUID()) || (nearest = ContextualDialogueController.nearestPlayer(level, villager.position(), 16.0)) == null) continue;
                    processed.add(villager.getUUID());
                    ContextualDialogueController.updateUnreachable(villager, nearest);
                }
            }
        }
        UNREACHABLE_STATES.entrySet().removeIf(entry -> {
            if (processed.contains(entry.getKey())) {
                return false;
            }
            for (ServerLevel level : server.getAllLevels()) {
                Villager villager;
                Entity entity = level.getEntity((UUID)entry.getKey());
                if (!(entity instanceof Villager) || !(villager = (Villager)entity).isAlive() || ContextualDialogueController.cast(villager) != CastProfile.UNREACHABLE) continue;
                villager.getNavigation().stop();
                ((UnreachableState)entry.getValue()).stopFleeing(ticks);
                return false;
            }
            return true;
        });
    }

    private static void updateUnreachable(Villager villager, ServerPlayer player) {
        UnreachableState state = UNREACHABLE_STATES.computeIfAbsent(villager.getUUID(), ignored -> new UnreachableState(ticks));
        state.updateFleeing(ticks);
        double distance = villager.distanceTo((Entity)player);
        if (distance <= 4.0) {
            ContextualDialogueController.teleportUnreachable(villager, (Player)player);
        }
        villager.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        if (ticks >= state.nextPathTick || villager.getNavigation().isDone()) {
            Vec3 destination = DefaultRandomPos.getPosAway((PathfinderMob)villager, (int)16, (int)7, (Vec3)player.position());
            if (destination == null || destination.distanceToSqr(player.position()) <= villager.distanceToSqr((Entity)player)) {
                Vec3 away = villager.position().subtract(player.position());
                if (away.horizontalDistanceSqr() < 1.0E-4) {
                    double angle = villager.getRandom().nextDouble() * Math.PI * 2.0;
                    away = new Vec3(Math.cos(angle), 0.0, Math.sin(angle));
                } else {
                    away = new Vec3(away.x, 0.0, away.z).normalize();
                }
                destination = villager.position().add(away.scale(16.0));
            }
            villager.getNavigation().moveTo(destination.x, destination.y, destination.z, 0.9);
            state.nextPathTick = ticks + 10L;
        }
        if (distance <= 12.0) {
            ContextualDialogueController.faceMob((Mob)villager, player.getEyePosition());
        }
        if (state.canTaunt(ticks) && ContextualDialogueController.playId((LivingEntity)villager, "eltxge", "unreachable_taunt:" + String.valueOf(villager.getUUID()), 1L, (Entity)player)) {
            state.taunted(ticks);
        }
    }

    private static void repelPlayer(Villager villager, Player player) {
        if (villager.distanceToSqr((Entity)player) <= 16.0) {
            ContextualDialogueController.teleportUnreachable(villager, player);
        }
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            if ((double)villager.distanceTo((Entity)player) <= 16.0) {
                ContextualDialogueController.updateUnreachable(villager, serverPlayer);
            }
        }
    }

    private static boolean teleportUnreachable(Villager villager, Player player) {
        Vec3 away = villager.position().subtract(player.position());
        double baseAngle = away.horizontalDistanceSqr() < 1.0E-4 ? villager.getRandom().nextDouble() * Math.PI * 2.0 : Math.atan2(away.z, away.x);
        for (int attempt = 0; attempt < 24; ++attempt) {
            double z;
            double y;
            double angle = baseAngle + (villager.getRandom().nextDouble() - 0.5) * Math.PI * 0.75;
            double distance = 14.0 + villager.getRandom().nextDouble() * 4.0;
            double x = villager.getX() + Math.cos(angle) * distance;
            if (!villager.randomTeleport(x, y = villager.getY() + (double)villager.getRandom().nextInt(11) - 5.0, z = villager.getZ() + Math.sin(angle) * distance, true)) continue;
            villager.getNavigation().stop();
            return true;
        }
        return false;
    }


    private static void processAutonomousPerception(ServerLevel level, ServerPlayer player) {
        List<Villager> villagers = ContextualDialogueController.nearbyVillagers(level, player.position(), 16.0);
        for (Villager villager : villagers) {
            if (!villager.isAlive() || villager.isSleeping() || ContextualDialogueController.isBusy(villager)) {
                continue;
            }
            ContextualDialogueController.processConditionDialogues(villager);
            
            // 1. Environmental perception (Magma, Ice, Snow, Shallow Water, Fire, Campfire, TNT)
            String env = ContextualDialogueController.environmentContext(level, villager);
            if (env != null && (env.startsWith("Stand on") || env.startsWith("Stand in") || env.startsWith("Stand Near") || env.startsWith("See a Campfire") || env.startsWith("See TNT"))) {
                if (ContextualDialogueController.playTitle(villager, env, "environment:" + villager.getUUID() + ":" + env, 200L)) {
                    continue;
                }
            }
            
            // 2. XP Orbs floating nearby (>= 4 orbs within 12 blocks)
            List<net.minecraft.world.entity.ExperienceOrb> orbs = level.getEntitiesOfClass(net.minecraft.world.entity.ExperienceOrb.class, AABB.ofSize(villager.position(), 16.0, 8.0, 16.0), Entity::isAlive);
            if (orbs.size() >= 4 && ContextualDialogueController.playSharedId(villager, "cvltyw", "xp_orbs:" + villager.getUUID(), 400L, orbs.getFirst())) {
                continue;
            }
            
            // 3. Pile of dropped items (>= 5 items within 5 blocks)
            List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(villager.position(), 10.0, 6.0, 10.0), Entity::isAlive);
            if (items.size() >= 5 && ContextualDialogueController.playSharedId(villager, "zywcju", "item_pile:" + villager.getUUID(), 400L, items.getFirst())) {
                continue;
            }
            
            // 4. Panic reaction when detecting danger or being hurt
            if ((villager.getBrain().hasMemoryValue(MemoryModuleType.DANGER_DETECTED_RECENTLY) || villager.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY)) && ContextualDialogueController.playId((LivingEntity)villager, "uzdxum", "panic:" + villager.getUUID(), 200L)) {
                continue;
            }
            
            // 5. Baby seeing Iron Golem
            if (villager.isBaby()) {
                List<LivingEntity> golems = level.getEntitiesOfClass(LivingEntity.class, AABB.ofSize(villager.position(), 12.0, 6.0, 12.0), e -> BuiltInRegistries.ENTITY_TYPE.getKey(e.getType()).getPath().equals("iron_golem"));
                if (!golems.isEmpty() && villager.hasLineOfSight(golems.getFirst()) && ContextualDialogueController.playSharedId(villager, "mqnapy", "baby_golem:" + villager.getUUID(), 400L, golems.getFirst())) {
                    continue;
                }
            }
            
            // 6. Adult seeing baby villager
            if (!villager.isBaby()) {
                List<Villager> babies = villagers.stream().filter(AgeableMob::isBaby).filter(b -> villager.hasLineOfSight(b)).toList();
                if (!babies.isEmpty() && ContextualDialogueController.playSharedId(villager, "pbmrxx", "see_baby:" + villager.getUUID(), 400L, babies.getFirst())) {
                    continue;
                }
            }
            
            // 7. General entity observation
            if (ticks % 40L == 0L) {
                ContextualDialogueController.playNearbyEntityContext(level, villager);
            }
        }
    }

    private static void processPlayer(ServerLevel level, ServerPlayer player) {
        String time;
        String approach;
        PlayerObservation observation = PLAYER_OBSERVATIONS.computeIfAbsent(player.getUUID(), ignored -> new PlayerObservation());
        String gameMode = player.gameMode().getName();
        boolean changedGameMode = observation.lastGameMode != null && !observation.lastGameMode.equals(gameMode);
        observation.lastGameMode = gameMode;
        Vec3 movement = player.position().subtract(observation.lastPosition);
        observation.stillTicks = movement.horizontalDistanceSqr() < 4.0E-4 && Math.abs(movement.y) < 0.01 ? (observation.stillTicks += 10) : 0;
        observation.lastPosition = player.position();
        BlockPos ground = player.blockPosition().below();
        String groundBlock = BuiltInRegistries.BLOCK.getKey(level.getBlockState(ground).getBlock()).getPath();
        if (ground.equals((Object)observation.lastGroundPos) && observation.lastGroundBlock.equals("farmland") && groundBlock.equals("dirt")) {
            ContextualDialogueController.playObserved(level, (Player)player, player.position(), "Trample Crops", 900L);
        }
        observation.lastGroundPos = ground;
        observation.lastGroundBlock = groundBlock;
        List<Villager> nearby = ContextualDialogueController.nearbyVillagers(level, player.position(), 16.0).stream().filter(villager -> !villager.isSleeping()).toList();
        for (Villager villager2 : nearby) {
            ContextualDialogueController.processConditionDialogues(villager2);
        }
        Villager adult = nearby.stream().filter(villager -> !villager.isBaby()).filter(villager -> ContextualDialogueController.cast(villager) != CastProfile.UNREACHABLE).filter(villager -> villager.hasLineOfSight((Entity)player)).min(Comparator.comparingDouble(villager -> villager.distanceToSqr((Entity)player))).orElse(null);
        if (adult == null) {
            Villager baby = nearby.stream().filter(AgeableMob::isBaby).filter(villager -> ContextualDialogueController.cast(villager) != CastProfile.UNREACHABLE).filter(villager -> villager.hasLineOfSight((Entity)player)).min(Comparator.comparingDouble(villager -> villager.distanceToSqr((Entity)player))).orElse(null);
            if (baby != null) {
                String id;
                boolean firstNotice = ACTIVE_PLAYER_ENCOUNTERS.add(player.getUUID());
                if (ticks % 40L == 0L && ContextualDialogueController.playNearbyEntityContext(level, baby)) {
                    return;
                }
                id = player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE) ? "fzyrfm" : (ContextualDialogueController.isNegativeReputation(baby, player) ? "jfuftm" : "wtuguc");
                if (firstNotice) {
                    ContextualDialogueController.playId((LivingEntity)baby, id, "player_greeting:" + player.getUUID(), 400L, (Entity)player);
                }
            } else {
                ACTIVE_PLAYER_ENCOUNTERS.remove(player.getUUID());
            }
            return;
        }
        boolean firstNotice = ACTIVE_PLAYER_ENCOUNTERS.add(player.getUUID());
        String pair = String.valueOf(player.getUUID()) + ":" + String.valueOf(adult.getUUID());
        if (ContextualDialogueController.playCosmeticObservation(player, adult)) {
            return;
        }
        if (player.getBoundingBox().inflate(0.15).intersects(adult.getBoundingBox()) && movement.horizontalDistanceSqr() > 0.002) {
            String id;
            String string = id = adult.getVehicle() != null && BuiltInRegistries.ENTITY_TYPE.getKey(adult.getVehicle().getType()).getPath().contains("boat") ? "zvbnea" : "ajexrq";
            if (ContextualDialogueController.playId((LivingEntity)adult, id, "nudge:" + String.valueOf(adult.getUUID()), 900L, (Entity)player)) {
                return;
            }
        }
        if (changedGameMode && ContextualDialogueController.playSharedId((LivingEntity)adult, gameMode.equals("creative") ? "ohtblt" : "fhhqxg", "gamemode:" + String.valueOf(player.getUUID()) + ":" + gameMode, 900L, (Entity)player)) {
            return;
        }
        CastProfile adultProfile = ContextualDialogueController.cast(adult);
        if (firstNotice && adultProfile != CastProfile.VILLAGER && player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE) && ContextualDialogueController.playSharedId((LivingEntity)adult, "gnetsk", "player_greeting:" + player.getUUID(), 400L, (Entity)player)) {
            return;
        }
        String string = approach = adultProfile == CastProfile.VILLAGER ? ContextualDialogueController.reputationApproach(adult, player) : adultProfile.approach;
        if (firstNotice && ContextualDialogueController.playId((LivingEntity)adult, approach, "player_greeting:" + player.getUUID(), 400L, (Entity)player)) {
            return;
        }
        Vec3 toVillager = adult.getEyePosition().subtract(player.getEyePosition()).normalize();
        double lookDot = player.getLookAngle().dot(toVillager);
        observation.stareTicks = lookDot > 0.985 ? (observation.stareTicks += 10) : 0;
        if (observation.stareTicks >= 60 && ContextualDialogueController.playSharedTitle((LivingEntity)adult, "Stare at a Villager", "stare:" + pair, 400L, (Entity)player)) {
            observation.stareTicks = 0;
            return;
        }
        if (ticks % 400L == 0L && observation.stillTicks >= 400 && ContextualDialogueController.playSharedTitle((LivingEntity)adult, "Stand Completely Still", "still:" + player.getUUID(), 400L, (Entity)player)) {
            observation.stillTicks = 0;
            return;
        }
        // Check player standing/jumping on bed
        BlockState bedBlockCurrent = level.getBlockState(player.blockPosition());
        BlockState bedBlockBelow = level.getBlockState(player.blockPosition().below());
        if (BuiltInRegistries.BLOCK.getKey(bedBlockCurrent.getBlock()).getPath().endsWith("_bed") || BuiltInRegistries.BLOCK.getKey(bedBlockBelow.getBlock()).getPath().endsWith("_bed")) {
            if (ContextualDialogueController.playObserved(level, (Player)player, player.position(), "Stand on a Villager's Bed", 400L)) {
                return;
            }
        }
        
        String playerContext = ContextualDialogueController.playerContext(player, adult);
        if (playerContext != null && ContextualDialogueController.playSharedTitle((LivingEntity)adult, playerContext, "player_context:" + adult.getUUID() + ":" + playerContext, 400L, (Entity)player)) {
            return;
        }
        long nearbyPlayers = level.players().stream().filter(other -> other.distanceToSqr((Entity)adult) <= 64.0).count();
        if (nearbyPlayers >= 2L && ContextualDialogueController.playSharedId((LivingEntity)adult, "cstyvg", "player_crowd:" + adult.getUUID(), 400L, (Entity)player)) {
            return;
        }
        String environment = ContextualDialogueController.environmentContext(level, adult);
        if (environment != null && ContextualDialogueController.playTitle((LivingEntity)adult, environment, "environment:" + adult.getUUID() + ":" + environment, 400L)) {
            return;
        }
        if (ticks % 40L == 0L && ContextualDialogueController.playNearbyEntityContext(level, adult)) {
            return;
        }
        if (ticks % 200L == 0L && ContextualDialogueController.playTitle((LivingEntity)adult, time = ContextualDialogueController.timeContext(level, adult), "time:" + adult.getUUID() + ":" + time, 400L)) {
            return;
        }
        if (ticks % 100L == 0L && adult.getDeltaMovement().horizontalDistanceSqr() > 4.0E-4) {
            CastProfile profile = ContextualDialogueController.cast(adult);
            if (profile != CastProfile.VILLAGER) {
                ContextualDialogueController.playId((LivingEntity)adult, profile.idle, "idle:" + adult.getUUID(), 400L);
            } else {
                String ambient = ContextualDialogueController.ambientDialogue(adult);
                ContextualDialogueController.playId((LivingEntity)adult, ambient, "idle:" + adult.getUUID() + ":" + ambient, 400L);
            }
        }
    }

    private static String environmentContext(ServerLevel level, Villager villager) {
        Entity vehicle = villager.getVehicle();
        if (vehicle != null) {
            String vehiclePath = BuiltInRegistries.ENTITY_TYPE.getKey(vehicle.getType()).getPath();
            if (vehiclePath.contains("minecart")) {
                return vehicle.getDeltaMovement().horizontalDistanceSqr() > 0.001 ? "Ride in a Moving Minecart" : "Sit in a Minecart";
            }
            if (vehiclePath.contains("boat")) {
                if (ticks / 3000L % 3L == 0L) {
                    return "Sit in a Boat";
                }
                return vehicle.isInWater() ? "Boat on Water" : "Boat on Land";
            }
        }
        if (level.dimension() == Level.NETHER) {
            return "Wander in the Nether";
        }
        if (level.dimension() == Level.END) {
            return "Wander in the End";
        }
        if (level.dimension() != Level.OVERWORLD) {
            return "Wander in Another Dimension";
        }
        if (villager.isInWater() && villager.getVehicle() == null) {
            return "Stand in Shallow Water";
        }
        BlockState below = level.getBlockState(villager.blockPosition().below());
        BlockState currentBlock = level.getBlockState(villager.blockPosition());
        if (below.is(Blocks.MAGMA_BLOCK)) {
            return "Stand on Magma";
        }
        if (below.is(Blocks.ICE) || below.is(Blocks.PACKED_ICE) || below.is(Blocks.BLUE_ICE) || below.is(Blocks.FROSTED_ICE)) {
            return "Stand on Ice";
        }
        if (below.is(Blocks.SNOW) || below.is(Blocks.SNOW_BLOCK) || below.is(Blocks.POWDER_SNOW) || currentBlock.is(Blocks.SNOW) || currentBlock.is(Blocks.POWDER_SNOW)) {
            return "Stand on Snow";
        }
        for (int x = -4; x <= 4; ++x) {
            for (int y = -2; y <= 2; ++y) {
                for (int z = -4; z <= 4; ++z) {
                    BlockState bs = level.getBlockState(villager.blockPosition().offset(x, y, z));
                    String block = BuiltInRegistries.BLOCK.getKey(bs.getBlock()).getPath();
                    if (block.contains("campfire")) {
                        return "See a Campfire";
                    }
                    if (block.equals("fire") || block.equals("soul_fire")) {
                        return "Stand Near Fire";
                    }
                    if (block.equals("tnt")) {
                        return "See TNT";
                    }
                    if (block.equals("bookshelf") && villager.getDeltaMovement().horizontalDistanceSqr() < 4.0E-4) {
                        return "Inspect Bookshelves";
                    }
                }
            }
        }
        if (level.isRainingAt(villager.blockPosition())) {
            return "Caught in the Rain";
        }
        String biome = level.getBiome(villager.blockPosition()).unwrapKey().map(key -> key.identifier().getPath()).orElse("");
        if (biome.contains("desert") || biome.contains("badlands") || biome.contains("savanna")) {
            return "Wander Somewhere Hot";
        }
        if (biome.contains("snow") || biome.contains("frozen") || biome.contains("ice") || biome.contains("cold")) {
            return "Wander Somewhere Cold";
        }
        if (villager.getY() < (double)(level.getSeaLevel() - 30)) {
            return "Wander Deep Underground";
        }
        if (villager.getY() > (double)(level.getSeaLevel() + 75)) {
            return "Wander High Above the Ground";
        }
        return null;
    }

    private static String timeContext(ServerLevel level, Villager villager) {
        float sunAngle = ((Float)level.environmentAttributes().getValue(EnvironmentAttributes.SUN_ANGLE, villager.blockPosition())).floatValue();
        if (sunAngle < 0.125f || sunAngle >= 0.875f) {
            return "Morning";
        }
        if (sunAngle < 0.45f) {
            return "Afternoon";
        }
        if (sunAngle < 0.625f) {
            return "Evening";
        }
        return "Night";
    }

    private static String ambientDialogue(Villager villager) {
        String profession = ContextualDialogueController.profession(villager);
        if (profession.equals("nitwit")) {
            return "uookqp";
        }
        if (profession.equals("none")) {
            return "gbxzxv";
        }
        LocalDate date = LocalDate.now();
        ArrayList<String> choices = new ArrayList<String>();
        if (date.getMonthValue() == 10) {
            choices.add("mltyge");
        }
        if (date.getMonthValue() == 12) {
            choices.add("tkkegl");
        }
        if (date.getMonthValue() == 1 && date.getDayOfMonth() == 1) {
            choices.add("uyqiwv");
        }
        if (date.getMonthValue() == 2 && date.getDayOfMonth() == 14) {
            choices.add("fabiyx");
        }
        if (date.getMonthValue() == 4 && date.getDayOfMonth() == 1) {
            choices.add("obitls");
        }
        if (date.getMonthValue() == 5 && date.getDayOfMonth() == 17) {
            choices.add("iriuqa");
        }
        if (date.getMonthValue() == 10 && date.getDayOfMonth() == 31) {
            choices.add("adhxce");
        }
        if (date.getMonthValue() == 12 && date.getDayOfMonth() == 24) {
            choices.add("zoqxvy");
        }
        if (date.getMonthValue() == 12 && date.getDayOfMonth() == 25) {
            choices.add("rclyrl");
        }
        if (date.getMonthValue() == 12 && date.getDayOfMonth() == 31) {
            choices.add("xljknt");
        }
        if (date.getDayOfMonth() == 13 && date.getDayOfWeek() == DayOfWeek.FRIDAY) {
            choices.add("qfcwvz");
        }
        if (LocalDateTime.now().getMinute() == 0) {
            choices.add("jqgkhy");
        }
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            choices.add("bkyidl");
            choices.add("ckngck");
        }
        choices.add(switch (date.getDayOfWeek()) {
            default -> throw new MatchException(null, null);
            case DayOfWeek.MONDAY -> {
                if (ticks / 3000L % 2L == 0L) {
                    yield "gkvlqc";
                }
                yield "jpucos";
            }
            case DayOfWeek.TUESDAY -> {
                if (ticks / 3000L % 2L == 0L) {
                    yield "dkpihl";
                }
                yield "lgeeem";
            }
            case DayOfWeek.WEDNESDAY -> {
                if (ticks / 3000L % 2L == 0L) {
                    yield "gwakiz";
                }
                yield "qiqiez";
            }
            case DayOfWeek.THURSDAY -> {
                if (ticks / 3000L % 2L == 0L) {
                    yield "zglkgp";
                }
                yield "caiyte";
            }
            case DayOfWeek.FRIDAY -> {
                if (ticks / 3000L % 2L == 0L) {
                    yield "ypyumu";
                }
                yield "cxtvsx";
            }
            case DayOfWeek.SATURDAY -> {
                if (ticks / 3000L % 2L == 0L) {
                    yield "ildosa";
                }
                yield "lfhnxz";
            }
            case DayOfWeek.SUNDAY -> ticks / 3000L % 2L == 0L ? "uzvatl" : "zckxrc";
        });
        if (choices.size() == 1) {
            choices.add("lvigit");
        }
        return (String)choices.get(Math.floorMod((int)(ticks / 3000L), choices.size()));
    }

    private static String playerContext(ServerPlayer player, Villager villager) {
        if (player.isFallFlying()) {
            return "Glide with Elytra";
        }
        if (player.gameMode().getName().equals("creative") && player.getAbilities().flying) {
            return "Fly in Creative Mode";
        }
        if (player.isShiftKeyDown() && player.getDeltaMovement().horizontalDistanceSqr() > 1.0E-4) {
            return "Crouch-Walk";
        }
        if (player.getHealth() <= player.getMaxHealth() * 0.3f) {
            return "Low Health";
        }
        if (player.hasEffect(MobEffects.INVISIBILITY)) {
            return "Invisibility";
        }
        if (player.hasEffect(MobEffects.DARKNESS)) {
            return "Darkness";
        }
        if (player.hasEffect(MobEffects.NIGHT_VISION)) {
            return "Night Vision";
        }
        if (player.hasEffect(MobEffects.WATER_BREATHING)) {
            return "Water Breathing";
        }
        if (player.hasEffect(MobEffects.SPEED)) {
            return "Swiftness";
        }
        if (player.hasEffect(MobEffects.SLOWNESS)) {
            return "Slowness";
        }
        if (player.hasEffect(MobEffects.STRENGTH)) {
            return "Strength";
        }
        if (player.hasEffect(MobEffects.WEAKNESS)) {
            return "Weakness";
        }
        if (player.hasEffect(MobEffects.HUNGER)) {
            return "Hunger";
        }
        if (player.hasEffect(MobEffects.NAUSEA)) {
            return "Nausea";
        }
        if (player.hasEffect(MobEffects.BAD_OMEN) || player.hasEffect(MobEffects.RAID_OMEN)) {
            return "Bad Omen";
        }
        if (player.hasEffect(MobEffects.OOZING)) {
            return "Oozing";
        }
        if (player.getActiveEffects().size() >= 2) {
            return "Multiple Status Effects";
        }
        if (BuiltInRegistries.BLOCK.getKey(player.level().getBlockState(player.blockPosition().below()).getBlock()).getPath().endsWith("_bed")) {
            return "Stand on a Villager's Bed";
        }
        ItemStack held = player.getMainHandItem();
        if (held.isDamageableItem() && (float)held.getDamageValue() >= (float)held.getMaxDamage() * 0.85f) {
            return "Hold a Nearly Broken Item";
        }
        int armor = 0;
        HashSet<String> armorMaterials = new HashSet<String>();
        for (EquipmentSlot slot : List.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)) {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.isEmpty()) continue;
            ++armor;
            String path = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
            int underscore = path.indexOf('_');
            armorMaterials.add(underscore > 0 ? path.substring(0, underscore) : path);
        }
        if (armor == 4 && armorMaterials.size() == 1 && armorMaterials.contains("iron")) {
            return "Wear Full Iron Armor";
        }
        if (armor == 4 && armorMaterials.size() > 1) {
            return "Wear Mixed Armor";
        }
        if (armor == 4 && (armorMaterials.contains("diamond") || armorMaterials.contains("netherite"))) {
            return "Wear High-Level Armor";
        }
        if (armor > 0) {
            return "Wear Armor";
        }
        return null;
    }

    private static String reputationApproach(Villager villager, ServerPlayer player) {
        if (player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
            return "gnetsk";
        }
        int reputation = villager.getPlayerReputation((Player)player);
        if (reputation < -225) {
            return BuiltInRegistries.ITEM.getKey(player.getMainHandItem().getItem()).getPath().endsWith("_sword") ? "stuirs" : "zstdjn";
        }
        if (reputation < -75) {
            return "tfzlsw";
        }
        if (reputation >= 75) {
            return "kcbenk";
        }
        if (reputation >= 25) {
            return "omgcte";
        }
        return "xfpjxq";
    }

    private static boolean isNegativeReputation(Villager villager, ServerPlayer player) {
        return villager.getPlayerReputation((Player)player) < -75;
    }

    private static boolean playCosmeticObservation(ServerPlayer player, Villager villager) {
        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack held = player.getMainHandItem();
        CastProfile profile = ContextualDialogueController.cast(villager);
        String id = null;
        if (head.getItem() == VillagerNewsItems.VILLAGER_NOSE) {
            id = "kejscw";
        } else if (head.getItem() == VillagerNewsItems.MAYOR_HAT && profile == CastProfile.MAYOR) {
            id = "cmkesu";
        } else if (head.getItem() == VillagerNewsItems.TESTIFICATE_MAN_HELMET && profile == CastProfile.TESTIFICATE_MAN) {
            id = "rooiup";
        } else if (head.getItem() == VillagerNewsItems.MOUSTACHE && profile == CastProfile.NUMBER_5) {
            id = "mjyhgw";
        } else if (held.getItem() == VillagerNewsItems.MICROPHONE && profile == CastProfile.NUMBER_9) {
            id = "adhvqz";
        }
        if (id == null) {
            return false;
        }
        String key = "player_cosmetic:" + String.valueOf(villager.getUUID()) + ":" + String.valueOf(player.getUUID()) + ":" + id;
        return id.equals("kejscw") ? ContextualDialogueController.playSharedId((LivingEntity)villager, id, key, 3000L, (Entity)player) : ContextualDialogueController.playId((LivingEntity)villager, id, key, 3000L, (Entity)player);
    }

    private static void processConversations(ServerLevel level) {
        HashSet<UUID> updatedVillagers = new HashSet<UUID>();
        for (ServerPlayer player : level.players()) {
            for (Villager villager2 : ContextualDialogueController.nearbyVillagers(level, player.position(), 24.0)) {
                if (ContextualDialogueController.cast(villager2) == CastProfile.UNREACHABLE || !updatedVillagers.add(villager2.getUUID())) continue;
                ContextualDialogueController.processVillagerState(villager2);
            }
        }
        HashSet<String> checkedPairs = new HashSet<String>();
        for (ServerPlayer player : level.players()) {
            List<Villager> villagers = ContextualDialogueController.nearbyVillagers(level, player.position(), 24.0).stream().filter(villager -> !villager.isBaby() && !villager.isSleeping()).filter(villager -> ContextualDialogueController.cast(villager) != CastProfile.UNREACHABLE).filter(villager -> ContextualDialogueController.activeConditionDialogues(villager).isEmpty()).toList();
            for (Villager subject : villagers) {
                if (ContextualDialogueController.data(subject).vnap$cosmetic() == 0) continue;
                Villager cosmeticWitness = villagers.stream()
                    .filter(other -> other != subject && other.hasLineOfSight((Entity)subject))
                    .min(Comparator.comparingDouble(other -> other.distanceToSqr((Entity)subject)))
                    .orElse(null);
                if (cosmeticWitness == null) continue;
                String id = ContextualDialogueController.cast(cosmeticWitness) == CastProfile.TESTIFICATE_MAN && ContextualDialogueController.data(subject).vnap$cosmetic() == 2 ? "pbbywc" : "anrhns";
                if (!ContextualDialogueController.playSharedId((LivingEntity)cosmeticWitness, id, "cosmetic_witness:" + cosmeticWitness.getUUID() + ":" + subject.getUUID() + ":" + id, 3000L, (Entity)subject)) continue;
                return;
            }
            if (villagers.size() >= 8 && ContextualDialogueController.playSharedId((LivingEntity)villagers.getFirst(), "kzemrz", "villager_crowd:" + String.valueOf(player.getUUID()), 3000L, (Entity)player)) {
                return;
            }
            Villager gatheringSpeaker = villagers.stream().filter(villager -> ContextualDialogueController.cast(villager) == CastProfile.VILLAGER).findFirst().orElse(null);
            Villager gatheringTarget = gatheringSpeaker == null ? null : ContextualDialogueController.nearestConversationPartner(gatheringSpeaker, villagers);
            if (gatheringTarget != null && villagers.size() >= 3 && ContextualDialogueController.playId((LivingEntity)gatheringSpeaker, "ebfifz", "gathering:" + String.valueOf(player.getUUID()), 3000L, (Entity)gatheringTarget)) {
                return;
            }
            for (Villager candidate : villagers) {
                boolean negativeGossip;
                int togetherTicks;
                Villager second;
                String pair;
                Villager partner = ContextualDialogueController.nearestConversationPartner(candidate, villagers);
                if (partner == null || !checkedPairs.add(pair = ContextualDialogueController.orderedPair(candidate.getUUID(), partner.getUUID()))) continue;
                Villager first = candidate.getUUID().compareTo(partner.getUUID()) <= 0 ? candidate : partner;
                Villager villager4 = second = first == candidate ? partner : candidate;
                if (ContextualDialogueController.isBusy((LivingEntity)first) || ContextualDialogueController.isBusy((LivingEntity)second) || (togetherTicks = PAIR_TICKS.merge(pair, 100, Integer::sum).intValue()) < 200) continue;
                boolean firstHasNose = ContextualDialogueController.data(first).vnap$hasNose();
                boolean secondHasNose = ContextualDialogueController.data(second).vnap$hasNose();
                if (!firstHasNose || !secondHasNose) {
                    boolean bothMissing = !firstHasNose && !secondHasNose;
                    Villager conversationSpeaker = !bothMissing && !firstHasNose ? second : first;
                    Villager conversationSubject = conversationSpeaker == first ? second : first;
                    List<List<String>> choices = bothMissing ? TWO_MISSING_NOSES_CONVERSATIONS : ONE_MISSING_NOSE_CONVERSATIONS;
                    List<String> sequence = choices.get(Math.floorMod(pair.hashCode() + (int)(ticks / 3000L), choices.size()));
                    if (ContextualDialogueController.playId((LivingEntity)conversationSpeaker, sequence.getFirst(), "nose_conversation:" + pair + ":" + sequence.getFirst(), 3000L, (Entity)conversationSubject)) {
                        ContextualDialogueController.holdListener((LivingEntity)conversationSubject, (Entity)conversationSpeaker, DialogueCatalog.byId(sequence.getFirst()).durationTicks());
                        ContextualDialogueController.queueConversation(level, conversationSpeaker, conversationSubject, sequence);
                        PAIR_TICKS.put(pair, 0);
                    }
                    return;
                }
                CastProfile firstCast = ContextualDialogueController.cast(first);
                CastProfile secondCast = ContextualDialogueController.cast(second);
                String meetId = ContextualDialogueController.meetDialogue(firstCast == CastProfile.VILLAGER ? secondCast : firstCast);
                if (meetId != null && (firstCast == CastProfile.VILLAGER || secondCast == CastProfile.VILLAGER)) {
                    Villager subject;
                    Villager speaker = firstCast == CastProfile.VILLAGER ? first : second;
                    Villager villager5 = subject = speaker == first ? second : first;
                    if (ContextualDialogueController.playId((LivingEntity)speaker, meetId, "meet:" + pair, 3000L, (Entity)subject)) {
                        ContextualDialogueController.holdListener((LivingEntity)subject, (Entity)speaker, 80L);
                        PAIR_TICKS.put(pair, 0);
                    }
                    return;
                }
                boolean atCampfire = ContextualDialogueController.nearBlock(level, first.blockPosition(), "campfire", 4) && ContextualDialogueController.nearBlock(level, second.blockPosition(), "campfire", 4);
                boolean bl = negativeGossip = ContextualDialogueController.isNegativeReputation(first, player) || ContextualDialogueController.isNegativeReputation(second, player);
                String string = atCampfire ? CAMPFIRE_CONVERSATION.getFirst() : (first.getVehicle() != null && first.getVehicle() == second.getVehicle() ? "zqfvby" : (negativeGossip && first.getDeltaMovement().horizontalDistanceSqr() + second.getDeltaMovement().horizontalDistanceSqr() < 4.0E-4 ? (Math.floorMod(pair.hashCode() + (int)(ticks / 3000L), 2) == 0 ? "wrjbdd" : GOSSIP_CONVERSATION.getFirst()) : ContextualDialogueController.wanderingConversation(pair).getFirst()));
                String conversation = string;
                if (!ContextualDialogueController.playId((LivingEntity)first, conversation, "conversation:" + pair + ":" + conversation, 3000L, (Entity)second)) continue;
                ContextualDialogueController.holdListener((LivingEntity)second, (Entity)first, DialogueCatalog.byId(conversation).durationTicks());
                if (conversation.startsWith("gmrypk")) {
                    ContextualDialogueController.queueWanderingConversation(level, first, second, ContextualDialogueController.wanderingConversation(pair));
                } else if (atCampfire) {
                    ContextualDialogueController.queueConversation(level, first, second, CAMPFIRE_CONVERSATION);
                } else if (conversation.equals(GOSSIP_CONVERSATION.getFirst())) {
                    ContextualDialogueController.queueConversation(level, first, second, GOSSIP_CONVERSATION);
                }
                PAIR_TICKS.put(pair, 0);
                return;
            }
            Villager adult = villagers.stream().findFirst().orElse(null);
            List<Villager> babies = ContextualDialogueController.nearbyVillagers(level, player.position(), 24.0).stream().filter(villager -> villager.isBaby() && !villager.isSleeping()).toList();
            Villager baby = babies.stream().findFirst().orElse(null);
            if (babies.size() >= 2 && babies.get(0).distanceToSqr((Entity)babies.get(1)) <= 64.0 && babies.get(0).getDeltaMovement().horizontalDistanceSqr() + babies.get(1).getDeltaMovement().horizontalDistanceSqr() > 0.01) {
                ContextualDialogueController.playId((LivingEntity)babies.get(0), "rfnirh", "baby_chase:" + ContextualDialogueController.orderedPair(babies.get(0).getUUID(), babies.get(1).getUUID()), 3000L, (Entity)babies.get(1));
            }
            if (adult == null || baby == null || !(adult.distanceToSqr((Entity)baby) <= 64.0)) continue;
            ContextualDialogueController.playSharedId((LivingEntity)adult, "pbmrxx", "see_baby:" + String.valueOf(adult.getUUID()) + ":" + String.valueOf(baby.getUUID()), 3000L, (Entity)baby);
        }
    }

    private static List<String> wanderingConversation(String pair) {
        return WANDERING_CONVERSATIONS.get(Math.floorMod(pair.hashCode() + (int)(ticks / 3000L), WANDERING_CONVERSATIONS.size()));
    }

    private static void queueWanderingConversation(ServerLevel level, Villager first, Villager second, List<String> sequence) {
        ContextualDialogueController.queueConversation(level, first, second, sequence);
    }

    private static void queueConversation(ServerLevel level, Villager first, Villager second, List<String> sequence) {
        long due = ticks + DialogueCatalog.byId(sequence.getFirst()).durationTicks() + 2L;
        for (int index = 1; index < sequence.size(); ++index) {
            Villager speaker = index % 2 == 1 ? second : first;
            Villager target = speaker == first ? second : first;
            String id = sequence.get(index);
            PENDING_SPEECH.add(new PendingSpeech(level, speaker.getUUID(), id, target.getUUID(), due));
            due += DialogueCatalog.byId(id).durationTicks() + 2L;
        }
    }

    private static Villager nearestConversationPartner(Villager villager, List<Villager> candidates) {
        return candidates.stream().filter(candidate -> candidate != villager && !ContextualDialogueController.isBusy((LivingEntity)candidate)).filter(candidate -> villager.distanceToSqr((Entity)candidate) <= 6.25 && villager.hasLineOfSight((Entity)candidate)).min(Comparator.comparingDouble(arg_0 -> ((Villager)villager).distanceToSqr(arg_0))).orElse(null);
    }

    private static boolean nearBlock(ServerLevel level, BlockPos origin, String pathPart, int range) {
        for (int x = -range; x <= range; ++x) {
            for (int y = -2; y <= 2; ++y) {
                for (int z = -range; z <= range; ++z) {
                    String path = BuiltInRegistries.BLOCK.getKey(level.getBlockState(origin.offset(x, y, z)).getBlock()).getPath();
                    if (!path.contains(pathPart)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean playHomeChestReaction(ServerLevel level, Player player, BlockPos chestPos) {
        boolean bedNearby = ContextualDialogueController.nearBlock(level, chestPos, "bed", 6);
        return ContextualDialogueController.nearbyVillagers(level, Vec3.atCenterOf((Vec3i)chestPos), 16.0).stream().filter(villager -> !villager.isBaby() && !villager.isSleeping() && ContextualDialogueController.cast(villager) == CastProfile.VILLAGER).filter(villager -> bedNearby || ContextualDialogueController.homeMatches(villager, chestPos, 12)).filter(villager -> villager.hasLineOfSight((Entity)player)).min(Comparator.comparingDouble(villager -> villager.distanceToSqr((Entity)player))).map(villager -> ContextualDialogueController.playId((LivingEntity)villager, "qfhrlh", "home_chest:" + String.valueOf(villager.getUUID()), 900L, (Entity)player)).orElse(false);
    }

    private static boolean homeMatches(Villager villager, BlockPos pos, int range) {
        return villager.getBrain().getMemory(MemoryModuleType.HOME).map(home -> home.isCloseEnough(villager.level().dimension(), pos, range)).orElse(false);
    }

    private static void playFireworkReactions(ServerLevel level, Entity firework) {
        List<Villager> witnesses = ContextualDialogueController.nearbyVillagers(level, firework.position(), 16.0).stream().filter(villager -> !villager.isSleeping()).filter(villager -> villager.hasLineOfSight(firework)).toList();
        witnesses.stream().filter(villager -> !villager.isBaby()).min(Comparator.comparingDouble(villager -> villager.distanceToSqr(firework))).ifPresent(villager -> ContextualDialogueController.playSharedId((LivingEntity)villager, "dfdkli", "firework_spawn:" + String.valueOf(villager.getUUID()), 900L, firework));
        witnesses.stream().filter(AgeableMob::isBaby).min(Comparator.comparingDouble(villager -> villager.distanceToSqr(firework))).ifPresent(villager -> ContextualDialogueController.playId((LivingEntity)villager, "zeykfp", "firework_seen:" + String.valueOf(villager.getUUID()), 900L, firework));
    }

    private static void playLightningReaction(ServerLevel level, Entity lightning) {
        ContextualDialogueController.nearbyVillagers(level, lightning.position(), 128.0).stream().filter(villager -> !villager.isBaby() && !villager.isSleeping() && villager.hasLineOfSight(lightning)).min(Comparator.comparingDouble(villager -> villager.distanceToSqr(lightning))).ifPresent(villager -> ContextualDialogueController.playSharedId((LivingEntity)villager, "ikrwzy", "lightning:" + String.valueOf(villager.getUUID()), 900L, lightning));
    }

    private static void processVillagerState(Villager villager) {
        long danger;
        Level id3;
        String id2;
        ContextualDialogueController.ensureSpecialTrade(villager);
        VillagerSnapshot previous = VILLAGER_STATES.get(villager.getUUID());
        BlockPos workstation = ContextualDialogueController.findWorkstation(villager);
        VillagerSnapshot current = ContextualDialogueController.snapshot(villager, workstation != null);
        Map<String, Integer> oldInventory = VILLAGER_INVENTORIES.get(villager.getUUID());
        boolean sleeping = villager.isSleeping();
        boolean wasSleeping = LAST_SLEEPING.getOrDefault(villager.getUUID(), sleeping);
        if (sleeping) {
            if (wasSleeping) {
                ContextualDialogueController.playTitle((LivingEntity)villager, "Sleeping", "sleeping:" + String.valueOf(villager.getUUID()), 3000L);
            }
            LAST_SLEEPING.put(villager.getUUID(), true);
            VILLAGER_INVENTORIES.put(villager.getUUID(), ContextualDialogueController.inventoryCounts(villager));
            VILLAGER_STATES.put(villager.getUUID(), current);
            return;
        }
        if (wasSleeping) {
            ContextualDialogueController.playSharedTitle((LivingEntity)villager, "Wake Up Naturally", "wake:" + String.valueOf(villager.getUUID()), 3000L, null);
        }
        LAST_SLEEPING.put(villager.getUUID(), false);
        ItemStack pickedUp = ContextualDialogueController.findNewItem(villager, oldInventory);
        if (pickedUp != null) {
            ServerPlayer nearbyPlayer;
            Villager villager2;
            boolean armor;
            String path = BuiltInRegistries.ITEM.getKey(pickedUp.getItem()).getPath();
            boolean bl = armor = path.endsWith("_helmet") || path.endsWith("_chestplate") || path.endsWith("_leggings") || path.endsWith("_boots");
            id2 = armor ? (pickedUp.isEnchanted() ? "habfnx" : "zjwpzi") : "dxmmiu";
            boolean food = path.equals("beetroot") || path.equals("bread") || path.equals("carrot") || path.equals("potato");
            Level level = villager.level();
            if (level instanceof ServerLevel) {
                ServerLevel level2 = (ServerLevel)level;
                villager2 = ContextualDialogueController.nearbyVillagers(level2, villager.position(), 4.0).stream().filter(other -> other != villager).min(Comparator.comparingDouble(other -> other.distanceToSqr((Entity)villager))).orElse(null);
            } else {
                villager2 = null;
            }
            Villager donor = villager2;
            nearbyPlayer = null;
            if (villager.level() instanceof ServerLevel) {
                nearbyPlayer = ContextualDialogueController.nearestPlayer((ServerLevel)villager.level(), villager.position(), 6.0);
            }
            if (villager.isBaby() && donor != null && food && ContextualDialogueController.playId((LivingEntity)donor, "locuih", "share_food:" + String.valueOf(donor.getUUID()), 900L, (Entity)villager)) {
                long due = ticks + DialogueCatalog.byId("locuih").durationTicks() + 2L;
                PENDING_SPEECH.add(new PendingSpeech((ServerLevel)villager.level(), villager.getUUID(), "saxuwk", donor.getUUID(), due));
            } else if (donor != null && !armor) {
                ContextualDialogueController.playId((LivingEntity)villager, "ujyxfg", "pickup_villager:" + String.valueOf(villager.getUUID()) + ":" + path, 900L, (Entity)donor);
            } else if (nearbyPlayer != null) {
                ContextualDialogueController.playId((LivingEntity)villager, armor ? id2 : "vkhrme", "pickup_player:" + String.valueOf(villager.getUUID()) + ":" + path, 900L, (Entity)nearbyPlayer);
            } else {
                ContextualDialogueController.playId((LivingEntity)villager, id2, "pickup:" + String.valueOf(villager.getUUID()) + ":" + path, 900L);
            }
        }
        VILLAGER_INVENTORIES.put(villager.getUUID(), ContextualDialogueController.inventoryCounts(villager));
        if (previous != null) {
            if (previous.baby && !current.baby) {
                ContextualDialogueController.playId((LivingEntity)villager, "smvnbj", "grow:" + String.valueOf(villager.getUUID()), 1L);
            } else if ((previous.profession.equals("none") || previous.profession.equals("nitwit")) && !current.profession.equals("none") && !current.profession.equals("nitwit")) {
                ContextualDialogueController.playId((LivingEntity)villager, "zndzjx", "job:" + String.valueOf(villager.getUUID()), 1L);
            } else if (current.level > previous.level) {
                ContextualDialogueController.playId((LivingEntity)villager, current.level >= 5 ? "pnvkfy" : "fltegg", "level:" + String.valueOf(villager.getUUID()) + ":" + current.level, 1L);
            } else if (!current.name.isEmpty() && !current.name.equals(previous.name)) {
                String lower = current.name.toLowerCase(Locale.ROOT);
                String nameId = current.baby ? (lower.equals("dragon") ? "cmrqhw" : "gzsztp") : (lower.equals("dinnerbone") ? "qmpcxi" : (lower.equals("jeb") || lower.equals("jeb_") ? "armupg" : "spfsrr"));
                ContextualDialogueController.playId((LivingEntity)villager, nameId, "name:" + String.valueOf(villager.getUUID()) + ":" + current.name, 1L);
            }
            if (!previous.working && current.working) {
                ContextualDialogueController.playId((LivingEntity)villager, "qawras", "work_start:" + String.valueOf(villager.getUUID()), 3000L, workstation == null ? null : Vec3.atCenterOf((Vec3i)workstation));
            } else if (current.working) {
                String work;
                String string = work = ticks / 3000L % 3L == 0L ? "sdhkke" : ContextualDialogueController.professionWorkDialogue(current.profession);
                if (work != null) {
                    ContextualDialogueController.playId((LivingEntity)villager, work, "work:" + String.valueOf(villager.getUUID()) + ":" + work, 3000L, workstation == null ? null : Vec3.atCenterOf((Vec3i)workstation));
                }
            }
        }
        ContextualDialogueController.processConditionDialogues(villager);
        if (!current.profession.equals("none") && !current.profession.equals("nitwit") && workstation == null) {
            long since = NO_WORKSTATION_SINCE.computeIfAbsent(villager.getUUID(), ignored -> ticks);
            if (ticks - since >= 600L) {
                ContextualDialogueController.playId((LivingEntity)villager, "ywzhwz", "missing_workstation:" + String.valueOf(villager.getUUID()), 3000L);
                NO_WORKSTATION_SINCE.put(villager.getUUID(), ticks);
            }
        } else {
            NO_WORKSTATION_SINCE.remove(villager.getUUID());
        }
        if (!villager.isBaby() && !villager.getBrain().hasMemoryValue(MemoryModuleType.MEETING_POINT)) {
            long since = NO_BELL_SINCE.computeIfAbsent(villager.getUUID(), ignored -> ticks);
            if (ticks - since >= 1200L) {
                ContextualDialogueController.playId((LivingEntity)villager, "trphsn", "missing_bell:" + String.valueOf(villager.getUUID()), 3000L);
                NO_BELL_SINCE.put(villager.getUUID(), ticks);
            }
        } else {
            NO_BELL_SINCE.remove(villager.getUUID());
        }
        if (ContextualDialogueController.profession(villager).equals("farmer") && ContextualDialogueController.nearCrops(villager.level(), villager.blockPosition(), 4)) {
            ContextualDialogueController.playId((LivingEntity)villager, "aobqjt", "farming:" + String.valueOf(villager.getUUID()), 3000L);
        }
        if (!villager.isSleeping() && villager.getDeltaMovement().horizontalDistanceSqr() > 4.0E-4 && (id3 = villager.level()) instanceof ServerLevel) {
            float sunAngle;
            ServerLevel level = (ServerLevel)id3;
            if (!ContextualDialogueController.data(villager).vnap$hasNose()) {
                ContextualDialogueController.playId((LivingEntity)villager, "dcvgnm", "no_nose_wander:" + String.valueOf(villager.getUUID()), 3000L);
            }
            if ((sunAngle = ((Float)level.environmentAttributes().getValue(EnvironmentAttributes.SUN_ANGLE, villager.blockPosition())).floatValue()) >= 0.5f && sunAngle < 0.85f) {
                id2 = level.dimension() == Level.END ? "iubjul" : (level.dimension() == Level.NETHER ? "bvtmmz" : (level.dimension() != Level.OVERWORLD ? "uhbigm" : (villager.getBrain().hasMemoryValue(MemoryModuleType.HOME) ? "wkfbuv" : "uqguqj")));
                ContextualDialogueController.playId((LivingEntity)villager, id2, "return_home:" + String.valueOf(villager.getUUID()) + ":" + id2, 3000L);
            }
        }
        if (ticks - (danger = LAST_DANGER.getOrDefault(villager.getUUID(), -4611686018427387904L).longValue()) >= 100L && ticks - danger <= 200L) {
            ContextualDialogueController.playId((LivingEntity)villager, villager.isBaby() ? "wsxfok" : "wbbxpo", "calm:" + String.valueOf(villager.getUUID()), 3000L);
        }
        if (villager.isBaby() && villager.getDeltaMovement().horizontalDistanceSqr() > 0.02) {
            boolean weekend = LocalDate.now().getDayOfWeek() == DayOfWeek.SATURDAY || LocalDate.now().getDayOfWeek() == DayOfWeek.SUNDAY;
            ContextualDialogueController.playId((LivingEntity)villager, weekend ? "vbclem" : "vhwksn", "baby_sprint:" + String.valueOf(villager.getUUID()), 3000L);
        }
        VILLAGER_STATES.put(villager.getUUID(), current);
    }

    private static Map<String, Integer> inventoryCounts(Villager villager) {
        HashMap<String, Integer> counts = new HashMap<String, Integer>();
        for (ItemStack stack : villager.getInventory().getItems()) {
            if (stack.isEmpty()) continue;
            counts.merge(BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath(), stack.getCount(), Integer::sum);
        }
        return counts;
    }

    private static ItemStack findNewItem(Villager villager, Map<String, Integer> previous) {
        if (previous == null) {
            return null;
        }
        Map<String, Integer> current = ContextualDialogueController.inventoryCounts(villager);
        for (ItemStack stack : villager.getInventory().getItems()) {
            String path;
            if (stack.isEmpty() || current.getOrDefault(path = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath(), 0) <= previous.getOrDefault(path, 0)) continue;
            return stack;
        }
        return null;
    }

    private static VillagerSnapshot snapshot(Villager villager, boolean working) {
        String name = villager.hasCustomName() && villager.getCustomName() != null ? villager.getCustomName().getString() : "";
        return new VillagerSnapshot(villager.isBaby(), ContextualDialogueController.profession(villager), villager.getVillagerData().level(), working, name, villager.hasEffect(MobEffects.POISON), villager.hasEffect(MobEffects.SLOWNESS), villager.hasEffect(MobEffects.WEAKNESS), villager.isInWall());
    }

    private static List<String> activeConditionDialogues(Villager villager) {
        ArrayList<String> active = new ArrayList<String>();
        if (villager.hasEffect(MobEffects.POISON)) {
            active.add("onindz");
        }
        if (villager.hasEffect(MobEffects.SLOWNESS)) {
            active.add("xemyaj");
        }
        if (villager.hasEffect(MobEffects.WEAKNESS)) {
            active.add("yebifs");
        }
        if (villager.isInLava()) {
            active.add("elryje");
        } else if (villager.isOnFire()) {
            active.add("etkxko");
        }
        if (villager.isFullyFrozen()) {
            active.add("igebly");
        }
        if (villager.isInWall()) {
            active.add("vnaodx");
        }
        return active;
    }

    private static boolean canSpeakDuringCondition(Villager villager, String dialogue) {
        PendingConditionRelief relief = PENDING_CONDITION_RELIEF.get(villager.getUUID());
        List<String> active = ContextualDialogueController.activeConditionDialogues(villager);
        if (active.isEmpty()) {
            return relief == null || relief.dialogueId.equals(dialogue);
        }
        if (active.contains(dialogue)) {
            return true;
        }
        if (villager.isBaby()) {
            return dialogue.equals("ahcvzd") || dialogue.equals("ecslqo");
        }
        CastProfile profile = ContextualDialogueController.cast(villager);
        return profile != CastProfile.VILLAGER && (dialogue.equals(profile.hurt) || dialogue.equals(profile.attack));
    }

    private static void processConditionDialogues(Villager villager) {
        CastProfile profile;
        UUID id = villager.getUUID();
        List<String> active = ContextualDialogueController.activeConditionDialogues(villager);
        if (active.isEmpty()) {
            Set<String> history = CONDITION_HISTORY.remove(id);
            CONDITION_CURSORS.remove(id);
            if (history != null && !history.isEmpty()) {
                ContextualDialogueController.queueConditionRelief(villager, history, ticks + 1L);
            }
            return;
        }
        PENDING_CONDITION_RELIEF.remove(id);
        CONDITION_HISTORY.computeIfAbsent(id, ignored -> new HashSet()).addAll(active);
        PENDING_SPEECH.removeIf(pending -> pending.speakerId.equals(id) || id.equals(pending.targetId));
        List<String> reactions = villager.isBaby() ? List.of("ecslqo") : ((profile = ContextualDialogueController.cast(villager)) == CastProfile.VILLAGER ? active : List.of(profile.hurt));
        ActiveSound sound = ACTIVE_SOUNDS.get(id);
        if (sound != null && sound.endTick > ticks && reactions.contains(sound.groupId)) {
            return;
        }
        if (ContextualDialogueController.isBusy((LivingEntity)villager)) {
            ContextualDialogueController.interrupt((LivingEntity)villager);
        }
        int cursor = Math.floorMod(CONDITION_CURSORS.getOrDefault(id, 0), reactions.size());
        for (int offset = 0; offset < reactions.size(); ++offset) {
            String key;
            int index = (cursor + offset) % reactions.size();
            String dialogue = reactions.get(index);
            if (!ContextualDialogueController.playId((LivingEntity)villager, dialogue, key = "condition:" + String.valueOf(id) + ":" + dialogue, 1L)) continue;
            CONDITION_CURSORS.put(id, index + 1);
            break;
        }
    }

    private static boolean queueConditionRelief(Villager villager, Set<String> conditions, long dueTick) {
        String dialogue;
        if (villager.isBaby()) {
            dialogue = "wsxfok";
        } else if (ContextualDialogueController.cast(villager) == CastProfile.VILLAGER) {
            dialogue = conditions.contains("vnaodx") ? "fxbysi" : "wbbxpo";
        } else {
            return false;
        }
        LAST_DANGER.remove(villager.getUUID());
        PENDING_CONDITION_RELIEF.put(villager.getUUID(), new PendingConditionRelief((ServerLevel)villager.level(), villager.getUUID(), dialogue, dueTick));
        return true;
    }

    private static void processPendingConditionRelief() {
        PENDING_CONDITION_RELIEF.entrySet().removeIf(entry -> {
            Villager villager;
            PendingConditionRelief pending = (PendingConditionRelief)entry.getValue();
            if (pending.dueTick > ticks) {
                return false;
            }
            Entity entity = pending.level.getEntity(pending.villagerId);
            if (!(entity instanceof Villager) || !(villager = (Villager)entity).isAlive()) {
                return true;
            }
            if (!ContextualDialogueController.activeConditionDialogues(villager).isEmpty()) {
                return false;
            }
            ActiveSound sound = ACTIVE_SOUNDS.get(villager.getUUID());
            if (sound != null && sound.endTick > ticks && DAMAGE_LOCK_DIALOGUES.contains(sound.groupId)) {
                return false;
            }
            if (ContextualDialogueController.isBusy((LivingEntity)villager)) {
                ContextualDialogueController.interrupt((LivingEntity)villager);
            }
            return ContextualDialogueController.playId((LivingEntity)villager, pending.dialogueId, "condition_relief:" + String.valueOf(villager.getUUID()) + ":" + pending.dialogueId, 1L);
        });
    }

    private static boolean queueFreedSuffocationRelief(ServerLevel level, BlockPos pos) {
        AABB block = new AABB((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)pos.getX() + 1.0, (double)pos.getY() + 1.0, (double)pos.getZ() + 1.0);
        boolean queued = false;
        for (Villager villager : ContextualDialogueController.nearbyVillagers(level, Vec3.atCenterOf((Vec3i)pos), 4.0)) {
            Set<String> history = CONDITION_HISTORY.get(villager.getUUID());
            VillagerSnapshot snapshot = VILLAGER_STATES.get(villager.getUUID());
            boolean wasSuffocating = history != null && history.contains("vnaodx") || snapshot != null && snapshot.suffocating;
            if (!wasSuffocating || !villager.getBoundingBox().inflate(0.25).intersects(block)) continue;
            CONDITION_HISTORY.remove(villager.getUUID());
            CONDITION_CURSORS.remove(villager.getUUID());
            queued |= ContextualDialogueController.queueConditionRelief(villager, Set.of("vnaodx"), ticks + 1L);
        }
        return queued;
    }

    private static String profession(Villager villager) {
        return villager.getVillagerData().profession().unwrapKey().map(key -> key.identifier().getPath()).orElse("none");
    }

    private static BlockPos findWorkstation(Villager villager) {
        String block = switch (ContextualDialogueController.profession(villager)) {
            case "armorer" -> "blast_furnace";
            case "butcher" -> "smoker";
            case "cartographer" -> "cartography_table";
            case "cleric" -> "brewing_stand";
            case "farmer" -> "composter";
            case "fisherman" -> "barrel";
            case "fletcher" -> "fletching_table";
            case "leatherworker" -> "cauldron";
            case "librarian" -> "lectern";
            case "mason" -> "stonecutter";
            case "shepherd" -> "loom";
            case "toolsmith" -> "smithing_table";
            case "weaponsmith" -> "grindstone";
            default -> null;
        };
        if (block == null) {
            return null;
        }
        BlockPos origin = villager.blockPosition();
        for (int x = -3; x <= 3; ++x) {
            for (int y = -2; y <= 2; ++y) {
                for (int z = -3; z <= 3; ++z) {
                    BlockPos pos = origin.offset(x, y, z);
                    if (!BuiltInRegistries.BLOCK.getKey(villager.level().getBlockState(pos).getBlock()).getPath().equals(block)) continue;
                    return pos;
                }
            }
        }
        return null;
    }

    private static boolean nearCrops(Level level, BlockPos origin, int range) {
        for (int x = -range; x <= range; ++x) {
            for (int y = -2; y <= 2; ++y) {
                for (int z = -range; z <= range; ++z) {
                    String path = BuiltInRegistries.BLOCK.getKey(level.getBlockState(origin.offset(x, y, z)).getBlock()).getPath();
                    if (!path.equals("wheat") && !path.equals("carrots") && !path.equals("potatoes") && !path.equals("beetroots") && !path.equals("torchflower_crop") && !path.equals("pitcher_crop")) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private static String professionWorkDialogue(String profession) {
        return switch (profession) {
            case "armorer" -> "djpksc";
            case "butcher" -> "ueczyh";
            case "cartographer" -> "wuoloh";
            case "cleric" -> "hkowex";
            case "farmer" -> "umdvtb";
            case "fisherman" -> "tgggoh";
            case "fletcher" -> "fzjope";
            case "leatherworker" -> "ljewqf";
            case "librarian" -> "fezzjw";
            case "mason" -> "yldlzt";
            case "shepherd" -> "opxfuo";
            case "toolsmith" -> "ivktls";
            case "weaponsmith" -> "ccpvqj";
            default -> null;
        };
    }

    private static boolean playNearbyEntityContext(ServerLevel level, Villager speaker) {
        AABB area = speaker.getBoundingBox().inflate(8.0, 6.0, 8.0);
        List<Entity> visibleEntities = level.getEntities((Entity)speaker, area, Entity::isAlive).stream().filter(entity -> speaker.distanceToSqr(entity) <= 64.0).filter(arg_0 -> ((Villager)speaker).hasLineOfSight(arg_0)).sorted(Comparator.comparingDouble(arg_0 -> ((Villager)speaker).distanceToSqr(arg_0))).toList();
        for (Entity entity2 : visibleEntities) {
            String id;
            String path;
            if ((id = (switch (path = BuiltInRegistries.ENTITY_TYPE.getKey(entity2.getType()).getPath()) {
                case "falling_block" -> "bodvsv";
                case "experience_orb" -> "cvltyw";
                case "tnt" -> "pmaqgq";
                case "firework_rocket" -> {
                    if (speaker.isBaby()) {
                        yield "zeykfp";
                    }
                    yield null;
                }
                default -> null;
            })) == null || !ContextualDialogueController.playSharedId((LivingEntity)speaker, id, "nearby_misc:" + String.valueOf(speaker.getUUID()) + ":" + String.valueOf(entity2.getUUID()) + ":" + id, 3000L, entity2)) continue;
            return true;
        }
        List<ItemEntity> droppedItems = level.getEntitiesOfClass(ItemEntity.class, AABB.ofSize((Vec3)speaker.position(), (double)10.0, (double)10.0, (double)10.0), Entity::isAlive).stream().filter(arg_0 -> ((Villager)speaker).hasLineOfSight(arg_0)).sorted(Comparator.comparingDouble(arg_0 -> ((Villager)speaker).distanceToSqr(arg_0))).toList();
        if (droppedItems.size() >= 5 && ContextualDialogueController.playSharedId((LivingEntity)speaker, "zywcju", "item_pile:" + String.valueOf(speaker.getUUID()), 3000L, (Entity)droppedItems.getFirst())) {
            return true;
        }
        return visibleEntities.stream().filter(LivingEntity.class::isInstance).map(LivingEntity.class::cast).filter(entity -> entity != speaker && !(entity instanceof Player) && !(entity instanceof Villager)).anyMatch(entity -> {
            Mob mob;
            TamableAnimal tame;
            Sheep sheep;
            String dialogue;
            String path = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).getPath();
            LivingEntity target = entity;
            String string = dialogue = speaker.isBaby() && path.equals("iron_golem") ? "mqnapy" : null;
            if (dialogue == null && entity instanceof Sheep && (sheep = (Sheep)entity).isSheared()) {
                dialogue = "afxbav";
            }
            if (dialogue == null && entity instanceof TamableAnimal && (tame = (TamableAnimal)entity).isTame()) {
                String string2 = dialogue = entity.isBaby() ? "sxikgq" : "aqxgxh";
            }
            if (dialogue == null && entity instanceof Mob && (mob = (Mob)entity).getTarget() != null) {
                LivingEntity patt0$temp;
                if (path.equals("bee")) {
                    dialogue = "bmimxe";
                } else if (path.equals("iron_golem") && (patt0$temp = mob.getTarget()) instanceof Player) {
                    Player player = (Player)patt0$temp;
                    dialogue = "qffeco";
                    target = player;
                }
            }
            if (dialogue == null && (entity.isPassenger() || !entity.getPassengers().isEmpty())) {
                dialogue = "dxeaal";
            }
            if (dialogue == null && entity.isBaby()) {
                dialogue = BABY_ENTITY_DIALOGUES.get(path);
            }
            if (dialogue == null) {
                dialogue = NEARBY_ENTITY_DIALOGUES.get(path);
            }
            if (!(dialogue != null || entity instanceof WanderingTrader || entity instanceof Sheep && ContextualDialogueController.isWooly(sheep = (Sheep)entity))) {
                dialogue = "gjtuqd";
            }
            return dialogue != null && speaker.hasLineOfSight((Entity)target) && ContextualDialogueController.playSharedId((LivingEntity)speaker, dialogue, "nearby:" + String.valueOf(speaker.getUUID()) + ":" + String.valueOf(entity.getUUID()) + ":" + dialogue, 3000L, (Entity)target);
        });
    }

        private static void onDamage(LivingEntity entity, DamageSource source, float baseDamageTaken, float damageTaken, boolean blocked) {
        if (entity.entityTags().contains("vnap_dialogue_test")) {
            return;
        }
        if (blocked || damageTaken <= 0.0f) {
            return;
        }
        ContextualDialogueController.playIronGolemAttackWitness(entity, source);
        ContextualDialogueController.playHurtWitness(entity);
        if (ContextualDialogueController.hasDamageLock(entity)) {
            return;
        }
        if (entity instanceof Sheep) {
            Sheep sheep = (Sheep) entity;
            if (ContextualDialogueController.isWooly(sheep)) {
                Entity attacker = source.getEntity();
                String id = (attacker instanceof Player) ? "ncyeaw" : "eyiraw";
                ContextualDialogueController.playDamageDialogue(sheep, id, "hurt:" + sheep.getUUID(), source, attacker);
                return;
            }
        }
        if (entity instanceof WanderingTrader) {
            WanderingTrader trader = (WanderingTrader) entity;
            Entity attacker = source.getEntity();
            String id = (attacker instanceof Player) ? "vevdkl" : "wyvzhk";
            ContextualDialogueController.playDamageDialogue(trader, id, "hurt:" + trader.getUUID(), source, attacker);
            return;
        }
        if (!(entity instanceof Villager)) {
            return;
        }
        Villager villager = (Villager) entity;
        ContextualDialogueController.LAST_DANGER.put(villager.getUUID(), ContextualDialogueController.ticks);
        if (villager.isBaby()) {
            Entity attacker = source.getEntity();
            String id = (attacker instanceof Player && source.getDirectEntity() == attacker) ? "ahcvzd" : "ecslqo";
            ContextualDialogueController.playDamageDialogue(villager, id, "hurt:" + villager.getUUID(), source, source.getEntity());
            return;
        }
        CastProfile profile = ContextualDialogueController.cast(villager);
        if (profile != CastProfile.VILLAGER) {
            String id = (source.getEntity() instanceof Player) ? profile.attack : profile.hurt;
            ContextualDialogueController.playDamageDialogue(villager, id, "hurt:" + villager.getUUID(), source, source.getEntity());
            return;
        }
        Entity sourceEntity = source.getEntity();
        if (sourceEntity instanceof Player) {
            Player player = (Player) sourceEntity;
            if (ContextualDialogueController.homeMatches(villager, villager.blockPosition(), 4)) {
                if (ContextualDialogueController.isPlaying(villager, "lpuocy") || ContextualDialogueController.isPlaying(villager, "slbqfwbayahw")) {
                    return;
                }
                ContextualDialogueController.interrupt(villager);
                ContextualDialogueController.playHomeAttackReaction(villager, player);
                return;
            }
        }
        String dialogue = ContextualDialogueController.damageDialogue(source);
        if (ContextualDialogueController.playDamageDialogue(villager, dialogue, "hurt:" + villager.getUUID() + ":" + dialogue, source, source.getEntity()) && ContextualDialogueController.ready("panic:" + villager.getUUID(), 900L)) {
            Level villagerLevel = villager.level();
            if (villagerLevel instanceof ServerLevel) {
                ServerLevel level = (ServerLevel) villagerLevel;
                ContextualDialogueController.COOLDOWNS.put("panic:" + villager.getUUID(), ContextualDialogueController.ticks);
                DialogueCatalog.DialogueGroup group = DialogueCatalog.byId(dialogue);
                long delay = (group != null) ? group.durationTicks() + 2L : 20L;
                ContextualDialogueController.PENDING_SPEECH.add(new PendingSpeech(level, villager.getUUID(), "uzdxum", source.getEntity() == null ? null : source.getEntity().getUUID(), ContextualDialogueController.ticks + delay));
            }
        }
    }

    private static boolean playDamageDialogue(LivingEntity speaker, String id, String cooldownKey, DamageSource source, Entity target) {
        Villager villager;
        DialogueCatalog.DialogueGroup group = DialogueCatalog.byId(id);
        long cooldown = ONGOING_DAMAGE_DIALOGUES.contains(id) ? 1L : (source.getEntity() == null ? 120L : (group == null ? 20L : group.durationTicks() + 20L));
        if (ContextualDialogueController.isPlaying(speaker, id) || !ContextualDialogueController.ready(cooldownKey, cooldown)) {
            return false;
        }
        ContextualDialogueController.interrupt(speaker);
        boolean sharedAdult = speaker instanceof WanderingTrader || speaker instanceof Villager && ContextualDialogueController.cast(villager = (Villager)speaker) == CastProfile.UNREACHABLE;
        return sharedAdult ? ContextualDialogueController.playSharedId(speaker, id, cooldownKey, cooldown, target) : ContextualDialogueController.playId(speaker, id, cooldownKey, cooldown, target);
    }

    private static void playHurtWitness(LivingEntity entity) {
        Level level = entity.level();
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel level2 = (ServerLevel)level;
        ContextualDialogueController.nearbyVillagers(level2, entity.position(), 16.0).stream().filter(witness -> witness != entity && !witness.isBaby() && !witness.isSleeping() && !ContextualDialogueController.isBusy((LivingEntity)witness) && witness.hasLineOfSight((Entity)entity)).min(Comparator.comparingDouble(witness -> witness.distanceToSqr((Entity)entity))).ifPresent(witness -> ContextualDialogueController.playSharedId((LivingEntity)witness, "pkvhpv", "witness_hurt:" + String.valueOf(witness.getUUID()), 900L, (Entity)entity));
    }

    private static void playIronGolemAttackWitness(LivingEntity entity, DamageSource source) {
        String attackerType;
        Level level = entity.level();
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel level2 = (ServerLevel)level;
        ServerPlayer player = null;
        String hurtType = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).getPath();
        Entity attacker = source.getEntity();
        String string = attackerType = attacker == null ? "" : BuiltInRegistries.ENTITY_TYPE.getKey(attacker.getType()).getPath();
        if (hurtType.equals("iron_golem") && attacker instanceof ServerPlayer) {
            ServerPlayer serverPlayer;
            player = serverPlayer = (ServerPlayer)attacker;
        } else if (entity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)entity;
            if (attackerType.equals("iron_golem")) {
                player = serverPlayer;
            }
        }
        if (player == null || player.isCreative()) {
            return;
        }
        ServerPlayer subject = player;
        ContextualDialogueController.nearbyVillagers(level2, entity.position(), 16.0).stream().filter(witness -> !witness.isBaby() && !witness.isSleeping() && !ContextualDialogueController.isBusy((LivingEntity)witness) && witness.hasLineOfSight((Entity)subject)).min(Comparator.comparingDouble(witness -> witness.distanceToSqr((Entity)entity))).ifPresent(witness -> ContextualDialogueController.playSharedId((LivingEntity)witness, "qffeco", "golem_attack:" + String.valueOf(witness.getUUID()), 900L, (Entity)subject));
    }

    private static String damageDialogue(DamageSource source) {
        String direct;
        String attacker = source.getEntity() == null ? "" : BuiltInRegistries.ENTITY_TYPE.getKey(source.getEntity().getType()).getPath();
        String string = direct = source.getDirectEntity() == null ? "" : BuiltInRegistries.ENTITY_TYPE.getKey(source.getDirectEntity().getType()).getPath();
        if (direct.equals("arrow")) {
            return "huhcbd";
        }
        if (direct.equals("snowball")) {
            return "dlrxes";
        }
        if (source.is(DamageTypes.FALLING_ANVIL)) {
            return "yzqpvi";
        }
        if (source.is(DamageTypes.STALAGMITE) || source.is(DamageTypes.FALLING_STALACTITE)) {
            return "nsxmkr";
        }
        if (direct.equals("firework_rocket") || source.is(DamageTypes.FIREWORKS)) {
            return "gesjov";
        }
        if (direct.equals("potion") || direct.equals("lingering_potion")) {
            return source.is(DamageTypes.MAGIC) || source.is(DamageTypes.INDIRECT_MAGIC) ? "gacgtq" : "hmadgp";
        }
        Entity entity = source.getEntity();
        if (entity instanceof Player) {
            Player player = (Player)entity;
            if (source.getDirectEntity() == player) {
                return ContextualDialogueController.weaponAttackDialogue(player.getMainHandItem());
            }
        }
        if (attacker.equals("evoker")) {
            return "nkcoqb";
        }
        if (attacker.equals("pillager")) {
            return "qhpyaw";
        }
        if (attacker.equals("ravager")) {
            return "uveohs";
        }
        if (attacker.equals("vex")) {
            return "caykki";
        }
        if (attacker.equals("vindicator")) {
            return "swomdw";
        }
        if (attacker.equals("witch")) {
            return "rtikom";
        }
        if (attacker.equals("zoglin")) {
            return "hfmwvf";
        }
        if (attacker.contains("zombie") || attacker.equals("drowned") || attacker.equals("husk")) {
            return "mytmrk";
        }
        if (source.is(DamageTypes.LIGHTNING_BOLT)) {
            return "ikrwzy";
        }
        if (source.is(DamageTypes.EXPLOSION) || source.is(DamageTypes.PLAYER_EXPLOSION)) {
            return "fcbygh";
        }
        if (source.is(DamageTypes.LAVA)) {
            return "elryje";
        }
        if (source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.CAMPFIRE) || source.is(DamageTypes.HOT_FLOOR) || source.is(DamageTypes.SULFUR_CUBE_HOT)) {
            return "etkxko";
        }
        if (source.is(DamageTypes.CACTUS) || source.is(DamageTypes.SWEET_BERRY_BUSH)) {
            return "rogpvp";
        }
        if (source.is(DamageTypes.FREEZE)) {
            return "igebly";
        }
        if (source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.CRAMMING)) {
            return "vnaodx";
        }
        if (source.is(DamageTypes.FALL)) {
            return "cifbit";
        }
        return "wyvzhk";
    }

    private static void onDeath(LivingEntity entity, DamageSource source) {
        Sheep sheep;
        if (entity.entityTags().contains(DIALOGUE_TEST_TAG)) {
            return;
        }
        ContextualDialogueController.interrupt(entity);
        Level level = entity.level();
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel level2 = (ServerLevel)level;
        if (entity.entityTags().contains(NATURAL_SPECIAL_TAG)) {
            ContextualDialogueController.clearNaturalSpecial(level2, (Entity)entity);
        }
        if (entity instanceof Villager) {
            Villager villager = (Villager)entity;
            BUSY_UNTIL.remove(villager.getUUID());
            if (villager.isBaby()) {
                ContextualDialogueController.playId((LivingEntity)villager, "ecslqo", "death:" + String.valueOf(villager.getUUID()), 1L);
            } else {
                ContextualDialogueController.playSharedId((LivingEntity)villager, "hivgme", "death:" + String.valueOf(villager.getUUID()), 1L, null);
            }
        }
        if (entity instanceof Villager || entity instanceof WanderingTrader || entity instanceof Sheep && ContextualDialogueController.isWooly(sheep = (Sheep)entity)) {
            ContextualDialogueController.nearbyVillagers(level2, entity.position(), 16.0).stream().filter(witness -> witness != entity && !witness.isBaby() && !witness.isSleeping()).min(Comparator.comparingDouble(witness -> witness.distanceToSqr((Entity)entity))).ifPresent(witness -> ContextualDialogueController.playSharedId((LivingEntity)witness, "pmqrpb", "witness_death:" + String.valueOf(witness.getUUID()), 900L, (Entity)entity));
        } else if (entity instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer)entity;
            int deaths = PLAYER_DEATHS.merge(player.getUUID(), 1, Integer::sum);
            String id = level2.getLevelData().isHardcore() ? "elcjbb" : (deaths > 1 ? "dxcjqn" : "hzjycq");
            ContextualDialogueController.nearbyVillagers(level2, player.position(), 16.0).stream().filter(witness -> !witness.isBaby() && !witness.isSleeping() && witness.hasLineOfSight((Entity)player)).min(Comparator.comparingDouble(witness -> witness.distanceToSqr((Entity)player))).ifPresent(witness -> ContextualDialogueController.playSharedId((LivingEntity)witness, id, "player_death:" + String.valueOf(witness.getUUID()) + ":" + deaths, 900L, (Entity)player));
        }
    }

    private static InteractionResult onUseEntity(Player player, Entity entity, InteractionHand hand) {
        Level level;
        Level level2;
        Sheep sheep;
        if (entity.entityTags().contains(DIALOGUE_TEST_TAG)) {
            return InteractionResult.SUCCESS;
        }
        if (entity instanceof Villager) {
            Level level3;
            Villager villager = (Villager)entity;
            if (ContextualDialogueController.cast(villager) == CastProfile.UNREACHABLE) {
                ContextualDialogueController.repelPlayer(villager, player);
                return InteractionResult.SUCCESS;
            }
            ItemStack heldStack = player.getItemInHand(hand);
            String heldItem = BuiltInRegistries.ITEM.getKey(heldStack.getItem()).getPath();
            InteractionResult cosmeticResult = ContextualDialogueController.interactWithCosmetic(player, villager, heldStack);
            if (cosmeticResult != InteractionResult.PASS) {
                return cosmeticResult;
            }
            String gift = ContextualDialogueController.foodGiftDialogue(villager.isBaby(), heldItem);
            if (gift != null) {
                ContextualDialogueController.playId((LivingEntity)villager, gift, "food_gift:" + String.valueOf(villager.getUUID()) + ":" + heldItem, 900L, (Entity)player);
                return InteractionResult.PASS;
            }
            int offeredSign = ContextualDialogueController.signType(heldStack);
            if (offeredSign >= 0 && !villager.isBaby()) {
                VillagerNewsData state = ContextualDialogueController.data(villager);
                int previousSign = state.vnap$signType();
                if (previousSign == offeredSign) {
                    return InteractionResult.SUCCESS;
                }
                if (previousSign >= 0) {
                    villager.spawnAtLocation((ServerLevel)villager.level(), ContextualDialogueController.signItem(previousSign));
                }
                state.vnap$setSignType(offeredSign);
                ContextualDialogueController.consume(player, heldStack);
                villager.level().playSound(null, villager.blockPosition(), SoundEvents.HORSE_STEP_WOOD, SoundSource.NEUTRAL, 1.0f, 1.0f);
                if (previousSign < 0) {
                    state.vnap$setSignMessage(villager.getRandom().nextInt(87));
                    ContextualDialogueController.playId((LivingEntity)villager, "vqlrqf", "sign_gift:" + String.valueOf(villager.getUUID()), 900L, (Entity)player);
                }
                return InteractionResult.SUCCESS;
            }
            if (villager.isSleeping()) {
                UUID id = villager.getUUID();
                WAKE_SOURCES.put(id, player.getUUID());
                INTERRUPTED_SLEEP.add(id);
                villager.stopSleeping();
                return InteractionResult.PASS;
            }
            ContextualDialogueController.ensureSpecialTrade(villager);
            if (!villager.isBaby() && hand == InteractionHand.MAIN_HAND && (level3 = player.level()) instanceof ServerLevel) {
                ServerLevel level4 = (ServerLevel)level3;
                ACTIVE_TRADES.put(player.getUUID(), new TradeSession(level4, villager.getUUID(), ticks));
            }
            if (villager.isBaby()) {
                ContextualDialogueController.playId((LivingEntity)villager, "aezdiy", "baby_trade:" + String.valueOf(villager.getUUID()), 900L, (Entity)player);
            }
        } else if (entity instanceof WanderingTrader) {
            Level level5;
            WanderingTrader trader = (WanderingTrader)entity;
            if (hand == InteractionHand.MAIN_HAND && (level5 = player.level()) instanceof ServerLevel) {
                ServerLevel level6 = (ServerLevel)level5;
                ACTIVE_TRADES.put(player.getUUID(), new TradeSession(level6, trader.getUUID(), ticks));
            }
        } else if (entity instanceof Sheep && ContextualDialogueController.isWooly(sheep = (Sheep)entity)) {
            String held = BuiltInRegistries.ITEM.getKey(player.getMainHandItem().getItem()).getPath();
            ContextualDialogueController.playId((LivingEntity)sheep, held.equals("shears") ? "jqaekk" : "fskcce", "interact:" + String.valueOf(sheep.getUUID()), 900L, (Entity)player);
        } else if (BuiltInRegistries.ITEM.getKey(player.getMainHandItem().getItem()).getPath().equals("lead") && (level2 = player.level()) instanceof ServerLevel) {
            ServerLevel level7 = (ServerLevel)level2;
            ContextualDialogueController.playObserved(level7, player, entity.position(), "Use a Lead", 900L);
        }
        if (entity instanceof Sheep && BuiltInRegistries.ITEM.getKey(player.getMainHandItem().getItem()).getPath().equals("shears") && (level = player.level()) instanceof ServerLevel) {
            ServerLevel level8 = (ServerLevel)level;
            ContextualDialogueController.playObserved(level8, player, entity.position(), "Shear a Sheep", 900L);
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult interactWithCosmetic(Player player, Villager villager, ItemStack stack) {
        VillagerNewsData state = ContextualDialogueController.data(villager);
        if (stack.getItem() == Items.SHEARS && state.vnap$signType() >= 0) {
            Level level = villager.level();
            if (level instanceof ServerLevel) {
                ServerLevel level2 = (ServerLevel)level;
                villager.spawnAtLocation(level2, ContextualDialogueController.signItem(state.vnap$signType()));
                level2.playSound(null, villager.blockPosition(), SoundEvents.SHEEP_SHEAR, SoundSource.NEUTRAL, 1.0f, 1.0f);
            }
            state.vnap$setSignMessage(-1);
            state.vnap$setSignType(-1);
            ContextualDialogueController.damageShears(player, stack);
            return InteractionResult.SUCCESS;
        }
        if (ContextualDialogueController.isAxe(stack) && state.vnap$signType() >= 0) {
            int direction = player.isShiftKeyDown() ? -1 : 1;
            int message = Math.floorMod(state.vnap$signMessage() + direction, 87);
            state.vnap$setSignMessage(message);
            Level level = villager.level();
            if (level instanceof ServerLevel) {
                ServerLevel level3 = (ServerLevel)level;
                level3.playSound(null, villager.blockPosition(), SoundEvents.HORSE_STEP_WOOD, SoundSource.NEUTRAL, 1.0f, 1.0f);
            }
            if (player instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)player;
                serverPlayer.sendOverlayMessage((Component)Component.literal((String)("Sign message " + (message + 1) + " / 87")));
            }
            return InteractionResult.SUCCESS;
        }
        if (stack.getItem() == Items.SHEARS && !villager.isBaby()) {
            if (state.vnap$cosmetic() != 0) {
                Level message = villager.level();
                if (message instanceof ServerLevel) {
                    ServerLevel level = (ServerLevel)message;
                    Item item = VillagerNewsItems.cosmeticItem(state.vnap$cosmetic());
                    if (item != null) {
                        villager.spawnAtLocation(level, new ItemStack((ItemLike)item));
                    }
                    level.playSound(null, villager.blockPosition(), SoundEvents.SHEEP_SHEAR, SoundSource.NEUTRAL, 1.0f, 1.0f);
                }
                state.vnap$setCosmetic(0);
                ContextualDialogueController.damageShears(player, stack);
                ContextualDialogueController.playId((LivingEntity)villager, "ckjbyd", "remove_cosmetic:" + String.valueOf(villager.getUUID()), 1L, (Entity)player);
                return InteractionResult.SUCCESS;
            }
            if (state.vnap$hasNose()) {
                Level level = villager.level();
                if (level instanceof ServerLevel) {
                    ServerLevel level4 = (ServerLevel)level;
                    villager.spawnAtLocation(level4, new ItemStack((ItemLike)VillagerNewsItems.VILLAGER_NOSE));
                    level4.playSound(null, villager.blockPosition(), SoundEvents.SHEEP_SHEAR, SoundSource.NEUTRAL, 1.0f, 1.0f);
                }
                state.vnap$setHasNose(false);
                ContextualDialogueController.damageShears(player, stack);
                ContextualDialogueController.playId((LivingEntity)villager, "jktrnd", "shear_nose:" + String.valueOf(villager.getUUID()), 1L, (Entity)player);
                return InteractionResult.SUCCESS;
            }
        }
        if (stack.getItem() == VillagerNewsItems.VILLAGER_NOSE) {
            if (state.vnap$hasNose()) {
                ContextualDialogueController.playId((LivingEntity)villager, "akfekx", "second_nose:" + String.valueOf(villager.getUUID()), 900L, (Entity)player);
                return InteractionResult.SUCCESS;
            }
            ContextualDialogueController.consume(player, stack);
            state.vnap$setHasNose(true);
            ContextualDialogueController.playId((LivingEntity)villager, "kxrhxt", "return_nose:" + String.valueOf(villager.getUUID()), 1L, (Entity)player);
            return InteractionResult.SUCCESS;
        }
        int cosmetic = VillagerNewsItems.cosmetic(stack.getItem());
        if (cosmetic == 0) {
            return InteractionResult.PASS;
        }
        if (state.vnap$cosmetic() != 0) {
            return InteractionResult.SUCCESS;
        }
        ContextualDialogueController.consume(player, stack);
        state.vnap$setCosmetic(cosmetic);
        ContextualDialogueController.playGivenCosmetic(villager, player, cosmetic);
        return InteractionResult.SUCCESS;
    }

    private static void playGivenCosmetic(Villager villager, Player player, int cosmetic) {
        String id;
        if (villager.isBaby()) {
            id = switch (cosmetic) {
                case 1 -> "svdjdk";
                case 2 -> "cxeziv";
                case 3 -> "riezum";
                case 4 -> "rlkdqd";
                default -> "orogba";
            };
        } else {
            String special = switch (cosmetic) {
                case 2 -> "wurmgu";
                case 3 -> "inirxg";
                case 4 -> "ozxzla";
                default -> null;
            };
            id = special != null && (cosmetic == 3 || ThreadLocalRandom.current().nextInt(3) != 0) ? special : "orogba";
        }
        ContextualDialogueController.playId((LivingEntity)villager, id, "give_cosmetic:" + String.valueOf(villager.getUUID()) + ":" + cosmetic, 1L, (Entity)player);
    }

    private static void damageShears(Player player, ItemStack stack) {
        if (!player.isCreative() && player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            stack.hurtAndBreak(1, serverPlayer.level(), serverPlayer, ignored -> {});
        }
    }

    private static void consume(Player player, ItemStack stack) {
        if (!player.isCreative()) {
            stack.shrink(1);
        }
    }

    private static boolean isStandingSign(ItemStack stack) {
        String path = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
        return path.endsWith("_sign") && !path.endsWith("_hanging_sign");
    }

    public static int signType(ItemStack stack) {
        if (!ContextualDialogueController.isStandingSign(stack)) {
            return -1;
        }
        return switch (BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath()) {
            case "oak_sign" -> 0;
            case "spruce_sign" -> 1;
            case "birch_sign" -> 2;
            case "jungle_sign" -> 3;
            case "acacia_sign" -> 4;
            case "dark_oak_sign" -> 5;
            case "mangrove_sign" -> 6;
            case "cherry_sign" -> 7;
            case "pale_oak_sign" -> 8;
            case "bamboo_sign" -> 9;
            case "crimson_sign" -> 10;
            case "warped_sign" -> 11;
            default -> -1;
        };
    }

    public static ItemStack signItem(int type) {
        return new ItemStack((ItemLike)(switch (type) {
            case 1 -> Items.SPRUCE_SIGN;
            case 2 -> Items.BIRCH_SIGN;
            case 3 -> Items.JUNGLE_SIGN;
            case 4 -> Items.ACACIA_SIGN;
            case 5 -> Items.DARK_OAK_SIGN;
            case 6 -> Items.MANGROVE_SIGN;
            case 7 -> Items.CHERRY_SIGN;
            case 8 -> Items.PALE_OAK_SIGN;
            case 9 -> Items.BAMBOO_SIGN;
            case 10 -> Items.CRIMSON_SIGN;
            case 11 -> Items.WARPED_SIGN;
            default -> Items.OAK_SIGN;
        }));
    }

    private static boolean isAxe(ItemStack stack) {
        return BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath().endsWith("_axe");
    }

    private static String foodGiftDialogue(boolean baby, String item) {
        if (baby) {
            return switch (item) {
                case "beetroot" -> "qrdzmt";
                case "bread" -> "hbalps";
                case "carrot" -> "hcdvqm";
                case "potato" -> "gotjxf";
                default -> null;
            };
        }
        return switch (item) {
            case "beetroot" -> "rlfjux";
            case "bread" -> "bbjsik";
            case "carrot" -> "nqktml";
            case "potato" -> "ytydjc";
            case "wheat" -> "ebyrtk";
            default -> null;
        };
    }

    private static void onAttackEntity(Player player, Entity entity) {
        Villager villager;
        if (!(entity instanceof Villager) || !(villager = (Villager)entity).isSleeping()) {
            return;
        }
        UUID id = villager.getUUID();
        WAKE_SOURCES.put(id, player.getUUID());
        INTERRUPTED_SLEEP.add(id);
        villager.stopSleeping();
    }

    private static void playHomeAttackReaction(Villager villager, Player player) {
        Level level = villager.level();
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel level2 = (ServerLevel)level;
        Villager witness = ContextualDialogueController.nearbyVillagers(level2, villager.position(), 10.0).stream().filter(other -> other != villager && !other.isBaby() && !other.isSleeping() && other.hasLineOfSight((Entity)villager)).min(Comparator.comparingDouble(other -> other.distanceToSqr((Entity)villager))).orElse(null);
        if (witness != null && ContextualDialogueController.playSharedId((LivingEntity)witness, "slbqfwswxeva", "home_witness:" + String.valueOf(witness.getUUID()), 20L, (Entity)villager)) {
            long due = ticks + DialogueCatalog.byId("slbqfwswxeva").durationTicks() + 2L;
            PENDING_SPEECH.add(new PendingSpeech(level2, villager.getUUID(), "slbqfwbayahw", witness.getUUID(), due));
        } else {
            ContextualDialogueController.playId((LivingEntity)villager, "lpuocy", "attacked_home:" + String.valueOf(villager.getUUID()), 20L, (Entity)player);
        }
    }

    private static String weaponAttackDialogue(ItemStack stack) {
        String path = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
        if (path.endsWith("_sword")) {
            return "rueszy";
        }
        if (path.endsWith("_axe")) {
            return "yjctyw";
        }
        if (path.endsWith("_hoe")) {
            return "qqyjjg";
        }
        if (path.endsWith("_shovel")) {
            return "hpnsfu";
        }
        return "vevdkl";
    }

    private static String selectBreakContext(BlockState state, PlayerObservation observation) {
        observation.breakStreak = ticks - observation.lastBreakTick <= 30L ? ++observation.breakStreak : 1;
        observation.lastBreakTick = ticks;
        if (observation.breakStreak >= 4) {
            return "Break Multiple Blocks";
        }
        String path = BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath();
        if (path.equals("wheat") || path.equals("carrots") || path.equals("potatoes") || path.equals("beetroots") || path.equals("torchflower_crop") || path.equals("pitcher_crop")) {
            return "Harvest Crops";
        }
        if (path.contains("flower") || path.contains("candle") || path.contains("coral") || path.contains("banner") || path.contains("decorated_pot")) {
            return "Break a Decorative Block";
        }
        if (path.endsWith("_door")) {
            return "Break a Door";
        }
        if (path.endsWith("_bed")) {
            return "Break a Bed";
        }
        if (path.equals("bell")) {
            return "Break a Bell";
        }
        if (ContextualDialogueController.isWorkstation(path)) {
            return "Break a Workstation";
        }
        if (path.contains("log") || path.contains("wood") || path.contains("stem") || path.contains("hyphae")) {
            return "Break Wood";
        }
        if (path.contains("stone") || path.contains("deepslate") || path.contains("cobblestone")) {
            return "Break Stone";
        }
        return "Break a Block";
    }

    private static String selectPlaceContext(Block block, ServerLevel level, BlockPos position) {
        String path = BuiltInRegistries.BLOCK.getKey(block).getPath();
        if (level.dimension() == Level.END) {
            return "Place a Block from the End";
        }
        if (level.dimension() == Level.NETHER) {
            return "Place a Block from the Nether";
        }
        String biome = level.getBiome(position).unwrapKey().map(key -> key.identifier().getPath()).orElse("");
        if (biome.contains("ocean")) {
            return "Place a Block from the Ocean";
        }
        if (path.equals("daylight_detector")) {
            return "Place a Daylight Detector";
        }
        if (path.equals("detector_rail")) {
            return "Place a Detector Rail";
        }
        if (path.equals("lightning_rod")) {
            return "Place a Lightning Rod";
        }
        if (path.equals("melon")) {
            return "Place a Melon";
        }
        if (path.equals("observer")) {
            return "Place an Observer";
        }
        if (path.endsWith("pressure_plate")) {
            return "Place a Pressure Plate";
        }
        if (path.equals("pumpkin")) {
            return "Place a Pumpkin";
        }
        if (path.equals("redstone_lamp")) {
            return "Place a Redstone Lamp";
        }
        if (path.equals("repeater")) {
            return "Place a Redstone Repeater";
        }
        if (path.equals("redstone_torch") || path.equals("redstone_wall_torch")) {
            return "Place a Redstone Torch";
        }
        if (path.contains("sculk_sensor")) {
            return "Place a Sculk Sensor";
        }
        if (path.equals("tripwire_hook")) {
            return "Place a Tripwire Hook";
        }
        if (path.equals("jack_o_lantern")) {
            return "Place a Jack o'Lantern";
        }
        if (path.equals("end_stone")) {
            return "Place End Stone";
        }
        if (path.contains("purpur")) {
            return "Place Purpur";
        }
        if (path.contains("copper")) {
            return "Place a Copper Block";
        }
        if (path.contains("brick")) {
            return "Place Bricks";
        }
        if (path.equals("powder_snow")) {
            return "Place Powder Snow";
        }
        if (path.equals("light")) {
            return "Place a Light Block";
        }
        if (path.equals("barrier") || path.contains("command_block") || path.equals("structure_block") || path.equals("jigsaw")) {
            return "Place a Creative-Only Block";
        }
        if (path.endsWith("sand") || path.endsWith("gravel") || path.equals("anvil")) {
            return "Place a Gravity-Affected Block";
        }
        if (path.equals("iron_block") || path.equals("gold_block") || path.equals("diamond_block") || path.equals("emerald_block") || path.equals("netherite_block")) {
            return "Place a Valuable Block";
        }
        if (path.contains("redstone") || path.equals("lever") || path.endsWith("button") || path.endsWith("rail")) {
            return "Place a Redstone Component";
        }
        if (path.endsWith("_bed")) {
            return "Place a Bed";
        }
        if (path.equals("chest")) {
            return "Place a Chest";
        }
        if (path.equals("trapped_chest")) {
            return "Place a Trapped Chest";
        }
        if (path.equals("crafting_table")) {
            return "Place a Crafting Table";
        }
        if (path.equals("furnace")) {
            return "Place a Furnace";
        }
        if (path.equals("bookshelf")) {
            return "Place a Bookshelf";
        }
        if (path.equals("jukebox")) {
            return "Place a Jukebox";
        }
        if (path.equals("armor_stand")) {
            return "Place an Armor Stand";
        }
        if (path.equals("beacon")) {
            return "Place a Beacon";
        }
        if (ContextualDialogueController.isWorkstation(path)) {
            return "Place a Workstation";
        }
        if (path.endsWith("_log") || path.endsWith("_wood") || path.endsWith("_planks")) {
            return "Place Wood";
        }
        if (path.contains("dirt")) {
            return "Place Dirt";
        }
        if (path.contains("leaves") || path.contains("sapling") || path.contains("flower")) {
            return "Place Leaves or Plants";
        }
        if (path.contains("wool")) {
            return "Place Wool";
        }
        if (path.contains("glass")) {
            return "Place Glass";
        }
        if (path.contains("concrete_powder")) {
            return "Place Concrete Powder";
        }
        if (path.contains("concrete")) {
            return "Place Concrete";
        }
        if (path.contains("glazed_terracotta")) {
            return "Place Glazed Terracotta";
        }
        if (path.contains("terracotta")) {
            return "Place Terracotta";
        }
        if (path.equals("iron_block")) {
            return "Place an Iron Block";
        }
        if (path.equals("gold_block")) {
            return "Place a Gold Block";
        }
        if (path.equals("diamond_block")) {
            return "Place a Diamond Block";
        }
        if (path.equals("emerald_block")) {
            return "Place an Emerald Block";
        }
        if (path.equals("lapis_block")) {
            return "Place a Lapis Block";
        }
        if (path.contains("ice")) {
            return "Place Ice";
        }
        if (path.contains("snow")) {
            return "Place Snow";
        }
        if (path.endsWith("_button")) {
            return "Place a Button";
        }
        if (path.equals("lever")) {
            return "Place a Lever";
        }
        return "Place a Block";
    }

    private static String selectUseBlockContext(BlockState state) {
        String path = BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath();
        if (path.endsWith("_button")) {
            return "Press a Button";
        }
        if (path.equals("bell")) {
            return "Hear a Bell Ring";
        }
        if (path.equals("lever")) {
            return "Flip a Lever";
        }
        if (path.endsWith("_door")) {
            return "Use a Door";
        }
        if (path.endsWith("_fence_gate")) {
            return state.hasProperty((Property)BlockStateProperties.OPEN) && (Boolean)state.getValue((Property)BlockStateProperties.OPEN) != false ? "Close a Fence Gate" : "Open a Fence Gate";
        }
        if (path.equals("crafter")) {
            return "Use a Crafter";
        }
        if (path.equals("dispenser")) {
            return "Use a Dispenser";
        }
        if (path.equals("dropper")) {
            return "Use a Dropper";
        }
        if (path.equals("jukebox")) {
            return "Use a Jukebox";
        }
        if (path.equals("loom")) {
            return "Use a Loom";
        }
        if (path.contains("shulker_box")) {
            return "Use a Shulker Box";
        }
        if (path.equals("stonecutter")) {
            return "Use a Stonecutter";
        }
        if (path.equals("beacon")) {
            return "Use a Beacon";
        }
        if (path.contains("campfire")) {
            return "Use a Campfire";
        }
        if (path.equals("cartography_table")) {
            return "Use a Cartography Table";
        }
        if (path.equals("cauldron") || path.endsWith("_cauldron")) {
            return "Use a Cauldron";
        }
        if (path.equals("chiseled_bookshelf")) {
            return "Use a Chiseled Bookshelf";
        }
        if (path.equals("composter")) {
            return "Use a Composter";
        }
        if (path.equals("ender_chest")) {
            return "Use an Ender Chest";
        }
        if (path.contains("shelf")) {
            return "Use Shelves";
        }
        if (path.contains("chest")) {
            return "Open a Chest";
        }
        if (path.equals("crafting_table")) {
            return "Use a Crafting Table";
        }
        if (path.equals("furnace") || path.equals("blast_furnace") || path.equals("smoker")) {
            return "Use a Furnace";
        }
        if (path.equals("anvil") || path.endsWith("_anvil")) {
            return "Use an Anvil";
        }
        if (path.equals("enchanting_table")) {
            return "Use an Enchanting Table";
        }
        if (path.equals("brewing_stand")) {
            return "Use a Brewing Stand";
        }
        if (path.equals("grindstone")) {
            return "Use a Grindstone";
        }
        if (path.equals("smithing_table")) {
            return "Use a Smithing Table";
        }
        return null;
    }

    private static String selectHeldBlockContext(ItemStack stack, BlockState clicked) {
        String item = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
        String block = BuiltInRegistries.BLOCK.getKey(clicked.getBlock()).getPath();
        if (item.equals("redstone")) {
            return "Place Redstone Dust";
        }
        if ((item.equals("flint_and_steel") || item.equals("fire_charge")) && block.equals("tnt")) {
            return "Light TNT";
        }
        if (block.equals("tnt")) {
            return "See TNT";
        }
        if ((item.equals("flint_and_steel") || item.equals("fire_charge")) && block.contains("campfire")) {
            return "Light a Campfire";
        }
        if ((item.equals("flint_and_steel") || item.equals("fire_charge")) && block.contains("candle")) {
            return "Light a Candle";
        }
        if (block.contains("campfire") && (item.contains("beef") || item.contains("porkchop") || item.contains("chicken") || item.contains("mutton") || item.contains("rabbit") || item.equals("potato"))) {
            return "Cook Food on a Campfire";
        }
        if ((item.equals("water_bucket") || item.endsWith("_shovel")) && block.contains("campfire")) {
            return "Extinguish a Campfire";
        }
        if (item.equals("water_bucket") && block.contains("candle")) {
            return "Extinguish a Candle";
        }
        if (item.equals("shears") && block.equals("pumpkin")) {
            return "Carve a Pumpkin";
        }
        return null;
    }

    private static String selectUseItemContext(ItemStack stack) {
        String path = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
        if (path.equals("firework_rocket")) {
            return "Set Off a Firework";
        }
        if (path.equals("ender_pearl")) {
            return "Teleport with an Ender Pearl";
        }
        if (path.equals("snowball")) {
            return "Snowball";
        }
        if (path.contains("apple") || path.contains("bread") || path.contains("carrot") || path.contains("potato") || path.contains("beef") || path.contains("porkchop") || path.contains("chicken") || path.contains("mutton") || path.contains("rabbit") || path.contains("stew") || path.contains("berries") || path.contains("melon")) {
            return "Eat Food";
        }
        return null;
    }

    private static boolean isWorkstation(String path) {
        return path.equals("composter") || path.equals("barrel") || path.equals("blast_furnace") || path.equals("smoker") || path.equals("cartography_table") || path.equals("brewing_stand") || path.equals("fletching_table") || path.equals("cauldron") || path.equals("lectern") || path.equals("stonecutter") || path.equals("loom") || path.equals("smithing_table") || path.equals("grindstone");
    }

    private static boolean playObserved(ServerLevel level, Player player, Vec3 eventPosition, String title, long cooldown) {
        DialogueCatalog.DialogueGroup group = DialogueCatalog.byTitle(title, "villager");
        if (group == null) {
            return false;
        }
        boolean requiresSight = title.contains("Stare") || title.contains("Approach") || title.contains("Nudge") || title.contains("Wear");
        List<Villager> candidates = ContextualDialogueController.nearbyVillagers(level, eventPosition, 16.0).stream()
            .filter(villager -> !villager.isBaby() && !villager.isSleeping())
            .filter(villager -> ContextualDialogueController.cast(villager) == CastProfile.VILLAGER)
            .filter(villager -> !requiresSight || (player != null && villager.hasLineOfSight((Entity)player)))
            .sorted(Comparator.comparingDouble(villager -> villager.distanceToSqr(eventPosition)))
            .toList();
        for (Villager candidate : candidates) {
            if (ContextualDialogueController.play((LivingEntity)candidate, group, "observed:" + candidate.getUUID() + ":" + title, cooldown, (Entity)player, null, true)) {
                return true;
            }
        }
        return false;
    }

    private static List<Villager> nearbyVillagers(ServerLevel level, Vec3 position, double range) {
        AABB area = AABB.ofSize((Vec3)position, (double)(range * 2.0), (double)range, (double)(range * 2.0));
        return level.getEntitiesOfClass(Villager.class, area, entity -> entity.isAlive() && !entity.entityTags().contains(DIALOGUE_TEST_TAG));
    }

    private static boolean playTitle(LivingEntity speaker, String title, String cooldownKey, long cooldown) {
        DialogueCatalog.DialogueGroup group = DialogueCatalog.byTitle(title, ContextualDialogueController.speakerType(speaker));
        return group != null && ContextualDialogueController.play(speaker, group, cooldownKey, cooldown, null, null);
    }

    private static boolean playTitle(LivingEntity speaker, String title, String cooldownKey, long cooldown, Entity target) {
        DialogueCatalog.DialogueGroup group = DialogueCatalog.byTitle(title, ContextualDialogueController.speakerType(speaker));
        return group != null && ContextualDialogueController.play(speaker, group, cooldownKey, cooldown, target, null);
    }

    private static boolean playSharedTitle(LivingEntity speaker, String title, String cooldownKey, long cooldown, Entity target) {
        DialogueCatalog.DialogueGroup group = DialogueCatalog.byTitle(title, "villager");
        return group != null && ContextualDialogueController.play(speaker, group, cooldownKey, cooldown, target, null, true);
    }

    private static boolean playId(LivingEntity speaker, String id, String cooldownKey, long cooldown) {
        DialogueCatalog.DialogueGroup group = DialogueCatalog.byId(id);
        return group != null && ContextualDialogueController.play(speaker, group, cooldownKey, cooldown, null, null);
    }

    private static boolean playId(LivingEntity speaker, String id, String cooldownKey, long cooldown, Entity target) {
        DialogueCatalog.DialogueGroup group = DialogueCatalog.byId(id);
        return group != null && ContextualDialogueController.play(speaker, group, cooldownKey, cooldown, target, null);
    }

    private static boolean playSharedId(LivingEntity speaker, String id, String cooldownKey, long cooldown, Entity target) {
        DialogueCatalog.DialogueGroup group = DialogueCatalog.byId(id);
        return group != null && ContextualDialogueController.play(speaker, group, cooldownKey, cooldown, target, null, true);
    }

    private static boolean playId(LivingEntity speaker, String id, String cooldownKey, long cooldown, Vec3 target) {
        DialogueCatalog.DialogueGroup group = DialogueCatalog.byId(id);
        return group != null && ContextualDialogueController.play(speaker, group, cooldownKey, cooldown, null, target);
    }

    private static boolean play(LivingEntity speaker, DialogueCatalog.DialogueGroup group, String cooldownKey, long cooldown, Entity target, Vec3 targetPosition) {
        return ContextualDialogueController.play(speaker, group, cooldownKey, cooldown, target, targetPosition, false);
    }

    private static boolean play(LivingEntity speaker, DialogueCatalog.DialogueGroup group, String cooldownKey, long cooldown, Entity target, Vec3 targetPosition, boolean sharedAdult) {
        ServerLevel level;
        block10: {
            block9: {
                Villager sleepingVillager;
                Villager conditionVillager;
                Villager villager;
                boolean sharedVillagerVoice = speaker instanceof Villager && !(villager = (Villager)speaker).isBaby() && ContextualDialogueController.cast(villager) == CastProfile.VILLAGER || speaker instanceof WanderingTrader;
                boolean validSpeaker = ContextualDialogueController.matchesSpeaker(speaker, group) || sharedAdult && sharedVillagerVoice && group.speaker().equals("villager");
                boolean blockedByCondition = speaker instanceof Villager && !ContextualDialogueController.canSpeakDuringCondition(conditionVillager = (Villager)speaker, group.id());
                Level level2 = speaker.level();
                if (!(level2 instanceof ServerLevel)) break block9;
                level = (ServerLevel)level2;
                if (validSpeaker && level.getServer().tickRateManager().runsNormally() && VillagerNewsSettings.dialogueEnabled() && !blockedByCondition && (!(speaker instanceof Villager) || !(sleepingVillager = (Villager)speaker).isSleeping() || group.id().equals("asqzby")) && !ContextualDialogueController.isBusy(speaker) && ContextualDialogueController.ready(cooldownKey, VillagerNewsSettings.scaleCooldown(cooldown))) break block10;
            }
            return false;
        }
        List recentVariants = SHARED_RECENT_VARIANTS.getOrDefault(group.id(), List.of());
        DialogueCatalog.DialogueVariant variant = group.chooseVariant(VillagerNewsSettings.rareVoicelines(), Set.copyOf(recentVariants));
        if (variant == null) {
            return false;
        }
        DialogueAnimationNetwork.send(level, speaker, group.id(), variant.index(), (int)variant.durationTicks());
        ACTIVE_SOUNDS.put(speaker.getUUID(), new ActiveSound(group.id(), ticks + variant.durationTicks()));
        COOLDOWNS.put(cooldownKey, ticks);
        int maximumWeight = group.variants().stream().mapToInt(DialogueCatalog.DialogueVariant::weight).max().orElse(1);
        int eligibleVariants = VillagerNewsSettings.rareVoicelines() == 0 ? (int)group.variants().stream().filter(candidate -> (double)candidate.weight() >= (double)maximumWeight * 0.8).count() : group.variants().size();
        int historySize = Math.min(8, eligibleVariants - 1);
        if (historySize > 0) {
            ArrayList<Integer> updatedHistory = new ArrayList<Integer>(recentVariants);
            updatedHistory.remove((Object)variant.index());
            updatedHistory.add(variant.index());
            while (updatedHistory.size() > historySize) {
                updatedHistory.removeFirst();
            }
            SHARED_RECENT_VARIANTS.put(group.id(), updatedHistory);
        }
        ContextualDialogueController.markBusy(speaker, variant.durationTicks() + 10L);
        if (speaker instanceof Mob) {
            Villager villager;
            Mob mob = (Mob)speaker;
            Vec3 position = target != null ? target.getEyePosition() : targetPosition;
            boolean lockMovement = !MOBILE_DIALOGUES.contains(group.id()) && (!(speaker instanceof Villager) || ContextualDialogueController.cast(villager = (Villager)speaker) != CastProfile.UNREACHABLE || !group.id().equals("eltxge"));
            SPEECH_TARGETS.put(speaker.getUUID(), new SpeechTarget(target == null ? null : target.getUUID(), position, ticks + variant.durationTicks(), lockMovement));
            if (lockMovement) {
                ContextualDialogueController.holdMob(mob, position);
            } else {
                ContextualDialogueController.faceMob(mob, position);
            }
        }
        return true;
    }

    private static boolean matchesSpeaker(LivingEntity speaker, DialogueCatalog.DialogueGroup group) {
        if (speaker instanceof Villager) {
            Villager villager = (Villager)speaker;
            if (villager.isBaby()) {
                return BABY_DIALOGUES.contains(group.id());
            }
            if (BABY_DIALOGUES.contains(group.id())) {
                return false;
            }
            if (COSMETIC_RECIPIENT_DIALOGUES.contains(group.id())) {
                return true;
            }
        }
        return group.speaker().equals(ContextualDialogueController.speakerType(speaker));
    }

    public static boolean requiresBabySpeaker(String groupId) {
        return BABY_DIALOGUES.contains(groupId);
    }

    public static long playTestDialogue(ServerLevel level, LivingEntity speaker, DialogueCatalog.DialogueGroup group, int variantIndex, Entity target) {
        ContextualDialogueController.interrupt(speaker);
        DialogueCatalog.DialogueVariant variant = group.variants().stream().filter(candidate -> candidate.index() == variantIndex).findFirst().orElse(null);
        if (variant == null) {
            return 0L;
        }
        long duration = variant.durationTicks();
        DialogueAnimationNetwork.send(level, speaker, group.id(), variant.index(), (int)duration);
        ACTIVE_SOUNDS.put(speaker.getUUID(), new ActiveSound(group.id(), ticks + duration));
        ContextualDialogueController.markBusy(speaker, duration + 10L);
        if (speaker instanceof Mob) {
            Mob mob = (Mob)speaker;
            Vec3 position = target == null ? null : target.getEyePosition();
            SPEECH_TARGETS.put(speaker.getUUID(), new SpeechTarget(target == null ? null : target.getUUID(), position, ticks + duration, true));
            ContextualDialogueController.holdMob(mob, position);
        }
        return duration;
    }

    public static void stopTestDialogue(LivingEntity speaker) {
        ContextualDialogueController.interrupt(speaker);
    }

    public static void onTradeCompleted(LivingEntity trader, Player player) {
        Object wanderingTrader;
        Villager villager;
        TradeSession session;
        if (player != null && (session = ACTIVE_TRADES.get(player.getUUID())) != null && session.traderId.equals(trader.getUUID())) {
            session.completed = true;
        }
        String id = null;
        if (trader instanceof Villager && ContextualDialogueController.cast(villager = (Villager)trader) == CastProfile.VILLAGER) {
            id = "xmkwxd";
        } else if (trader instanceof WanderingTrader) {
            wanderingTrader = (WanderingTrader)trader;
            id = "bvrbhy";
        }
        if (id != null && (wanderingTrader = trader.level()) instanceof ServerLevel) {
            ServerLevel level = (ServerLevel)wanderingTrader;
            long due = Math.max(ticks + 1L, BUSY_UNTIL.getOrDefault(trader.getUUID(), ticks) + 1L);
            PENDING_SPEECH.add(new PendingSpeech(level, trader.getUUID(), id, player == null ? null : player.getUUID(), due));
        }
    }

    private static boolean ready(String key, long cooldown) {
        return ticks - COOLDOWNS.getOrDefault(key, -4611686018427387904L) >= cooldown;
    }

    private static boolean isBusy(LivingEntity entity) {
        ActiveSound sound = ACTIVE_SOUNDS.get(entity.getUUID());
        return BUSY_UNTIL.getOrDefault(entity.getUUID(), 0L) > ticks || sound != null && sound.endTick > ticks;
    }

    private static boolean isPlaying(LivingEntity entity, String groupId) {
        ActiveSound sound = ACTIVE_SOUNDS.get(entity.getUUID());
        return sound != null && sound.endTick > ticks && sound.groupId.equals(groupId);
    }

    private static boolean hasDamageLock(LivingEntity entity) {
        ActiveSound sound = ACTIVE_SOUNDS.get(entity.getUUID());
        return sound != null && sound.endTick > ticks && DAMAGE_LOCK_DIALOGUES.contains(sound.groupId);
    }

    private static void markBusy(LivingEntity entity, long duration) {
        BUSY_UNTIL.put(entity.getUUID(), ticks + duration);
    }

    private static void holdListener(LivingEntity listener, Entity speaker, long duration) {
        ContextualDialogueController.markBusy(listener, duration);
        if (listener instanceof Mob) {
            Mob mob = (Mob)listener;
            Vec3 position = speaker.getEyePosition();
            SPEECH_TARGETS.put(listener.getUUID(), new SpeechTarget(speaker.getUUID(), position, ticks + duration, true));
            ContextualDialogueController.holdMob(mob, position);
        }
    }

    private static void holdMob(Mob mob, Vec3 position) {
        mob.getNavigation().stop();
        mob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        mob.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        if (mob.onGround() && !mob.isPassenger()) {
            Vec3 movement = mob.getDeltaMovement();
            mob.setDeltaMovement(0.0, movement.y, 0.0);
        }
        ContextualDialogueController.faceMob(mob, position);
    }

    private static void faceMob(Mob mob, Vec3 position) {
        if (position == null) {
            return;
        }
        mob.getLookControl().setLookAt(position.x, position.y, position.z, 15.0f, 10.0f);
        double x = position.x - mob.getX();
        double y = position.y - mob.getEyeY();
        double z = position.z - mob.getZ();
        double horizontal = Math.sqrt(x * x + z * z);
        if (horizontal > 0.01) {
            float targetYaw = (float)(Mth.atan2((double)z, (double)x) * 180.0 / Math.PI) - 90.0f;
            float headOffset = Mth.clamp((float)Mth.wrapDegrees((float)(targetYaw - mob.yBodyRot)), (float)-65.0f, (float)65.0f);
            float bodyYaw = ContextualDialogueController.easedRotation(mob.yBodyRot, targetYaw - headOffset, 12.0f, 0.35f);
            float headYaw = ContextualDialogueController.easedRotation(mob.getYHeadRot(), targetYaw, 15.0f, 0.4f);
            mob.setYHeadRot(headYaw);
        }
        float targetPitch = (float)(-(Mth.atan2((double)y, (double)horizontal) * 180.0 / Math.PI));
        mob.setXRot(ContextualDialogueController.easedRotation(mob.getXRot(), Mth.clamp((float)targetPitch, (float)-90.0f, (float)90.0f), 10.0f, 0.3f));
    }

    private static float easedRotation(float current, float target, float maximumStep, float proportion) {
        float distance = Math.abs(Mth.wrapDegrees((float)(target - current)));
        return Mth.approachDegrees((float)current, (float)target, (float)Mth.clamp((float)(distance * proportion), (float)0.2f, (float)maximumStep));
    }

    private static void interrupt(LivingEntity speaker) {
        Level level = speaker.level();
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel level2 = (ServerLevel)level;
        ACTIVE_SOUNDS.remove(speaker.getUUID());
        DialogueAnimationNetwork.stop(level2, speaker);
        BUSY_UNTIL.remove(speaker.getUUID());
        SPEECH_TARGETS.remove(speaker.getUUID());
    }

    private static boolean isWooly(Sheep sheep) {
        String name = sheep.getName().getString().toLowerCase(Locale.ROOT);
        return name.equals("wooly") || name.equals("wooly the sheep");
    }

    private static void normalizeSpecialEntity(Entity entity) {
        String prefix;
        Component customName = entity.getCustomName();
        if (customName == null) {
            return;
        }
        String name = customName.getString();
        if (!name.startsWith(prefix = "{\"text\":\"") || !name.endsWith("\"}")) {
            return;
        }
        String decoded = name.substring(prefix.length(), name.length() - 2);
        if (SPECIAL_NAMES.contains(decoded)) {
            entity.setCustomName((Component)Component.literal((String)decoded));
        }
    }

    private static VillagerNewsData data(Villager villager) {
        return (VillagerNewsData)villager;
    }

    private static void ensureSpecialTrade(Villager villager) {
        CastProfile profile = ContextualDialogueController.cast(villager);
        Item result = switch (profile.ordinal()) {
            case 1 -> VillagerNewsItems.MAYOR_HAT;
            case 2 -> VillagerNewsItems.TESTIFICATE_MAN_HELMET;
            case 3 -> VillagerNewsItems.MOUSTACHE;
            case 4 -> VillagerNewsItems.MICROPHONE;
            default -> null;
        };
        VillagerNewsData state = ContextualDialogueController.data(villager);
        if (result == null) {
            if (state.vnap$hasOriginalVillagerState()) {
                state.vnap$restoreOriginalVillagerState();
            }
            return;
        }
        state.vnap$captureOriginalVillagerState();
        if (!villager.getVillagerData().profession().is(VillagerProfession.NONE)) {
            villager.setVillagerData(villager.getVillagerData().withProfession((HolderGetter.Provider)villager.level().registryAccess(), VillagerProfession.NONE).withLevel(1));
        }
        villager.getOffers().removeIf(offer -> offer.getResult().getItem() != result);
        if (villager.getOffers().stream().anyMatch(offer -> offer.getResult().getItem() == result)) {
            return;
        }
        int price = profile == CastProfile.MAYOR ? 24 : 16;
        villager.getOffers().add(new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, price), new ItemStack((ItemLike)result), 16, 2, 0.1f));
    }

    public static boolean isSpecialTrader(Villager villager) {
        return switch (ContextualDialogueController.cast(villager).ordinal()) {
            case 1, 2, 3, 4 -> true;
            default -> false;
        };
    }

    private static boolean tryCreateNaturalSpecial(Villager villager, ServerLevel level) {
        if (!VillagerNewsSettings.spawnSpecialVillagers() || villager.spawnReason() != EntitySpawnReason.STRUCTURE) {
            return false;
        }
        BlockPos spawn = level.getRespawnData().pos();
        if (villager.distanceToSqr(Vec3.atCenterOf((Vec3i)spawn)) <= 1000000.0) {
            return false;
        }
        ServerScoreboard scoreboard = level.getServer().getScoreboard();
        Objective spawned = ContextualDialogueController.objective((Scoreboard)scoreboard, SPECIAL_OBJECTIVE);
        Objective xPosition = ContextualDialogueController.objective((Scoreboard)scoreboard, SPECIAL_X_OBJECTIVE);
        Objective zPosition = ContextualDialogueController.objective((Scoreboard)scoreboard, SPECIAL_Z_OBJECTIVE);
        ArrayList<String> available = new ArrayList<String>();
        for (String key : NATURAL_SPECIAL_NAMES.keySet()) {
            double dz;
            ScoreHolder holder = ScoreHolder.forNameOnly((String)("$vnap_" + key));
            if (scoreboard.getOrCreatePlayerScore(holder, spawned).get() == 0) {
                available.add(key);
                continue;
            }
            int x = scoreboard.getOrCreatePlayerScore(holder, xPosition).get();
            int z = scoreboard.getOrCreatePlayerScore(holder, zPosition).get();
            double dx = villager.getX() - (double)x;
            if (!(dx * dx + (dz = villager.getZ() - (double)z) * dz <= 22500.0)) continue;
            return false;
        }
        if (available.isEmpty()) {
            return false;
        }
        String key = (String)available.get(ThreadLocalRandom.current().nextInt(available.size()));
        if (key.equals("wooly")) {
            Sheep sheep = (Sheep)EntityTypes.SHEEP.create((Level)level, EntitySpawnReason.STRUCTURE);
            if (sheep == null) {
                return false;
            }
            sheep.copyPosition((Entity)villager);
            sheep.setCustomName((Component)Component.literal((String)NATURAL_SPECIAL_NAMES.get(key)));
            sheep.setPersistenceRequired();
            sheep.setColor(DyeColor.WHITE);
            sheep.addTag(NATURAL_SPECIAL_TAG);
            if (!level.addFreshEntity((Entity)sheep)) {
                return false;
            }
            villager.discard();
        } else {
            villager.setCustomName((Component)Component.literal((String)NATURAL_SPECIAL_NAMES.get(key)));
            villager.setPersistenceRequired();
            villager.addTag(NATURAL_SPECIAL_TAG);
        }
        ScoreHolder holder = ScoreHolder.forNameOnly((String)("$vnap_" + key));
        scoreboard.getOrCreatePlayerScore(holder, spawned).set(1);
        scoreboard.getOrCreatePlayerScore(holder, xPosition).set(villager.blockPosition().getX());
        scoreboard.getOrCreatePlayerScore(holder, zPosition).set(villager.blockPosition().getZ());
        return key.equals("wooly");
    }

    private static void clearNaturalSpecial(ServerLevel level, Entity entity) {
        String key = ContextualDialogueController.naturalSpecialKey(entity);
        if (key == null) {
            return;
        }
        ServerScoreboard scoreboard = level.getServer().getScoreboard();
        ScoreHolder holder = ScoreHolder.forNameOnly((String)("$vnap_" + key));
        scoreboard.getOrCreatePlayerScore(holder, ContextualDialogueController.objective((Scoreboard)scoreboard, SPECIAL_OBJECTIVE)).set(0);
    }

    private static String naturalSpecialKey(Entity entity) {
        Sheep sheep;
        if (entity instanceof Sheep && ContextualDialogueController.isWooly(sheep = (Sheep)entity)) {
            return "wooly";
        }
        if (!(entity instanceof Villager)) {
            return null;
        }
        Villager villager = (Villager)entity;
        return switch (ContextualDialogueController.cast(villager).ordinal()) {
            case 1 -> "mayor";
            case 2 -> "testificate";
            case 3 -> "number_5";
            case 4 -> "number_9";
            case 5 -> "unreachable";
            default -> null;
        };
    }

    private static Objective objective(Scoreboard scoreboard, String name) {
        Objective existing = scoreboard.getObjective(name);
        return existing == null ? scoreboard.addObjective(name, ObjectiveCriteria.DUMMY, (Component)Component.literal((String)name), ObjectiveCriteria.RenderType.INTEGER, false, null) : existing;
    }

    private static String orderedPair(UUID first, UUID second) {
        return first.compareTo(second) < 0 ? String.valueOf(first) + ":" + String.valueOf(second) : String.valueOf(second) + ":" + String.valueOf(first);
    }

    private static String meetDialogue(CastProfile profile) {
        return switch (profile.ordinal()) {
            case 1 -> "lyatyf";
            case 3 -> "kmvqxe";
            case 4 -> "sifqsj";
            case 2 -> "zvamyb";
            default -> null;
        };
    }

    private static CastProfile cast(Villager villager) {
        String name = villager.getName().getString().toLowerCase(Locale.ROOT);
        if (name.equals("mayor") || name.equals("the mayor") || name.equals("mayor villager")) {
            return CastProfile.MAYOR;
        }
        if (name.equals("testificate man")) {
            return CastProfile.TESTIFICATE_MAN;
        }
        if (name.equals("villager #5") || name.equals("villager number 5")) {
            return CastProfile.NUMBER_5;
        }
        if (name.equals("villager #9") || name.equals("villager number 9")) {
            return CastProfile.NUMBER_9;
        }
        if (name.equals("villager unreachable") || name.equals("can't catch me!")) {
            return CastProfile.UNREACHABLE;
        }
        return CastProfile.VILLAGER;
    }

    private static String speakerType(LivingEntity speaker) {
        Sheep sheep;
        if (speaker instanceof Villager) {
            Villager villager = (Villager)speaker;
            return switch (ContextualDialogueController.cast(villager).ordinal()) {
                default -> throw new MatchException(null, null);
                case 0 -> "villager";
                case 1 -> "mayor";
                case 2 -> "testificate_man";
                case 3 -> "number_5";
                case 4 -> "number_9";
                case 5 -> "unreachable";
            };
        }
        if (speaker instanceof WanderingTrader) {
            return "wandering_trader";
        }
        if (speaker instanceof Sheep && ContextualDialogueController.isWooly(sheep = (Sheep)speaker)) {
            return "wooly";
        }
        return "";
    }

    private static enum CastProfile {
        VILLAGER("xfpjxq", "lvigit", "clbjww", "wyvzhk", "vevdkl"),
        MAYOR("dpwhhs", "xxehbq", "njyapy", "ssbhiv", "ltdnvy"),
        TESTIFICATE_MAN("nmwmrz", "luoibc", "mpbnsm", "fzoqwd", "fzoqwd"),
        NUMBER_5("xccwah", "legnsy", "sclaoa", "behifz", "behifz"),
        NUMBER_9("kzogzi", "ezgbfw", "snnkrl", "wrbvvp", "asuufu"),
        UNREACHABLE("eltxge", "eltxge", "eltxge", "wyvzhk", "vevdkl");

        private final String approach;
        private final String idle;
        private final String trade;
        private final String hurt;
        private final String attack;

        private CastProfile(String approach, String idle, String trade, String hurt, String attack) {
            this.approach = approach;
            this.idle = idle;
            this.trade = trade;
            this.hurt = hurt;
            this.attack = attack;
        }
    }

    private static final class UnreachableState {
        private long stateEnteredTick;
        private long nextPathTick;
        private boolean coolingDown;

        private UnreachableState(long tick) {
            this.stateEnteredTick = tick;
            this.nextPathTick = tick;
        }

        private void updateFleeing(long tick) {
            if (this.coolingDown && tick - this.stateEnteredTick > 240L) {
                this.coolingDown = false;
                this.stateEnteredTick = tick;
            }
        }

        private void stopFleeing(long tick) {
            if (this.coolingDown) {
                this.coolingDown = false;
                this.stateEnteredTick = tick;
            }
        }

        private boolean canTaunt(long tick) {
            return !this.coolingDown && tick - this.stateEnteredTick > 120L;
        }

        private void taunted(long tick) {
            this.coolingDown = true;
            this.stateEnteredTick = tick;
        }
    }

    private record VillagerSnapshot(boolean baby, String profession, int level, boolean working, String name, boolean poisoned, boolean slowed, boolean weakened, boolean suffocating) {
    }

    private record PendingSpeech(ServerLevel level, UUID speakerId, String dialogueId, UUID targetId, long dueTick, boolean sharedAdult) {
        private PendingSpeech(ServerLevel level, UUID speakerId, String dialogueId, UUID targetId, long dueTick) {
            this(level, speakerId, dialogueId, targetId, dueTick, false);
        }
    }

    private static final class PlayerObservation {
        private Vec3 lastPosition = Vec3.ZERO;
        private int stillTicks;
        private int stareTicks;
        private int breakStreak;
        private long lastBreakTick = -4611686018427387904L;
        private String lastGameMode;
        private String lastPlayerContext;
        private BlockPos lastGroundPos = BlockPos.ZERO;
        private String lastGroundBlock = "";

        private PlayerObservation() {
        }
    }

    private record PendingBell(ServerLevel level, Vec3 position, long dueTick) {
    }

    private record ActiveSound(String groupId, long endTick) {
    }

    private static final class PendingSleep {
        private final ServerLevel level;
        private final BlockPos bedPos;
        private long dueTick;
        private boolean bedtimeStarted;

        private PendingSleep(ServerLevel level, BlockPos bedPos, long dueTick, boolean bedtimeStarted) {
            this.level = level;
            this.bedPos = bedPos;
            this.dueTick = dueTick;
            this.bedtimeStarted = bedtimeStarted;
        }
    }

    private record PendingConditionRelief(ServerLevel level, UUID villagerId, String dialogueId, long dueTick) {
    }

    private static final class TradeSession {
        private final ServerLevel level;
        private final UUID traderId;
        private final long createdTick;
        private boolean opened;
        private boolean completed;

        private TradeSession(ServerLevel level, UUID traderId, long createdTick) {
            this.level = level;
            this.traderId = traderId;
            this.createdTick = createdTick;
        }
    }

    private record SpeechTarget(UUID targetId, Vec3 position, long untilTick, boolean lockMovement) {
    }

    private record PendingBellReaction(ServerLevel level, UUID villagerId, Vec3 position, long dueTick, long expireTick) {
    }

    private record PendingWake(ServerLevel level, UUID villagerId, String dialogueId, UUID targetId, long dueTick) {
    }
}

