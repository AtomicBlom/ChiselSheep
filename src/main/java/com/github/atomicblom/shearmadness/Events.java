package com.github.atomicblom.shearmadness;

import com.github.atomicblom.shearmadness.api.CommonReference;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CommonReference.MOD_ID, value=Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class Events {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onAtlasStitch(TextureStitchEvent.Pre event) {
        if (!event.getMap().getTextureLocation().equals(PlayerContainer.LOCATION_BLOCKS_TEXTURE)) return;

        event.addSprite(new ResourceLocation("shearmadness:bad_render"));
    }
}
