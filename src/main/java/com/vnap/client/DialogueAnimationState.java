/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.npc.villager.Villager
 *  net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader
 *  traben.entity_model_features.EMFAnimationApi
 *  traben.entity_model_features.utils.EMFEntity
 */
package com.vnap.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vnap.client.RainbowNoseRenderState;
import com.vnap.entity.VillagerNewsData;
import com.vnap.network.DialogueAnimationPayload;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import traben.entity_model_features.EMFAnimationApi;
import traben.entity_model_features.utils.EMFEntity;

public final class DialogueAnimationState {
    private static final String DATA_PATH = "/assets/villager-news-addon-port/dialogue_animations.json";
    private static final String[] TARGETS = new String[]{"root", "waist", "body", "head", "head_inner", "arms", "left_leg_root", "left_leg", "right_leg_root", "right_leg", "brow", "eye_group", "lower_face", "pupil_left", "pupil_right", "eye_left", "eye_right", "nose"};
    private static final String[] COMPONENTS = new String[]{"rx", "ry", "rz", "tx", "ty", "tz", "sx", "sy", "sz"};
    private static final Map<String, List<VariantTimeline>> TIMELINES = new HashMap<String, List<VariantTimeline>>();
    private static final List<Gesture> GESTURES = new ArrayList<Gesture>();
    private static final List<Gesture> IDLES = new ArrayList<Gesture>();
    private static final VariantTimeline EMPTY_TIMELINE = new VariantTimeline(List.of(), List.of());
    private static final Map<UUID, ActiveDialogue> ACTIVE = new ConcurrentHashMap<UUID, ActiveDialogue>();
    private static final Map<UUID, IdleState> IDLE_STATES = new ConcurrentHashMap<UUID, IdleState>();
    private static final Map<UUID, LookState> LOOK_STATES = new ConcurrentHashMap<UUID, LookState>();
    private static final Map<UUID, TurnState> TURN_STATES = new ConcurrentHashMap<UUID, TurnState>();
    private static final Map<UUID, LocomotionState> LOCOMOTION_STATES = new ConcurrentHashMap<UUID, LocomotionState>();
    private static final float BLEND_SECONDS = 0.3f;
    private static final float IDLE_BLEND_SECONDS = 0.24f;
    private static final float MOUTH_BLEND_SECONDS = 0.15f;
    private static final float TURN_SECONDS = 0.5f;
    private static final float LOCOMOTION_BLEND_SECONDS = 0.2f;
    private static final float RUN_ENTER_SPEED = 0.6f;
    private static final float RUN_EXIT_SPEED = 0.3f;
    private static Gesture locomotion = new Gesture(0.0f, Map.of());
    private static Gesture runLocomotion = new Gesture(0.0f, Map.of());
    private static float framesPerSecond = 24.0f;

    private DialogueAnimationState() {
    }

    static void load() throws IOException {
        try (InputStream stream = DialogueAnimationState.class.getResourceAsStream(DATA_PATH);){
            if (stream == null) {
                throw new IOException("Missing /assets/villager-news-addon-port/dialogue_animations.json");
            }
            JsonObject root = JsonParser.parseReader((Reader)new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
            framesPerSecond = root.get("framesPerSecond").getAsFloat();
            for (JsonElement gestureElement : root.getAsJsonArray("gestures")) {
                GESTURES.add(DialogueAnimationState.readGesture(gestureElement.getAsJsonObject()));
            }
            locomotion = DialogueAnimationState.readGesture(root.getAsJsonObject("locomotion"));
            runLocomotion = DialogueAnimationState.readGesture(root.getAsJsonObject("runLocomotion"));
            for (JsonElement idleElement : root.getAsJsonArray("idles")) {
                IDLES.add(DialogueAnimationState.readGesture(idleElement.getAsJsonObject()));
            }
            for (Map.Entry group : root.getAsJsonObject("groups").entrySet()) {
                ArrayList<VariantTimeline> variants = new ArrayList<VariantTimeline>();
                for (JsonElement variantElement : ((JsonElement)group.getValue()).getAsJsonArray()) {
                    JsonObject variant = variantElement.getAsJsonObject();
                    ArrayList<MouthFrame> mouth = new ArrayList<MouthFrame>();
                    for (JsonElement frameElement : variant.getAsJsonArray("mouth")) {
                        JsonArray frame = frameElement.getAsJsonArray();
                        mouth.add(new MouthFrame(frame.get(0).getAsFloat(), frame.get(1).getAsFloat(), frame.get(2).getAsFloat(), frame.get(3).getAsFloat()));
                    }
                    ArrayList<GestureFrame> gestures = new ArrayList<GestureFrame>();
                    for (JsonElement frameElement : variant.getAsJsonArray("gestures")) {
                        JsonArray frame = frameElement.getAsJsonArray();
                        gestures.add(new GestureFrame(frame.get(0).getAsFloat(), frame.get(1).getAsInt()));
                    }
                    variants.add(new VariantTimeline(List.copyOf(mouth), List.copyOf(gestures)));
                }
                TIMELINES.put((String)group.getKey(), List.copyOf(variants));
            }
        }
    }

    private static Gesture readGesture(JsonObject value) {
        HashMap<String, float[]> tracks = new HashMap<String, float[]>();
        for (Map.Entry track : value.getAsJsonObject("tracks").entrySet()) {
            JsonArray samples = ((JsonElement)track.getValue()).getAsJsonArray();
            float[] values = new float[samples.size()];
            for (int index = 0; index < values.length; ++index) {
                values[index] = samples.get(index).getAsFloat();
            }
            tracks.put((String)track.getKey(), values);
        }
        return new Gesture(value.get("duration").getAsFloat(), Map.copyOf(tracks));
    }

    static List<String> animationVariables() {
        ArrayList<String> variables = new ArrayList<String>(TARGETS.length * COMPONENTS.length + 2);
        for (String target : TARGETS) {
            for (String component : COMPONENTS) {
                variables.add("vnap_" + target + "_" + component);
            }
        }
        variables.add("vnap_look_pitch");
        variables.add("vnap_look_yaw");
        return variables;
    }

    static void tick(Minecraft minecraft) {
        if (minecraft.level == null || minecraft.player == null) {
            DialogueAnimationState.clear();
            return;
        }
        long now = System.nanoTime();
        ACTIVE.entrySet().removeIf(entry -> now > ((ActiveDialogue)entry.getValue()).endNanos());
        IDLE_STATES.keySet().removeIf(id -> minecraft.level.getEntity(id) == null);
        LOOK_STATES.keySet().removeIf(id -> minecraft.level.getEntity(id) == null);
        TURN_STATES.keySet().removeIf(id -> minecraft.level.getEntity(id) == null);
        LOCOMOTION_STATES.keySet().removeIf(id -> minecraft.level.getEntity(id) == null);
    }

    static void clear() {
        ACTIVE.clear();
        IDLE_STATES.clear();
        LOOK_STATES.clear();
        TURN_STATES.clear();
        LOCOMOTION_STATES.clear();
    }

    static void start(DialogueAnimationPayload payload) {
        if (payload.groupId().isEmpty()) {
            ActiveDialogue previous = ACTIVE.get(payload.entityId());
            if (previous == null || previous.poseSnapshot().isEmpty()) {
                ACTIVE.remove(payload.entityId());
                return;
            }
            long now = System.nanoTime();
            ACTIVE.put(payload.entityId(), new ActiveDialogue(now, now, now + 300000000L, EMPTY_TIMELINE, previous.poseSnapshot(), false));
            return;
        }
        List<VariantTimeline> variants = TIMELINES.get(payload.groupId());
        if (variants == null || payload.variantIndex() < 0 || payload.variantIndex() >= variants.size()) {
            return;
        }
        long now = System.nanoTime();
        VariantTimeline timeline = variants.get(payload.variantIndex());
        float audioSeconds = (float)Math.max(1, payload.durationTicks()) / 20.0f;
        float totalSeconds = Math.max(audioSeconds + 0.15f, timeline.poseEndSeconds());
        ActiveDialogue previous = ACTIVE.get(payload.entityId());
        Map<String, Float> previousPose = previous == null ? Map.of() : previous.poseSnapshot();
        ACTIVE.put(payload.entityId(), new ActiveDialogue(now, now + (long)(audioSeconds * 1.0E9f), now + (long)(totalSeconds * 1.0E9f), timeline, previousPose, true));
    }

    static float speaking() {
        ActiveDialogue active = DialogueAnimationState.active();
        return active == null ? 0.0f : active.speechWeight();
    }

    static float mouthOpen() {
        MouthFrame frame = DialogueAnimationState.mouthFrame();
        return frame == null ? 0.0f : frame.open();
    }

    static float mouthWidth() {
        MouthFrame frame = DialogueAnimationState.mouthFrame();
        return frame == null ? 1.0f : frame.width();
    }

    static float mouthClosed() {
        MouthFrame frame = DialogueAnimationState.mouthFrame();
        return frame == null ? 1.0f : frame.closed();
    }

    static float hasNose() {
        Villager villager;
        EMFEntity entity = EMFAnimationApi.getCurrentEntity();
        boolean rainbow = entity instanceof Villager && "jeb_".equals((villager = (Villager)entity).getName().getString());
        RainbowNoseRenderState.update(rainbow, entity == null ? 0.0f : DialogueAnimationState.animationTick(entity), entity == null ? null : entity.etf$getUuid());
        return entity instanceof Villager && ((VillagerNewsData)(villager = (Villager)entity)).vnap$hasNose() ? 1.0f : 0.0f;
    }

    static float cosmetic(int cosmetic) {
        Villager villager;
        EMFEntity entity = EMFAnimationApi.getCurrentEntity();
        return entity instanceof Villager && ((VillagerNewsData)(villager = (Villager)entity)).vnap$cosmetic() == cosmetic ? 1.0f : 0.0f;
    }

    static float transform(String variableName) {
        ActiveDialogue active = DialogueAnimationState.active();
        if (variableName.equals("vnap_look_pitch") || variableName.equals("vnap_look_yaw")) {
            return DialogueAnimationState.look(variableName.endsWith("pitch"));
        }
        boolean scale = variableName.endsWith("_sx") || variableName.endsWith("_sy") || variableName.endsWith("_sz");
        float fallback = scale ? 1.0f : 0.0f;
        String trackName = variableName.substring("vnap_".length());
        float base = DialogueAnimationState.baseTransform(trackName, fallback, active);
        float dialogue = active == null ? fallback : active.timeline().transformAt(active.elapsedSeconds(), trackName, fallback);
        float result = scale ? base * dialogue : base + dialogue;
        return active == null ? result : active.transition(variableName, result);
    }

    public static void trackBodyRotation(Villager villager, float bodyRotation, float age) {
        TURN_STATES.computeIfAbsent(villager.getUUID(), ignored -> new TurnState()).update(age, bodyRotation, villager.isAlive() && !villager.isSleeping() && villager.onGround());
    }

    private static float baseTransform(String trackName, float fallback, ActiveDialogue active) {
        LivingEntity entity;
        EMFEntity emfEntity = EMFAnimationApi.getCurrentEntity();
        if (!(emfEntity instanceof LivingEntity) || !((entity = (LivingEntity)emfEntity) instanceof Villager) && !(entity instanceof WanderingTrader)) {
            return fallback;
        }
        UUID id = entity.getUUID();
        float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
        float age = emfEntity.emf$age() + partialTick;
        float speed = entity.walkAnimation.speed(partialTick);
        IdleState idle = IDLE_STATES.computeIfAbsent(id, ignored -> new IdleState());
        TurnState turn = TURN_STATES.computeIfAbsent(id, ignored -> new TurnState());
        LocomotionState locomotionState = LOCOMOTION_STATES.computeIfAbsent(id, ignored -> new LocomotionState());
        double horizontalDistanceSqr = entity.getDeltaMovement().horizontalDistanceSqr();
        boolean moving = speed > 0.01f && horizontalDistanceSqr > 1.0E-4;
        boolean groundedMovement = !entity.isSleeping() && entity.onGround() && moving;
        boolean canIdle = !entity.isSleeping() && entity.onGround() && !moving && !IDLES.isEmpty();
        idle.update(age, canIdle);
        locomotionState.update(age, speed, groundedMovement);
        if (!(entity instanceof Villager)) {
            turn.update(age, entity.yBodyRot, !entity.isSleeping() && entity.onGround());
        }
        float base = idle.valueAt(age, trackName, fallback);
        if (groundedMovement && locomotion.duration() > 0.0f) {
            float phase = entity.walkAnimation.position(partialTick) * 0.6662f / ((float)Math.PI * 2);
            float cycle = phase - (float)Math.floor(phase);
            float walkValue = locomotion.valueAt(cycle * locomotion.duration(), trackName, fallback);
            float runValue = runLocomotion.duration() > 0.0f ? runLocomotion.valueAt(cycle * runLocomotion.duration(), trackName, fallback) : walkValue;
            float value = VariantTimeline.lerp(walkValue, runValue, locomotionState.runWeight());
            float weight = Math.min(1.0f, speed * 0.9f);
            base = trackName.endsWith("_sx") || trackName.endsWith("_sy") || trackName.endsWith("_sz") ? base * VariantTimeline.lerp(fallback, value, weight) : base + (value - fallback) * weight;
        }
        float turnValue = turn.valueAt(age, trackName, fallback);
        float turnWeight = Mth.clamp((float)(1.1f - speed), (float)0.01f, (float)1.0f);
        return trackName.endsWith("_sx") || trackName.endsWith("_sy") || trackName.endsWith("_sz") ? base * VariantTimeline.lerp(fallback, turnValue, turnWeight) : base + (turnValue - fallback) * turnWeight;
    }

    private static float look(boolean pitch) {
        LivingEntity entity;
        EMFEntity emfEntity = EMFAnimationApi.getCurrentEntity();
        if (!(emfEntity instanceof LivingEntity) || !((entity = (LivingEntity)emfEntity) instanceof Villager) && !(entity instanceof WanderingTrader) || entity.isSleeping()) {
            return 0.0f;
        }
        float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
        float age = emfEntity.emf$age() + partialTick;
        LookState state = LOOK_STATES.computeIfAbsent(entity.getUUID(), ignored -> new LookState());
        state.update(age, Mth.clamp((float)entity.getXRot(), (float)-90.0f, (float)90.0f), Mth.clamp((float)Mth.wrapDegrees((float)(entity.getYHeadRot() - entity.yBodyRot)), (float)-90.0f, (float)90.0f));
        return pitch ? state.pitch : state.yaw;
    }

    private static float animationTick(EMFEntity entity) {
        return entity.emf$age() + Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
    }

    private static MouthFrame mouthFrame() {
        ActiveDialogue active = DialogueAnimationState.active();
        return active == null || active.speechWeight() <= 0.0f ? null : active.timeline().mouthAt(active.elapsedSeconds());
    }

    private static ActiveDialogue active() {
        EMFEntity entity = EMFAnimationApi.getCurrentEntity();
        if (entity == null || entity.etf$getUuid() == null) {
            return null;
        }
        UUID id = entity.etf$getUuid();
        ActiveDialogue value = ACTIVE.get(id);
        if (value == null) {
            return null;
        }
        value.beginFrame(DialogueAnimationState.animationTick(entity), System.nanoTime());
        if (value.frameNanos() > value.endNanos()) {
            ACTIVE.remove(id, value);
            return null;
        }
        return value;
    }

    private record Gesture(float duration, Map<String, float[]> tracks) {
        float valueAt(float time, String trackName, float fallback) {
            return VariantTimeline.sample(this, trackName, Math.max(0.0f, Math.min(time, this.duration)), fallback);
        }
    }

    private record MouthFrame(float time, float open, float width, float closed) {
    }

    private record GestureFrame(float time, int gestureIndex) {
    }

    private record VariantTimeline(List<MouthFrame> mouth, List<GestureFrame> gestures) {
        MouthFrame mouthAt(float time) {
            MouthFrame selected = this.mouth.isEmpty() ? null : this.mouth.getFirst();
            for (MouthFrame frame : this.mouth) {
                if (frame.time() > time) break;
                selected = frame;
            }
            return selected;
        }

        float transformAt(float time, String trackName, float fallback) {
            int selected = -1;
            int index = 0;
            while (index < this.gestures.size() && !(this.gestures.get(index).time() > time)) {
                selected = index++;
            }
            if (selected < 0) {
                return fallback;
            }
            GestureFrame current = this.gestures.get(selected);
            float currentValue = VariantTimeline.stateValue(current, time, trackName, fallback);
            float transitionTime = time - current.time();
            if (transitionTime >= 0.3f) {
                return currentValue;
            }
            float previousValue = selected == 0 ? fallback : VariantTimeline.stateValue(this.gestures.get(selected - 1), time, trackName, fallback);
            return VariantTimeline.lerp(previousValue, currentValue, VariantTimeline.blendCurve(transitionTime / 0.3f));
        }

        float poseWeightAt(float time) {
            int selected = -1;
            int index = 0;
            while (index < this.gestures.size() && !(this.gestures.get(index).time() > time)) {
                selected = index++;
            }
            if (selected < 0) {
                return 0.0f;
            }
            GestureFrame current = this.gestures.get(selected);
            float currentWeight = VariantTimeline.stateWeight(current, time);
            float transitionTime = time - current.time();
            if (transitionTime >= 0.3f) {
                return currentWeight;
            }
            float previousWeight = selected == 0 ? 0.0f : VariantTimeline.stateWeight(this.gestures.get(selected - 1), time);
            return VariantTimeline.lerp(previousWeight, currentWeight, VariantTimeline.blendCurve(transitionTime / 0.3f));
        }

        float poseEndSeconds() {
            if (this.gestures.isEmpty()) {
                return 0.0f;
            }
            GestureFrame last = this.gestures.getLast();
            if (last.gestureIndex() < 0 || last.gestureIndex() >= GESTURES.size()) {
                return last.time() + 0.3f;
            }
            return last.time() + GESTURES.get(last.gestureIndex()).duration() + 0.3f;
        }

        private static float stateValue(GestureFrame frame, float time, String trackName, float fallback) {
            if (frame.gestureIndex() < 0 || frame.gestureIndex() >= GESTURES.size()) {
                return fallback;
            }
            Gesture gesture = GESTURES.get(frame.gestureIndex());
            float localTime = Math.max(0.0f, time - frame.time());
            float value = VariantTimeline.sample(gesture, trackName, Math.min(localTime, gesture.duration()), fallback);
            if (localTime <= gesture.duration()) {
                return value;
            }
            float out = VariantTimeline.blendCurve((localTime - gesture.duration()) / 0.3f);
            return VariantTimeline.lerp(value, fallback, out);
        }

        private static float stateWeight(GestureFrame frame, float time) {
            if (frame.gestureIndex() < 0 || frame.gestureIndex() >= GESTURES.size()) {
                return 0.0f;
            }
            Gesture gesture = GESTURES.get(frame.gestureIndex());
            float localTime = Math.max(0.0f, time - frame.time());
            if (localTime <= gesture.duration()) {
                return 1.0f;
            }
            return 1.0f - VariantTimeline.blendCurve((localTime - gesture.duration()) / 0.3f);
        }

        private static float sample(Gesture gesture, String trackName, float localTime, float fallback) {
            float[] samples = gesture.tracks().get(trackName);
            if (samples == null || samples.length == 0) {
                return fallback;
            }
            float sample = Math.max(0.0f, localTime) * framesPerSecond;
            int lower = Math.min(samples.length - 1, (int)Math.floor(sample));
            int upper = Math.min(samples.length - 1, lower + 1);
            float progress = Math.min(1.0f, sample - (float)lower);
            return VariantTimeline.lerp(samples[lower], samples[upper], progress);
        }

        private static float blendCurve(float progress) {
            float clamped = Math.max(0.0f, Math.min(1.0f, progress));
            float sine = (float)Math.sin((double)clamped * Math.PI * 0.5);
            return sine * sine;
        }

        private static float lerp(float from, float to, float progress) {
            return from + (to - from) * progress;
        }
    }

    private static final class ActiveDialogue {
        private final long startNanos;
        private final long audioEndNanos;
        private final long endNanos;
        private final VariantTimeline timeline;
        private final Map<String, Float> previousPose;
        private final boolean hasAudio;
        private final Map<String, Float> renderedPose = new ConcurrentHashMap<String, Float>();
        private int frameAgeBits = Integer.MIN_VALUE;
        private long frameNanos;

        private ActiveDialogue(long startNanos, long audioEndNanos, long endNanos, VariantTimeline timeline, Map<String, Float> previousPose, boolean hasAudio) {
            this.startNanos = startNanos;
            this.audioEndNanos = audioEndNanos;
            this.endNanos = endNanos;
            this.timeline = timeline;
            this.previousPose = previousPose;
            this.hasAudio = hasAudio;
            this.frameNanos = startNanos;
        }

        void beginFrame(float entityAge, long now) {
            int ageBits = Float.floatToIntBits(entityAge);
            if (ageBits == this.frameAgeBits) {
                return;
            }
            this.frameAgeBits = ageBits;
            this.frameNanos = now;
        }

        long frameNanos() {
            return this.frameNanos;
        }

        long endNanos() {
            return this.endNanos;
        }

        VariantTimeline timeline() {
            return this.timeline;
        }

        float elapsedSeconds() {
            return (float)(this.frameNanos - this.startNanos) / 1.0E9f;
        }

        float transition(String variableName, float value) {
            Float previous = this.previousPose.get(variableName);
            float result = previous == null ? value : VariantTimeline.lerp(previous.floatValue(), value, VariantTimeline.blendCurve(this.elapsedSeconds() / 0.3f));
            this.renderedPose.put(variableName, Float.valueOf(result));
            return result;
        }

        Map<String, Float> poseSnapshot() {
            return Map.copyOf(this.renderedPose);
        }

        float speechWeight() {
            if (!this.hasAudio) {
                return 0.0f;
            }
            long now = this.frameNanos;
            float fadeIn = (float)(now - this.startNanos) / 1.5E8f;
            float fadeOut = (float)(this.endNanos - now) / 1.5E8f;
            fadeOut = now <= this.audioEndNanos ? 1.0f : (float)(this.audioEndNanos + 150000000L - now) / 1.5E8f;
            return VariantTimeline.blendCurve(Math.min(fadeIn, fadeOut));
        }
    }

    private static final class TurnState {
        private boolean playing;
        private float anchorYaw;
        private float lastBodyYaw;
        private float lastUpdateTick = Float.NaN;
        private float startTick;
        private float signal;

        private TurnState() {
        }

        void update(float tick, float bodyYaw, boolean canTurn) {
            if (Float.compare(this.lastUpdateTick, tick) == 0) {
                return;
            }
            if (Float.isNaN(this.lastUpdateTick) || tick < this.lastUpdateTick || tick - this.lastUpdateTick > 5.0f) {
                this.playing = false;
                this.anchorYaw = bodyYaw;
                this.lastBodyYaw = bodyYaw;
                this.lastUpdateTick = tick;
                return;
            }
            float bodyDelta = Mth.wrapDegrees((float)(bodyYaw - this.lastBodyYaw));
            this.lastUpdateTick = tick;
            if (!canTurn) {
                this.playing = false;
                this.anchorYaw = bodyYaw;
                this.lastBodyYaw = bodyYaw;
                this.signal = 0.0f;
                return;
            }
            if (this.playing && tick - this.startTick >= 10.0f) {
                this.playing = false;
                this.anchorYaw = this.lastBodyYaw;
            }
            if (!this.playing && Math.abs(bodyDelta) > 0.1f) {
                this.playing = true;
                this.anchorYaw = this.lastBodyYaw;
                this.startTick = tick;
            }
            if (this.playing) {
                this.signal = Mth.sin((double)(Mth.wrapDegrees((float)(bodyYaw - this.anchorYaw)) * ((float)Math.PI / 180))) * 90.0f;
                if (this.signal * bodyDelta < -0.1f) {
                    this.anchorYaw = this.lastBodyYaw;
                    this.startTick = tick;
                    this.signal = Mth.sin((double)(bodyDelta * ((float)Math.PI / 180))) * 90.0f;
                }
            } else {
                this.signal = 0.0f;
            }
            this.lastBodyYaw = bodyYaw;
        }

        float valueAt(float tick, String trackName, float fallback) {
            boolean positive;
            if (!this.playing || this.signal == 0.0f) {
                return fallback;
            }
            float time = Mth.clamp((float)((tick - this.startTick) / 20.0f), (float)0.0f, (float)0.5f);
            if (trackName.equals("waist_rz")) {
                return time <= 0.25f ? -Mth.sin((double)(time * 720.0f * ((float)Math.PI / 180))) * this.signal * 0.1f * ((float)Math.PI / 180) : 0.0f;
            }
            if (trackName.equals("waist_ty")) {
                return time <= 0.25f && Math.abs(this.signal) > 12.0f ? Mth.sin((double)(time * 1440.0f * ((float)Math.PI / 180))) * 0.3f : 0.0f;
            }
            boolean bl = positive = this.signal > 0.0f;
            if (trackName.equals("left_leg_root_ry")) {
                return this.legRotation(time, positive ? 0.0f : 0.2083f, positive ? 0.1667f : 0.375f);
            }
            if (trackName.equals("right_leg_root_ry")) {
                return this.legRotation(time, positive ? 0.2083f : 0.0f, positive ? 0.375f : 0.1667f);
            }
            if (trackName.equals("left_leg_root_ty")) {
                return this.legLift(time, positive ? 0.0f : 0.2083f, positive ? 0.0833f : 0.2917f, positive ? 0.1667f : 0.375f, positive ? 0.06f : 0.05f);
            }
            if (trackName.equals("right_leg_root_ty")) {
                return this.legLift(time, positive ? 0.2083f : 0.0f, positive ? 0.2917f : 0.0833f, positive ? 0.375f : 0.1667f, positive ? 0.05f : 0.06f);
            }
            return fallback;
        }

        private float legRotation(float time, float holdUntil, float end) {
            if (time <= holdUntil) {
                return -this.signal * ((float)Math.PI / 180);
            }
            if (time >= end) {
                return 0.0f;
            }
            return -this.signal * ((float)Math.PI / 180) * (1.0f - (time - holdUntil) / (end - holdUntil));
        }

        private float legLift(float time, float start, float peak, float end, float multiplier) {
            if (time < start || time > end) {
                return 0.0f;
            }
            float height = Math.min(Math.abs(this.signal) * multiplier, 1.0f);
            float weight = time <= peak ? (time - start) / (peak - start) : (end - time) / (end - peak);
            return -height * Mth.clamp((float)weight, (float)0.0f, (float)1.0f);
        }
    }

    private static final class IdleState {
        private boolean active;
        private int activeIndex = -1;
        private int lastIndex = -1;
        private int blendFromIndex = -1;
        private float startTick;
        private float lastUpdateTick = Float.NaN;
        private float weight;

        private IdleState() {
        }

        void update(float tick, boolean shouldPlay) {
            if (Float.compare(this.lastUpdateTick, tick) == 0) {
                return;
            }
            float elapsedTicks = Float.isNaN(this.lastUpdateTick) ? 0.0f : Math.max(0.0f, tick - this.lastUpdateTick);
            this.lastUpdateTick = tick;
            if (shouldPlay && !this.active) {
                this.active = true;
                this.startNext(tick, -1);
            }
            if (this.active) {
                this.advance(tick);
            }
            float step = elapsedTicks / 4.7999997f;
            float f = this.weight = shouldPlay ? Math.min(1.0f, this.weight + step) : Math.max(0.0f, this.weight - step);
            if (!shouldPlay && this.weight == 0.0f && this.active) {
                this.active = false;
                if (this.activeIndex >= 0) {
                    this.lastIndex = this.activeIndex;
                }
                this.activeIndex = -1;
                this.blendFromIndex = -1;
            }
        }

        float valueAt(float tick, String trackName, float fallback) {
            if (!this.active || this.activeIndex < 0 || IDLES.isEmpty()) {
                return fallback;
            }
            Gesture activeGesture = IDLES.get(this.activeIndex);
            float elapsed = (tick - this.startTick) / 20.0f;
            float value = activeGesture.valueAt(elapsed, trackName, fallback);
            if (elapsed < 0.3f) {
                float previous = this.blendFromIndex < 0 ? fallback : IDLES.get(this.blendFromIndex).valueAt(IDLES.get(this.blendFromIndex).duration(), trackName, fallback);
                value = VariantTimeline.lerp(previous, value, VariantTimeline.blendCurve(elapsed / 0.3f));
            }
            return VariantTimeline.lerp(fallback, value, this.weight);
        }

        private void advance(float tick) {
            for (int transitions = 0; transitions < 32; ++transitions) {
                Gesture gesture = IDLES.get(this.activeIndex);
                float durationTicks = gesture.duration() * 20.0f;
                if (tick - this.startTick <= durationTicks) {
                    return;
                }
                float nextStartTick = this.startTick + durationTicks;
                int previous = this.activeIndex;
                this.startNext(nextStartTick, previous);
            }
            this.startNext(tick, this.activeIndex);
        }

        private void startNext(float tick, int blendFromIndex) {
            int next = ThreadLocalRandom.current().nextInt(IDLES.size());
            if (IDLES.size() > 1 && next == this.lastIndex) {
                next = (next + 1) % IDLES.size();
            }
            this.blendFromIndex = blendFromIndex;
            this.activeIndex = next;
            this.lastIndex = next;
            this.startTick = tick;
        }
    }

    private static final class LocomotionState {
        private boolean running;
        private float smoothedSpeed;
        private float runWeight;
        private float lastUpdateTick = Float.NaN;

        private LocomotionState() {
        }

        void update(float tick, float movementSpeed, boolean canMove) {
            if (Float.compare(this.lastUpdateTick, tick) == 0) {
                return;
            }
            if (Float.isNaN(this.lastUpdateTick) || tick < this.lastUpdateTick || tick - this.lastUpdateTick > 5.0f) {
                this.smoothedSpeed = movementSpeed;
                this.running = canMove && movementSpeed > 0.6f;
                this.runWeight = this.running ? 1.0f : 0.0f;
                this.lastUpdateTick = tick;
                return;
            }
            float elapsedTicks = tick - this.lastUpdateTick;
            this.lastUpdateTick = tick;
            float speedBlend = 1.0f - (float)Math.pow(0.3, elapsedTicks);
            this.smoothedSpeed = VariantTimeline.lerp(this.smoothedSpeed, movementSpeed, speedBlend);
            this.running = canMove && (this.running ? this.smoothedSpeed >= 0.3f : this.smoothedSpeed > 0.6f);
            float step = elapsedTicks / 4.0f;
            this.runWeight = this.running ? Math.min(1.0f, this.runWeight + step) : Math.max(0.0f, this.runWeight - step);
        }

        float runWeight() {
            return this.runWeight;
        }
    }

    private static final class LookState {
        private float pitch;
        private float yaw;
        private float lastUpdateTick = Float.NaN;

        private LookState() {
        }

        void update(float tick, float targetPitch, float targetYaw) {
            if (Float.compare(this.lastUpdateTick, tick) == 0) {
                return;
            }
            if (Float.isNaN(this.lastUpdateTick) || tick < this.lastUpdateTick || tick - this.lastUpdateTick > 5.0f) {
                this.pitch = targetPitch;
                this.yaw = targetYaw;
                this.lastUpdateTick = tick;
                return;
            }
            float elapsedTicks = tick - this.lastUpdateTick;
            this.lastUpdateTick = tick;
            float yawBlend = 1.0f - (float)Math.pow(0.95, elapsedTicks * 3.0f);
            float pitchBlend = 1.0f - (float)Math.pow(0.98, elapsedTicks * 3.0f);
            this.yaw += Mth.wrapDegrees((float)(targetYaw - this.yaw)) * yawBlend;
            this.pitch = VariantTimeline.lerp(this.pitch, targetPitch, pitchBlend);
        }
    }
}

