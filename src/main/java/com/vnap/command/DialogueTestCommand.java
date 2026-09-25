/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.IntegerArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
 *  net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
 *  net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.core.HolderGetter$Provider
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.Identifier
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.server.permissions.PermissionCheck
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntitySpawnReason
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EntityTypes
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LightningBolt
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.TamableAnimal
 *  net.minecraft.world.entity.animal.bee.Bee
 *  net.minecraft.world.entity.item.PrimedTnt
 *  net.minecraft.world.entity.npc.villager.Villager
 *  net.minecraft.world.entity.npc.villager.VillagerProfession
 *  net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.DyeColor
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.phys.Vec3
 */
package com.vnap.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.vnap.dialogue.ContextualDialogueController;
import com.vnap.dialogue.DialogueCatalog;
import com.vnap.entity.VillagerNewsData;
import com.vnap.item.VillagerNewsItems;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.PermissionCheck;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class DialogueTestCommand {
    private static final Map<UUID, TestSession> SESSIONS = new LinkedHashMap<UUID, TestSession>();
    private static final Map<UUID, TestRun> TEST_RUNS = new LinkedHashMap<UUID, TestRun>();
    private static final long CLIENT_TRACKING_DELAY = 5L;
    private static final long CONTINUOUS_GAP = 20L;
    private static final Map<String, String> SUBJECT_TYPES = Map.ofEntries(Map.entry("Allay", "allay"), Map.entry("Angry Bee", "bee"), Map.entry("Baby Bee", "bee"), Map.entry("Baby Cat", "cat"), Map.entry("Baby Chicken", "chicken"), Map.entry("Baby Cow", "cow"), Map.entry("Baby Drowned", "drowned"), Map.entry("Baby Horse", "horse"), Map.entry("Baby Husk", "husk"), Map.entry("Baby Panda", "panda"), Map.entry("Baby Pig", "pig"), Map.entry("Baby Sheep", "sheep"), Map.entry("Baby Wolf", "wolf"), Map.entry("Baby Zombie", "zombie"), Map.entry("Baby Zombie Piglin", "zombified_piglin"), Map.entry("Baby Zombie Villager", "zombie_villager"), Map.entry("Bat", "bat"), Map.entry("Bee", "bee"), Map.entry("Bogged", "bogged"), Map.entry("Camel", "camel"), Map.entry("Cat", "cat"), Map.entry("Chicken", "chicken"), Map.entry("Copper Golem", "copper_golem"), Map.entry("Cow", "cow"), Map.entry("Creaking", "creaking"), Map.entry("Creeper", "creeper"), Map.entry("Dolphin", "dolphin"), Map.entry("Drowned", "drowned"), Map.entry("Ender Dragon", "ender_dragon"), Map.entry("Enderman", "enderman"), Map.entry("Evoker", "evoker"), Map.entry("Fish", "cod"), Map.entry("Frog", "frog"), Map.entry("Happy Ghast", "happy_ghast"), Map.entry("Horse", "horse"), Map.entry("Husk", "husk"), Map.entry("Iron Golem", "iron_golem"), Map.entry("Jockey", "chicken"), Map.entry("Llama", "llama"), Map.entry("Panda", "panda"), Map.entry("Parrot", "parrot"), Map.entry("Phantom", "phantom"), Map.entry("Pig", "pig"), Map.entry("Pillager", "pillager"), Map.entry("Polar Bear", "polar_bear"), Map.entry("Rabbit", "rabbit"), Map.entry("Ravager", "ravager"), Map.entry("Sheared Sheep", "sheep"), Map.entry("Sheep", "sheep"), Map.entry("Skeleton", "skeleton"), Map.entry("Slime", "slime"), Map.entry("Sniffer", "sniffer"), Map.entry("Snow Golem", "snow_golem"), Map.entry("Spider", "spider"), Map.entry("Stray", "stray"), Map.entry("Sulfur Cube", "magma_cube"), Map.entry("Tamed Baby Wolf", "wolf"), Map.entry("Tamed Wolf", "wolf"), Map.entry("Turtle", "turtle"), Map.entry("Vex", "vex"), Map.entry("Vindicator", "vindicator"), Map.entry("Warden", "warden"), Map.entry("Witch", "witch"), Map.entry("Wither", "wither"), Map.entry("Wolf", "wolf"), Map.entry("Zoglin", "zoglin"), Map.entry("Zombie", "zombie"), Map.entry("Zombie Piglin", "zombified_piglin"), Map.entry("Zombie Villager", "zombie_villager"), Map.entry("See an Iron Golem", "iron_golem"), Map.entry("Uses a Potion with One Llama", "trader_llama"), Map.entry("Uses a Potion with Two Llamas", "trader_llama"));
    private static long ticks;

    private DialogueTestCommand() {
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, selection) -> dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal((String)"dialoguetest").requires((Predicate)Commands.hasPermission((PermissionCheck)Commands.LEVEL_GAMEMASTERS))).then(Commands.literal((String)"continuous").executes(context -> DialogueTestCommand.runContinuous((CommandSourceStack)context.getSource())))).then(Commands.argument((String)"group", (ArgumentType)IntegerArgumentType.integer((int)1, (int)DialogueCatalog.groups().size())).executes(context -> DialogueTestCommand.runSingle((CommandSourceStack)context.getSource(), IntegerArgumentType.getInteger((CommandContext)context, (String)"group"))))));
        ServerTickEvents.END_SERVER_TICK.register(DialogueTestCommand::tick);
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> DialogueTestCommand.clear());
    }

    private static int runSingle(CommandSourceStack source, int number) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        DialogueTestCommand.cancel(player.getUUID());
        TEST_RUNS.put(player.getUUID(), new TestRun(number, 0, ticks + 1L, false));
        return 1;
    }

    private static int runContinuous(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        DialogueTestCommand.cancel(player.getUUID());
        TEST_RUNS.put(player.getUUID(), new TestRun(1, 0, ticks + 1L, true));
        player.sendSystemMessage((Component)Component.literal((String)("Starting continuous dialogue test: " + DialogueCatalog.groups().size() + " groups")));
        return 1;
    }

    private static boolean startTest(ServerPlayer player, int number, int variantOffset, boolean continuous) {
        ArrayList<DialogueCatalog.DialogueGroup> groups = new ArrayList<DialogueCatalog.DialogueGroup>(DialogueCatalog.groups().values());
        if (number < 1 || number > groups.size()) {
            player.sendSystemMessage((Component)Component.literal((String)("Dialogue group must be between 1 and " + groups.size())));
            return false;
        }
        DialogueTestCommand.removeSession(player.getUUID());
        DialogueCatalog.DialogueGroup group = (DialogueCatalog.DialogueGroup)groups.get(number - 1);
        if (variantOffset < 0 || variantOffset >= group.variants().size()) {
            player.sendSystemMessage((Component)Component.literal((String)("Dialogue group " + number + " has no variant " + (variantOffset + 1))));
            return false;
        }
        DialogueCatalog.DialogueVariant variant = group.variants().get(variantOffset);
        ServerLevel level = player.level();
        String title = DialogueTestCommand.scenarioTitle(group);
        Vec3 forward = DialogueTestCommand.horizontalDirection(player);
        Vec3 side = new Vec3(-forward.z, 0.0, forward.x);
        Vec3 center = player.position().add(forward.scale(2.5));
        Vec3 speakerPosition = center.add(side.scale(0.8));
        Vec3 subjectPosition = center.subtract(side.scale(0.8));
        ArrayList<Entity> spawned = new ArrayList<Entity>();
        LivingEntity speaker = DialogueTestCommand.createSpeaker(level, group);
        if (speaker == null) {
            DialogueTestCommand.announceFailure(player, number, groups.size(), group, title, "could not create speaker");
            return false;
        }
        DialogueTestCommand.prepareEntity((Entity)speaker, speakerPosition);
        DialogueTestCommand.configureSpeaker(level, speaker, group, title);
        if (!level.addFreshEntity((Entity)speaker)) {
            DialogueTestCommand.announceFailure(player, number, groups.size(), group, title, "could not summon speaker");
            return false;
        }
        spawned.add((Entity)speaker);
        SubjectSpec subjectSpec = DialogueTestCommand.subjectSpec(group, title);
        Entity subject = DialogueTestCommand.createSubject(level, player, group, title, subjectSpec, subjectPosition, spawned);
        if (subjectSpec != null && subject == null) {
            for (Entity entity : spawned) {
                if (entity.isRemoved()) continue;
                entity.discard();
            }
            DialogueTestCommand.announceFailure(player, number, groups.size(), group, title, "could not summon subject");
            return false;
        }
        DialogueTestCommand.configureScene(level, speaker, subject, group, title, center, spawned);
        SESSIONS.put(player.getUUID(), new TestSession(speaker, (Entity)(subject == null ? player : subject), spawned, group, variant, ticks + 5L, number, variantOffset, continuous));
        player.sendSystemMessage((Component)Component.literal((String)("[Dialogue " + number + "/" + groups.size() + ", variant " + (variantOffset + 1) + "/" + group.variants().size() + "] " + group.id() + " - " + title)));
        return true;
    }

    private static void tick(MinecraftServer server) {
        TestSession session;
        ++ticks;
        ArrayList<UUID> finished = new ArrayList<UUID>();
        for (Map.Entry<UUID, TestSession> entry : SESSIONS.entrySet()) {
            session = entry.getValue();
            if (!session.speaker.isAlive()) {
                finished.add(entry.getKey());
                continue;
            }
            if (!session.started && ticks >= session.startTick) {
                session.started = true;
                long duration = ContextualDialogueController.playTestDialogue((ServerLevel)session.speaker.level(), session.speaker, session.group(), session.variant.index(), session.subject);
                session.endTick = ticks + Math.max(1L, duration);
            }
            if (!session.started || ticks < session.endTick) continue;
            finished.add(entry.getKey());
        }
        for (UUID owner : finished) {
            session = SESSIONS.get(owner);
            DialogueTestCommand.removeSession(owner);
            if (session == null) continue;
            DialogueTestCommand.scheduleNext(server, owner, session.number, session.variantOffset, ticks + 20L);
        }
        ArrayList<ScheduledTest> due = new ArrayList<ScheduledTest>();
        for (Map.Entry<UUID, TestRun> entry : TEST_RUNS.entrySet()) {
            TestRun run = entry.getValue();
            if (SESSIONS.containsKey(entry.getKey()) || ticks < run.nextTick) continue;
            due.add(new ScheduledTest(entry.getKey(), run.number, run.variantOffset));
            run.nextTick = Long.MAX_VALUE;
        }
        for (ScheduledTest scheduled : due) {
            ServerPlayer player = server.getPlayerList().getPlayer(scheduled.owner());
            if (player == null) {
                TEST_RUNS.remove(scheduled.owner());
                continue;
            }
            TestRun run = TEST_RUNS.get(scheduled.owner());
            if (run == null || DialogueTestCommand.startTest(player, scheduled.number(), scheduled.variantOffset(), run.continuous)) continue;
            DialogueTestCommand.scheduleNext(server, scheduled.owner(), scheduled.number(), scheduled.variantOffset(), ticks + 20L);
        }
    }

    private static void scheduleNext(MinecraftServer server, UUID owner, int number, int variantOffset, long nextTick) {
        TestRun run = TEST_RUNS.get(owner);
        if (run == null) {
            return;
        }
        ArrayList<DialogueCatalog.DialogueGroup> groups = new ArrayList<DialogueCatalog.DialogueGroup>(DialogueCatalog.groups().values());
        DialogueCatalog.DialogueGroup group = (DialogueCatalog.DialogueGroup)groups.get(number - 1);
        if (variantOffset + 1 < group.variants().size()) {
            run.number = number;
            run.variantOffset = variantOffset + 1;
            run.nextTick = nextTick;
            return;
        }
        if (!run.continuous || number >= groups.size()) {
            TEST_RUNS.remove(owner);
            ServerPlayer player = server.getPlayerList().getPlayer(owner);
            if (player != null) {
                String message = run.continuous ? "Continuous dialogue test complete: " + groups.size() + "/" + groups.size() : "Dialogue test complete: group " + number + ", " + group.variants().size() + " variants";
                player.sendSystemMessage((Component)Component.literal((String)message));
            }
            return;
        }
        run.number = number + 1;
        run.variantOffset = 0;
        run.nextTick = nextTick;
    }

    private static void announceFailure(ServerPlayer player, int number, int total, DialogueCatalog.DialogueGroup group, String title, String reason) {
        player.sendSystemMessage((Component)Component.literal((String)("[Dialogue " + number + "/" + total + "] " + group.id() + " - " + title + " (failed: " + reason + ")")));
    }

    private static LivingEntity createSpeaker(ServerLevel level, DialogueCatalog.DialogueGroup group) {
        if (DialogueTestCommand.isCosmeticRecipientDialogue(group.id())) {
            return (LivingEntity)EntityTypes.VILLAGER.create((Level)level, EntitySpawnReason.COMMAND);
        }
        return switch (group.speaker()) {
            case "wooly" -> (Sheep)EntityTypes.SHEEP.create((Level)level, EntitySpawnReason.COMMAND);
            case "wandering_trader" -> (WanderingTrader)EntityTypes.WANDERING_TRADER.create((Level)level, EntitySpawnReason.COMMAND);
            default -> (Villager)EntityTypes.VILLAGER.create((Level)level, EntitySpawnReason.COMMAND);
        };
    }

    private static void configureSpeaker(ServerLevel level, LivingEntity speaker, DialogueCatalog.DialogueGroup group, String title) {
        if (speaker instanceof Villager) {
            String name;
            Villager villager = (Villager)speaker;
            if (DialogueTestCommand.isCosmeticRecipientDialogue(group.id())) {
                v0 = null;
            } else {
                switch (group.speaker()) {
                    case "mayor": {
                        v0 = "The Mayor";
                        break;
                    }
                    case "testificate_man": {
                        v0 = "Testificate Man";
                        break;
                    }
                    case "number_5": {
                        v0 = "Villager #5";
                        break;
                    }
                    case "number_9": {
                        v0 = "Villager #9";
                        break;
                    }
                    case "unreachable": {
                        v0 = "Villager Unreachable";
                        break;
                    }
                    default: {
                        v0 = name = null;
                    }
                }
            }
            if (name != null) {
                villager.setCustomName((Component)Component.literal(name));
            }
            if (group.id().equals("qmpcxi")) {
                villager.setCustomName((Component)Component.literal((String)"Dinnerbone"));
            }
            if (group.id().equals("armupg")) {
                villager.setCustomName((Component)Component.literal((String)"Jeb"));
            }
            if (group.id().equals("cmrqhw")) {
                villager.setCustomName((Component)Component.literal((String)"Dragon"));
            }
            if (ContextualDialogueController.requiresBabySpeaker(group.id())) {
                villager.setBaby(true);
            }
            VillagerNewsData data = (VillagerNewsData)villager;
            data.vnap$setHasNose(!DialogueTestCommand.speakerHasNoNose(group.id(), title));
            data.vnap$setCosmetic(DialogueTestCommand.speakerCosmetic(group.id(), title));
            data.vnap$setSignType(group.id().equals("vqlrqf") ? 0 : -1);
            data.vnap$setSignMessage(group.id().equals("vqlrqf") ? 0 : -1);
            DialogueTestCommand.setProfession(level, villager, group.speaker().equals("villager") ? title : "");
            if (group.id().equals("adhvqz")) {
                villager.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)VillagerNewsItems.MICROPHONE));
            }
        }
        if (speaker instanceof Sheep) {
            Sheep sheep = (Sheep)speaker;
            sheep.setCustomName((Component)Component.literal((String)"Wooly The Sheep"));
            sheep.setColor(DyeColor.WHITE);
            sheep.setSheared(group.id().equals("jqaekk"));
        }
        if (speaker instanceof WanderingTrader) {
            WanderingTrader trader = (WanderingTrader)speaker;
            if (group.id().equals("dbzjqi")) {
                trader.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 12000, 0, false, false));
            }
        }
        if (group.id().equals("onindz")) {
            speaker.addEffect(new MobEffectInstance(MobEffects.POISON, 12000));
        }
        if (group.id().equals("xemyaj")) {
            speaker.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 12000));
        }
        if (group.id().equals("yebifs")) {
            speaker.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 12000));
        }
        if (group.id().equals("etkxko")) {
            speaker.setRemainingFireTicks(12000);
        }
        if (group.id().equals("igebly")) {
            speaker.setTicksFrozen(speaker.getTicksRequiredToFreeze());
        }
    }

    private static Entity createSubject(ServerLevel level, ServerPlayer player, DialogueCatalog.DialogueGroup group, String title, SubjectSpec spec, Vec3 position, List<Entity> spawned) {
        if (spec == null) {
            return null;
        }
        Entity entity = DialogueTestCommand.createEntity(level, spec.type());
        if (entity == null) {
            return null;
        }
        DialogueTestCommand.prepareEntity(entity, position);
        if (entity instanceof Mob) {
            Mob mob = (Mob)entity;
            if (spec.baby()) {
                mob.setBaby(true);
            }
        }
        if (entity instanceof TamableAnimal) {
            TamableAnimal tamable = (TamableAnimal)entity;
            if (title.startsWith("Tamed ")) {
                tamable.tame((Player)player);
            }
        }
        if (entity instanceof Bee) {
            Bee bee = (Bee)entity;
            if (title.equals("Angry Bee")) {
                bee.setTarget((LivingEntity)player);
                bee.startPersistentAngerTimer();
            }
        }
        if (entity instanceof Sheep) {
            Sheep sheep = (Sheep)entity;
            sheep.setColor(DyeColor.WHITE);
            sheep.setSheared(spec.sheared());
        }
        if (entity instanceof Villager) {
            Villager villager = (Villager)entity;
            DialogueTestCommand.configureSubjectVillager(level, villager, group, title, spec);
        }
        if (!level.addFreshEntity(entity)) {
            return null;
        }
        spawned.add(entity);
        return entity;
    }

    private static SubjectSpec subjectSpec(DialogueCatalog.DialogueGroup group, String title) {
        String id = group.id();
        if (DialogueTestCommand.isConversation(id) || title.equals("Share Food with Another Villager") || title.equals("Receive Food from Another Villager") || title.equals("Have a Baby") || title.equals("See a Baby Villager") || title.equals("See Another Villager Die") || title.equals("See a Villager Wearing a Cosmetic") || title.equals("Villager Wears Testificate Man's Helmet") || DialogueTestCommand.specialSubjectName(title) != null) {
            boolean baby = title.equals("Share Food with Another Villager") || title.equals("Have a Baby") || title.equals("See a Baby Villager") || title.startsWith("Give a Baby ");
            return new SubjectSpec("villager", baby, false, DialogueTestCommand.specialSubjectName(title));
        }
        String type = SUBJECT_TYPES.get(title);
        if (type != null) {
            return new SubjectSpec(type, title.startsWith("Baby ") || title.equals("Tamed Baby Wolf"), title.equals("Sheared Sheep"), null);
        }
        if (id.equals("ckniqq")) {
            return new SubjectSpec("armor_stand", false, false, null);
        }
        if (id.equals("dfdkli") || id.equals("zeykfp")) {
            return new SubjectSpec("firework_rocket", false, false, null);
        }
        if (id.equals("ikrwzy")) {
            return new SubjectSpec("lightning_bolt", false, false, null);
        }
        if (id.equals("pmaqgq")) {
            return new SubjectSpec("tnt", false, false, null);
        }
        if (id.equals("cvltyw")) {
            return new SubjectSpec("experience_orb", false, false, null);
        }
        if (id.equals("bodvsv") || id.equals("yzqpvi")) {
            return new SubjectSpec("falling_block", false, false, null);
        }
        if (title.equals("See Another Entity Get Hurt")) {
            return new SubjectSpec("cow", false, false, null);
        }
        return null;
    }

    private static void configureSubjectVillager(ServerLevel level, Villager villager, DialogueCatalog.DialogueGroup group, String title, SubjectSpec spec) {
        if (spec.name() != null) {
            villager.setCustomName((Component)Component.literal((String)spec.name()));
        }
        VillagerNewsData data = (VillagerNewsData)villager;
        boolean noNose = title.equals("Two Villagers Without Noses") || title.equals("One Villager Is Missing a Nose") && !group.id().equals("bygaxwbayahw") && !group.id().equals("bygaxwfobzlt");
        data.vnap$setHasNose(!noNose);
        int cosmetic = switch (group.id()) {
            case "inirxg", "riezum" -> 3;
            case "ozxzla", "rlkdqd" -> 4;
            case "wurmgu", "cxeziv", "pbbywc" -> 2;
            case "anrhns" -> 1;
            default -> 0;
        };
        data.vnap$setCosmetic(cosmetic);
        DialogueTestCommand.setProfession(level, villager, "");
    }

    private static void configureScene(ServerLevel level, LivingEntity speaker, Entity subject, DialogueCatalog.DialogueGroup group, String title, Vec3 center, List<Entity> spawned) {
        Entity golem;
        Entity secondLlama;
        Entity minecart;
        Entity boat;
        if ((title.equals("Two Villagers in One Boat") || title.equals("Sit in a Boat") || title.equals("Boat on Land") || title.equals("Boat on Water") || title.equals("Nudge a Villager in a Boat")) && (boat = DialogueTestCommand.createEntity(level, "oak_boat")) != null) {
            DialogueTestCommand.prepareEntity(boat, center);
            if (level.addFreshEntity(boat)) {
                spawned.add(boat);
                speaker.startRiding(boat);
                if (title.equals("Two Villagers in One Boat") && subject != null) {
                    subject.startRiding(boat);
                }
            }
        }
        if (title.contains("Minecart") && (minecart = DialogueTestCommand.createEntity(level, "minecart")) != null) {
            DialogueTestCommand.prepareEntity(minecart, center);
            if (level.addFreshEntity(minecart)) {
                spawned.add(minecart);
                speaker.startRiding(minecart);
            }
        }
        if (group.id().equals("myajyt") && (secondLlama = DialogueTestCommand.createEntity(level, "trader_llama")) != null) {
            DialogueTestCommand.prepareEntity(secondLlama, center.add(0.0, 0.0, 1.6));
            if (level.addFreshEntity(secondLlama)) {
                spawned.add(secondLlama);
            }
        }
        if (group.id().equals("qffeco") && (golem = DialogueTestCommand.createEntity(level, "iron_golem")) != null) {
            DialogueTestCommand.prepareEntity(golem, center.add(0.0, 0.0, 1.6));
            if (level.addFreshEntity(golem)) {
                spawned.add(golem);
            }
        }
        if (title.equals("Jockey") && subject != null) {
            Entity rider = DialogueTestCommand.createEntity(level, "zombie");
            if (rider instanceof Mob) {
                Mob mob = (Mob)rider;
                mob.setBaby(true);
            }
            if (rider != null) {
                DialogueTestCommand.prepareEntity(rider, center);
                if (level.addFreshEntity(rider)) {
                    spawned.add(rider);
                    rider.startRiding(subject);
                }
            }
        }
        if (group.id().equals("asqzby")) {
            speaker.startSleeping(speaker.blockPosition());
        }
    }

    private static void setProfession(ServerLevel level, Villager villager, String title) {
        ResourceKey profession;
        switch (title) {
            case "Armorer at Work": {
                ResourceKey resourceKey = VillagerProfession.ARMORER;
                break;
            }
            case "Butcher at Work": {
                ResourceKey resourceKey = VillagerProfession.BUTCHER;
                break;
            }
            case "Cartographer at Work": {
                ResourceKey resourceKey = VillagerProfession.CARTOGRAPHER;
                break;
            }
            case "Cleric at Work": {
                ResourceKey resourceKey = VillagerProfession.CLERIC;
                break;
            }
            case "Farmer at Work": 
            case "Farming": 
            case "Harvest Crops Near a Farmer": {
                ResourceKey resourceKey = VillagerProfession.FARMER;
                break;
            }
            case "Fisherman at Work": {
                ResourceKey resourceKey = VillagerProfession.FISHERMAN;
                break;
            }
            case "Fletcher at Work": {
                ResourceKey resourceKey = VillagerProfession.FLETCHER;
                break;
            }
            case "Leatherworker at Work": {
                ResourceKey resourceKey = VillagerProfession.LEATHERWORKER;
                break;
            }
            case "Librarian at Work": 
            case "Inspect Bookshelves": {
                ResourceKey resourceKey = VillagerProfession.LIBRARIAN;
                break;
            }
            case "Mason at Work": {
                ResourceKey resourceKey = VillagerProfession.MASON;
                break;
            }
            case "Shepherd at Work": {
                ResourceKey resourceKey = VillagerProfession.SHEPHERD;
                break;
            }
            case "Toolsmith at Work": {
                ResourceKey resourceKey = VillagerProfession.TOOLSMITH;
                break;
            }
            case "Weaponsmith at Work": {
                ResourceKey resourceKey = VillagerProfession.WEAPONSMITH;
                break;
            }
            case "Nitwit Wandering": 
            case "Try to Trade with a Nitwit": {
                ResourceKey resourceKey = VillagerProfession.NITWIT;
                break;
            }
            default: {
                ResourceKey resourceKey = profession = title.contains("Trad") || title.contains("Workstation") || title.equals("Level Up") || title.equals("Reach Master Level") || title.equals("Start Work") || title.equals("Get a Job") ? VillagerProfession.FARMER : VillagerProfession.NONE;
            }
        }
        int levelNumber = title.equals("Reach Master Level") ? 5 : (title.equals("Level Up") ? 2 : 1);
        villager.setVillagerData(villager.getVillagerData().withProfession((HolderGetter.Provider)level.registryAccess(), profession).withLevel(levelNumber));
    }

    private static boolean speakerHasNoNose(String id, String title) {
        return id.equals("jktrnd") || id.equals("dcvgnm") || id.equals("bygaxwbayahw") || id.equals("bygaxwfobzlt") || title.equals("Two Villagers Without Noses");
    }

    private static int speakerCosmetic(String id, String title) {
        if (id.equals("svdjdk") || id.equals("orogba")) {
            return 1;
        }
        if (id.equals("wurmgu") || id.equals("cxeziv")) {
            return 2;
        }
        if (id.equals("inirxg") || id.equals("riezum")) {
            return 3;
        }
        if (id.equals("ozxzla") || id.equals("rlkdqd")) {
            return 4;
        }
        if (title.equals("Give a Villager a Sign")) {
            return 0;
        }
        return 0;
    }

    private static boolean isCosmeticRecipientDialogue(String id) {
        return id.equals("wurmgu") || id.equals("inirxg") || id.equals("ozxzla") || id.equals("cxeziv") || id.equals("riezum") || id.equals("rlkdqd");
    }

    private static String specialSubjectName(String title) {
        return switch (title) {
            case "Meet Testificate Man" -> "Testificate Man";
            case "Meet the Mayor" -> "The Mayor";
            case "Meet Villager #5" -> "Villager #5";
            case "Meet Villager #9" -> "Villager #9";
            default -> null;
        };
    }

    private static boolean isConversation(String id) {
        return id.startsWith("gmrypk") || id.startsWith("bygaxw") || id.startsWith("loicsw") || id.startsWith("wrjbdd") || id.startsWith("wrswgi") || id.startsWith("slbqfw") || id.equals("zqfvby");
    }

    private static String scenarioTitle(DialogueCatalog.DialogueGroup group) {
        if (!group.title().isBlank()) {
            return group.title();
        }
        String id = group.id();
        if (id.startsWith("gmrypk")) {
            return "Two Villagers Wander Together";
        }
        if (id.startsWith("bygaxw")) {
            return "One Villager Is Missing a Nose";
        }
        if (id.startsWith("loicsw")) {
            return "Two Villagers Without Noses";
        }
        if (id.startsWith("wrjbdd")) {
            return "Villagers Gossip";
        }
        if (id.startsWith("wrswgi")) {
            return "Two Villagers at a Campfire";
        }
        if (id.startsWith("slbqfw")) {
            return "Attack a Villager at Home with Witnesses";
        }
        return group.id();
    }

    private static Entity createEntity(ServerLevel level, String path) {
        EntityType type = BuiltInRegistries.ENTITY_TYPE.getOptional(Identifier.fromNamespaceAndPath((String)"minecraft", (String)path)).orElse(null);
        return type == null ? null : type.create((Level)level, EntitySpawnReason.COMMAND);
    }

    private static void prepareEntity(Entity entity, Vec3 position) {
        entity.addTag("vnap_dialogue_test");
        entity.setInvulnerable(true);
        entity.setSilent(true);
        entity.setNoGravity(!(entity instanceof LivingEntity));
        entity.snapTo(position);
        if (entity instanceof Mob) {
            Mob mob = (Mob)entity;
            mob.setNoAi(true);
            mob.setPersistenceRequired();
        }
        if (entity instanceof LightningBolt) {
            LightningBolt lightning = (LightningBolt)entity;
            lightning.setVisualOnly(true);
        }
        if (entity instanceof PrimedTnt) {
            PrimedTnt tnt = (PrimedTnt)entity;
            tnt.setFuse(12000);
        }
    }

    private static Vec3 horizontalDirection(ServerPlayer player) {
        Vec3 look = player.getLookAngle();
        Vec3 horizontal = new Vec3(look.x, 0.0, look.z);
        return horizontal.lengthSqr() < 1.0E-4 ? new Vec3(0.0, 0.0, 1.0) : horizontal.normalize();
    }

    private static void removeSession(UUID owner) {
        TestSession session = SESSIONS.remove(owner);
        if (session == null) {
            return;
        }
        if (!session.speaker.isRemoved()) {
            ContextualDialogueController.stopTestDialogue(session.speaker);
        }
        for (Entity entity : session.spawned) {
            if (entity.isRemoved()) continue;
            entity.discard();
        }
    }

    private static void cancel(UUID owner) {
        DialogueTestCommand.removeSession(owner);
        TEST_RUNS.remove(owner);
    }

    private static void clear() {
        for (UUID owner : List.copyOf(SESSIONS.keySet())) {
            DialogueTestCommand.removeSession(owner);
        }
        TEST_RUNS.clear();
        ticks = 0L;
    }

    private static final class TestRun {
        private int number;
        private int variantOffset;
        private long nextTick;
        private final boolean continuous;

        private TestRun(int number, int variantOffset, long nextTick, boolean continuous) {
            this.number = number;
            this.variantOffset = variantOffset;
            this.nextTick = nextTick;
            this.continuous = continuous;
        }
    }

    private record SubjectSpec(String type, boolean baby, boolean sheared, String name) {
    }

    private static final class TestSession {
        private final LivingEntity speaker;
        private final Entity subject;
        private final List<Entity> spawned;
        private final long startTick;
        private final DialogueCatalog.DialogueGroup dialogueGroup;
        private final DialogueCatalog.DialogueVariant variant;
        private final int number;
        private final int variantOffset;
        private final boolean continuous;
        private boolean started;
        private long endTick = Long.MAX_VALUE;

        private TestSession(LivingEntity speaker, Entity subject, List<Entity> spawned, DialogueCatalog.DialogueGroup dialogueGroup, DialogueCatalog.DialogueVariant variant, long startTick, int number, int variantOffset, boolean continuous) {
            this.speaker = speaker;
            this.subject = subject;
            this.spawned = List.copyOf(spawned);
            this.startTick = startTick;
            this.dialogueGroup = dialogueGroup;
            this.variant = variant;
            this.number = number;
            this.variantOffset = variantOffset;
            this.continuous = continuous;
        }

        private DialogueCatalog.DialogueGroup group() {
            return this.dialogueGroup;
        }
    }

    private record ScheduledTest(UUID owner, int number, int variantOffset) {
    }
}

