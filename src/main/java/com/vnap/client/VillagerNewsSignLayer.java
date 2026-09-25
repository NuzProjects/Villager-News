/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.model.npc.VillagerModel
 *  net.minecraft.client.renderer.SubmitNodeCollector
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.client.renderer.entity.state.VillagerRenderState
 *  net.minecraft.client.renderer.rendertype.RenderTypes
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.Identifier
 *  org.joml.Quaternionfc
 *  traben.entity_model_features.models.IEMFModel
 *  traben.entity_model_features.models.animation.EMFAttachment$Type
 */
package com.vnap.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.vnap.client.VillagerNewsRenderState;
import java.util.function.Consumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.npc.VillagerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.joml.Quaternionfc;
import traben.entity_model_features.models.IEMFModel;
import traben.entity_model_features.models.animation.EMFAttachment;

public final class VillagerNewsSignLayer
extends RenderLayer<VillagerRenderState, VillagerModel> {
    private static final Identifier[] BOARD_TEXTURES = new Identifier[]{VillagerNewsSignLayer.minecraft("oak"), VillagerNewsSignLayer.minecraft("spruce"), VillagerNewsSignLayer.minecraft("birch"), VillagerNewsSignLayer.minecraft("jungle"), VillagerNewsSignLayer.minecraft("acacia"), VillagerNewsSignLayer.minecraft("dark_oak"), VillagerNewsSignLayer.minecraft("mangrove"), VillagerNewsSignLayer.minecraft("cherry"), VillagerNewsSignLayer.minecraft("pale_oak"), VillagerNewsSignLayer.minecraft("bamboo"), VillagerNewsSignLayer.minecraft("crimson"), VillagerNewsSignLayer.minecraft("warped")};
    private static final Identifier TEXT_TEXTURE = Identifier.fromNamespaceAndPath((String)"villager-news-addon-port", (String)"textures/entity/sign_text.png");

    public VillagerNewsSignLayer(RenderLayerParent<VillagerRenderState, VillagerModel> renderer) {
        super(renderer);
    }

    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int packedLight, VillagerRenderState state, float yRot, float xRot) {
        Consumer positioner;
        VillagerNewsRenderState sign = (VillagerNewsRenderState)state;
        int type = sign.vnap$signType();
        int message = sign.vnap$signMessage();
        if (state.isInvisible || state.isBaby || type < 0 || type >= BOARD_TEXTURES.length || message < 0 || message >= 87) {
            return;
        }
        poseStack.pushPose();
        EntityModel entityModel = this.getParentModel();
        if (entityModel instanceof IEMFModel) {
            IEMFModel emfModel = (IEMFModel)entityModel;
            v0 = emfModel.emf$getEMFRootModel().getPositionerForAttachment(EMFAttachment.Type.VILLAGER);
        } else {
            v0 = positioner = null;
        }
        if (positioner == null) {
            ((VillagerModel)this.getParentModel()).translateToArms(state, poseStack);
        } else {
            positioner.accept(poseStack);
        }
        poseStack.translate(0.0f, 0.359375f, -0.109375f);
        poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(42.97f));
        collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout((Identifier)BOARD_TEXTURES[type]), (pose, vertices) -> VillagerNewsSignLayer.drawBoard(pose, vertices, packedLight));
        collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout((Identifier)TEXT_TEXTURE), (pose, vertices) -> VillagerNewsSignLayer.drawText(pose, vertices, packedLight, message));
        poseStack.popPose();
    }

    private static Identifier minecraft(String wood) {
        return Identifier.withDefaultNamespace((String)("textures/block/" + wood + "_sign.png"));
    }

    private static void drawBoard(PoseStack.Pose pose, VertexConsumer vertices, int light) {
        float left = -0.50625f;
        float right = 0.50625f;
        float top = -0.25625f;
        float bottom = 0.25625f;
        float front = -0.04792f;
        float back = 0.04792f;
        VillagerNewsSignLayer.quad(pose, vertices, light, left, bottom, front, right, bottom, front, right, top, front, left, top, front, 0.0f, 0.875f, 0.75f, 0.5f, 0.0f, 0.0f, -1.0f);
        VillagerNewsSignLayer.quad(pose, vertices, light, right, bottom, back, left, bottom, back, left, top, back, right, top, back, 0.0f, 0.4375f, 0.75f, 0.0625f, 0.0f, 0.0f, 1.0f);
        VillagerNewsSignLayer.quad(pose, vertices, light, left, top, back, left, top, front, right, top, front, right, top, back, 0.0f, 0.0625f, 0.75f, 0.0f, 0.0f, -1.0f, 0.0f);
        VillagerNewsSignLayer.quad(pose, vertices, light, left, bottom, front, left, bottom, back, right, bottom, back, right, bottom, front, 0.0f, 0.9375f, 0.75f, 0.875f, 0.0f, 1.0f, 0.0f);
        VillagerNewsSignLayer.quad(pose, vertices, light, left, bottom, back, left, bottom, front, left, top, front, left, top, back, 0.75f, 0.875f, 0.8125f, 0.5f, -1.0f, 0.0f, 0.0f);
        VillagerNewsSignLayer.quad(pose, vertices, light, right, bottom, front, right, bottom, back, right, top, back, right, top, front, 0.75f, 0.4375f, 0.8125f, 0.0625f, 1.0f, 0.0f, 0.0f);
    }

    private static void drawText(PoseStack.Pose pose, VertexConsumer vertices, int light, int message) {
        float topV = (float)message / 87.0f;
        float bottomV = (float)(message + 1) / 87.0f;
        VillagerNewsSignLayer.quad(pose, vertices, light, -0.5f, 0.1875f, -0.06042f, 0.5f, 0.1875f, -0.06042f, 0.5f, -0.1875f, -0.06042f, -0.5f, -0.1875f, -0.06042f, 0.0f, bottomV, 1.0f, topV, 0.0f, 0.0f, -1.0f);
    }

    private static void quad(PoseStack.Pose pose, VertexConsumer vertices, int light, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float u1, float v1, float u2, float v2, float nx, float ny, float nz) {
        VillagerNewsSignLayer.vertex(pose, vertices, light, x1, y1, z1, u1, v1, nx, ny, nz);
        VillagerNewsSignLayer.vertex(pose, vertices, light, x2, y2, z2, u2, v1, nx, ny, nz);
        VillagerNewsSignLayer.vertex(pose, vertices, light, x3, y3, z3, u2, v2, nx, ny, nz);
        VillagerNewsSignLayer.vertex(pose, vertices, light, x4, y4, z4, u1, v2, nx, ny, nz);
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer vertices, int light, float x, float y, float z, float u, float v, float nx, float ny, float nz) {
        vertices.addVertex(pose, x, y, z).setColor(-1).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, nx, ny, nz);
    }
}

