/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.resources.sounds.EntityBoundSoundInstance
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.Entity
 */
package com.vnap.client;

import com.vnap.dialogue.DialogueCatalog;
import com.vnap.network.DialogueAnimationPayload;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

public final class DialogueSoundState {
    private static final Map<UUID, ActiveSound> ACTIVE = new HashMap<UUID, ActiveSound>();
    private static final Map<UUID, PendingSound> PENDING = new HashMap<UUID, PendingSound>();
    private static final long PENDING_TIMEOUT_NANOS = 5000000000L;

    private DialogueSoundState() {
    }

    public static void start(DialogueAnimationPayload payload) {
        Minecraft minecraft = Minecraft.getInstance();
        DialogueSoundState.stop(minecraft, payload.entityId());
        PENDING.remove(payload.entityId());
        if (payload.groupId().isEmpty() || minecraft.level == null) {
            return;
        }
        if (!DialogueSoundState.tryStart(minecraft, payload)) {
            PENDING.put(payload.entityId(), new PendingSound(payload, System.nanoTime() + 5000000000L));
        }
    }

    private static boolean tryStart(Minecraft minecraft, DialogueAnimationPayload payload) {
        DialogueCatalog.DialogueGroup group = DialogueCatalog.byId(payload.groupId());
        if (group == null) {
            return true;
        }
        DialogueCatalog.DialogueVariant variant = group.variants().stream().filter(candidate -> candidate.index() == payload.variantIndex()).findFirst().orElse(null);
        Entity entity = minecraft.level.getEntity(payload.entityId());
        if (variant == null) {
            return true;
        }
        if (entity == null) {
            return false;
        }
        boolean followsEntity = entity.isAlive() && !entity.isSilent() && !payload.groupId().equals("hivgme") && !payload.groupId().equals("ecslqo");
        EntityBoundSoundInstance sound = followsEntity ? new EntityBoundSoundInstance(variant.sound(), SoundSource.NEUTRAL, 1.0f, 1.0f, entity, entity.getRandom().nextLong()) : new SimpleSoundInstance(variant.sound(), SoundSource.NEUTRAL, 1.0f, 1.0f, RandomSource.create(), entity.getX(), entity.getY(), entity.getZ());
        minecraft.getSoundManager().play((SoundInstance)sound);
        ACTIVE.put(payload.entityId(), new ActiveSound((SoundInstance)sound, followsEntity, System.nanoTime() + (long)payload.durationTicks() * 50000000L));
        return true;
    }

    public static void tick(Minecraft minecraft) {
        if (minecraft.level == null || minecraft.player == null) {
            DialogueSoundState.clear(minecraft);
            return;
        }
        long now = System.nanoTime();
        Iterator<Map.Entry<UUID, PendingSound>> pendingIterator = PENDING.entrySet().iterator();
        while (pendingIterator.hasNext()) {
            PendingSound pending = pendingIterator.next().getValue();
            if (now < pending.expiresAtNanos() && !DialogueSoundState.tryStart(minecraft, pending.payload())) continue;
            pendingIterator.remove();
        }
        Iterator<Map.Entry<UUID, ActiveSound>> iterator = ACTIVE.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, ActiveSound> entry = iterator.next();
            Entity entity = minecraft.level.getEntity(entry.getKey());
            if (now < entry.getValue().endNanos() && (!entry.getValue().followsEntity() || entity != null && entity.isAlive())) continue;
            minecraft.getSoundManager().stop(entry.getValue().instance());
            iterator.remove();
        }
    }

    public static void clear(Minecraft minecraft) {
        PENDING.clear();
        for (UUID id : (UUID[])ACTIVE.keySet().toArray(UUID[]::new)) {
            DialogueSoundState.stop(minecraft, id);
        }
    }

    private static void stop(Minecraft minecraft, UUID id) {
        ActiveSound active = ACTIVE.remove(id);
        if (active != null) {
            minecraft.getSoundManager().stop(active.instance());
        }
    }

    private record PendingSound(DialogueAnimationPayload payload, long expiresAtNanos) {
    }

    private record ActiveSound(SoundInstance instance, boolean followsEntity, long endNanos) {
    }
}

