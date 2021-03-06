package com.github.atomicblom.shearmadness.rendering;

import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.VariationRegistry;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.api.modelmaker.IModelMaker;
import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.configuration.Settings;
import com.github.atomicblom.shearmadness.utility.Logger;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;

import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("StaticNonFinalField")
@SideOnly(Side.CLIENT)
public class LayerSheepChiselWool implements LayerRenderer<EntitySheep>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");

    private final RenderChiselSheep sheepRenderer;
    private final Cache<Integer, ModelQuadruped> modelCache = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build();
    private final HashSet<Integer> badModels = Sets.newHashSet();

    private final ModelQuadruped defaultBody;
    private final IModelMaker errorModelMaker = new ErrorModelMaker();
    private ModelQuadruped sheepModel;
    public LayerSheepChiselWool(RenderChiselSheep sheepRendererIn)
    {
        sheepRenderer = sheepRendererIn;
        defaultBody = new ModelSheep1();
        sheepModel = defaultBody;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void doRenderLayer(EntitySheep sheep, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (!sheep.getSheared() && !sheep.isInvisible())
        {
            final IChiseledSheepCapability capability = sheep.getCapability(Capability.CHISELED_SHEEP, null);
            if (capability.isChiseled())
            {
                final int itemIdentifier = capability.getItemIdentifier();
                final ItemStack itemStack = capability.getChiselItemStack();
                try
                {
                    if (Settings.debugModels()) {
                        modelCache.invalidate(itemIdentifier);
                    }

                    sheepModel = modelCache.get(itemIdentifier, () ->
                    {
                        try {
                            if (badModels.contains(itemIdentifier)) {
                                return errorModelMaker.createModel(null, sheep);
                            }

                            final IModelMaker variationModelMaker = VariationRegistry.INSTANCE.getVariationModelMaker(itemStack);
                            return variationModelMaker.createModel(itemStack, sheep);
                        } catch (Exception e) {
                            Logger.log(Level.INFO, e, "Error creating chiseled sheep model, item stack was %s", itemStack);
                            return errorModelMaker.createModel(null, sheep);
                        }
                    });
                    sheepRenderer.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

                    final Item item = itemStack.getItem();
                    if (item instanceof ItemBlock)
                    {
                        final int colorMultiplier = Minecraft.getMinecraft().getBlockColors().colorMultiplier(((ItemBlock) item).block.getDefaultState(), sheep.worldObj, sheep.getPosition(), 0);
                        GlStateManager.color(
                                (colorMultiplier >> 16 & 255) / 255.0F, //Red
                                (colorMultiplier >> 8 & 255) / 255.0F, //Green
                                (colorMultiplier & 255) / 255.0F); //Blue
                    }

                    renderModel(sheep, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                } catch (final Exception exception) {
                    badModels.add(itemIdentifier);
                    modelCache.put(itemIdentifier, errorModelMaker.createModel(null, sheep));
                    Logger.warning("Error rendering chiselled sheep with item %s - %s", itemStack, exception);
                    exception.printStackTrace();
                }
            } else
            {
                sheepModel = defaultBody;
                sheepRenderer.bindTexture(TEXTURE);

                final float[] afloat = EntitySheep.getDyeRgb(sheep.getFleeceColor());
                GlStateManager.color(afloat[0], afloat[1], afloat[2]);

                renderModel(sheep, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            }
        }
    }

    public void renderModel(EntitySheep sheep, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        sheepModel.setModelAttributes(sheepRenderer.getMainModel());
        sheepModel.setLivingAnimations(sheep, limbSwing, limbSwingAmount, partialTicks);
        sheepModel.render(sheep, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return true;
    }


}

