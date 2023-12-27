package dev.huskuraft.effortless.vanilla.adapters;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.gui.Typeface;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.renderer.*;
import dev.huskuraft.effortless.renderer.texture.BlockRenderTextures;
import dev.huskuraft.effortless.renderer.texture.OutlineRenderTextures;
import dev.huskuraft.effortless.renderer.texture.RenderTextures;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.vanilla.renderer.MinecraftBlockRenderTextures;
import dev.huskuraft.effortless.vanilla.renderer.MinecraftOutlineRenderTextures;
import dev.huskuraft.effortless.vanilla.renderer.MinecraftRenderTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.List;
import java.util.Optional;

public class MinecraftRenderer extends Renderer {

    @Deprecated
    private static final ResourceLocation BACKGROUND_LOCATION = new ResourceLocation("textures/gui/options_background.png");

    private static final WidgetSprites BUTTON_SPRITES = new WidgetSprites(new ResourceLocation("widget/button"), new ResourceLocation("widget/button_disabled"), new ResourceLocation("widget/button_highlighted"));

    private static final RenderTextures RENDER_TEXTURES = new MinecraftRenderTextures();
    private static final BlockRenderTextures BLOCK_RENDER_TEXTURES = new MinecraftBlockRenderTextures();
    private static final OutlineRenderTextures OUTLINE_RENDER_TEXTURES = new MinecraftOutlineRenderTextures();

    private static final RandomSource RAND = RandomSource.create();
    private final GuiGraphics reference;

    public MinecraftRenderer(GuiGraphics reference) {
        this.reference = reference;
    }

    public MinecraftRenderer(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource) {
        this.reference = new GuiGraphics(Minecraft.getInstance(), poseStack, bufferSource);
    }

    @Override
    public int windowWidth() {
        return reference.guiWidth();
    }

    @Override
    public int windowHeight() {
        return reference.guiHeight();
    }

    @Override
    public int optionColor(float alpha) {
        return Minecraft.getInstance().options.getBackgroundColor(alpha);
    }

    @Override
    public Camera camera() {
        return new Camera() {
            @Override
            public Vector3d position() {
                return MinecraftPlayer.fromMinecraftVector3d(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition());
            }

            @Override
            public Quaternionf rotation() {
                return Minecraft.getInstance().gameRenderer.getMainCamera().rotation();
            }

            @Override
            public float eyeHeight() {
                return Minecraft.getInstance().gameRenderer.getMainCamera().getEntity().getEyeHeight();
            }
        };
    }

    @Override
    public void pushPose() {
        reference.pose().pushPose();
    }

    @Override
    public void popPose() {
        reference.pose().popPose();
    }

    @Override
    public Matrix4f lastPose() {
        return reference.pose().last().pose();
    }

    @Override
    public Matrix3f lastPoseNormal() {
        return reference.pose().last().normal();
    }

    @Override
    public void enableScissor(int x1, int y1, int x2, int y2) {
        reference.enableScissor(x1, y1, x2, y2);
    }

    @Override
    public void disableScissor() {
        reference.disableScissor();
    }

    @Override
    public void setShaderColor(float red, float green, float blue, float alpha) {
        reference.setColor(red, green, blue, alpha);
    }

    @Override
    public VertexBuffer vertexBuffer(RenderTexture renderTexture) {
        return new MinecraftVertexBuffer(reference.bufferSource().getBuffer(dev.huskuraft.effortless.vanilla.adapters.MinecraftRenderTexture.toMinecraftRenderType(renderTexture)));
    }

    @Override
    public void flush() {
        reference.flush();
    }

    @Override
    public int renderText(Typeface typeface, Text text, int x, int y, int color, int backgroundColor, boolean shadow, FontDisplay mode, int lightMap) {
        var width = MinecraftTypeface.toMinecraftTypeface(typeface).drawInBatch(MinecraftText.toMinecraftText(text), x, y, color, shadow, lastPose(), reference.bufferSource(), Font.DisplayMode.values()[mode.ordinal()], backgroundColor, lightMap);
        flush();
        return width;
    }

    @Override
    public void renderTexture(Resource resource, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV) {
        reference.innerBlit(MinecraftResource.toMinecraftResource(resource), x1, x2, y1, y2, blitOffset, minU, maxU, minV, maxV);
    }

    @Override
    public void renderPanelBackgroundTexture(int x, int y, float uOffset, float vOffset, int uWidth, int vHeight) {
//        drawTexture(MinecraftClientAdapter.adapt(BACKGROUND_LOCATION), x, y, 0, uOffset, vOffset, uWidth, vHeight, 32, 32);
    }

    @Override
    public void renderButtonTexture(int x, int y, int width, int height, boolean active, boolean focused) {
        reference.blitSprite(BUTTON_SPRITES.get(active, focused), x, y, width, height);
    }

    @Override
    public void renderItem(ItemStack stack, int x, int y) {
        reference.renderItem(MinecraftItemStack.toMinecraftItemStack(stack), x, y);
    }

    @Override
    public void renderTooltip(Typeface typeface, List<Text> list, int x, int y) {
        reference.renderTooltip(MinecraftTypeface.toMinecraftTypeface(typeface), list.stream().map(MinecraftText::toMinecraftText).toList(), Optional.empty(), x, y);
    }

    @Override
    public void renderTooltip(Typeface typeface, ItemStack itemStack, int x, int y) {
        reference.renderTooltip(MinecraftTypeface.toMinecraftTypeface(typeface), MinecraftItemStack.toMinecraftItemStack(itemStack), x, y);
    }

    @Override
    public void renderBlockInWorld(RenderTexture renderTexture, World world, BlockPosition blockPosition, BlockData blockData) {
        var minecraftBlockRenderer = Minecraft.getInstance().getBlockRenderer();
        var minecraftWorld = MinecraftWorld.toMinecraftWorld(world);
        var minecraftRenderType = dev.huskuraft.effortless.vanilla.adapters.MinecraftRenderTexture.toMinecraftRenderType(renderTexture);
        var minecraftBlockData = MinecraftBlockData.toMinecraftBlockData(blockData);
        var minecraftBlockPosition = MinecraftPlayer.toMinecraftBlockPosition(blockPosition);

        minecraftBlockRenderer.getModelRenderer().tesselateBlock(
                minecraftWorld,
                minecraftBlockRenderer.getBlockModel(minecraftBlockData),
                minecraftBlockData,
                minecraftBlockPosition,
                reference.pose(),
                reference.bufferSource().getBuffer(minecraftRenderType),
                false,
                RAND,
                minecraftBlockData.getSeed(minecraftBlockPosition),
                OverlayTexture.NO_OVERLAY);

    }

    @Override
    public RenderTextures renderTextures() {
        return RENDER_TEXTURES;
    }

    @Override
    public BlockRenderTextures blockRenderTextures() {
        return BLOCK_RENDER_TEXTURES;
    }

    @Override
    public OutlineRenderTextures outlineRenderTextures() {
        return OUTLINE_RENDER_TEXTURES;
    }
}
