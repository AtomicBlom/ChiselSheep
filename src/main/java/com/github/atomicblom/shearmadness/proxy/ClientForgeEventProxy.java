package com.github.atomicblom.shearmadness.proxy;

import com.github.atomicblom.shearmadness.api.events.RegisterShearMadnessVariationEvent;
import com.github.atomicblom.shearmadness.api.VariationRegistry;
import com.github.atomicblom.shearmadness.networking.CheckSheepChiseledRequestMessage;
import com.github.atomicblom.shearmadness.variations.CommonReference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.github.atomicblom.shearmadness.ShearMadnessMod.CHANNEL;

@SuppressWarnings({"MethodMayBeStatic", "unused"})
public class ClientForgeEventProxy extends CommonForgeEventProxy
{
    @Override
    public void fireRegistryEvent() {
        MinecraftForge.EVENT_BUS.post(new RegisterShearMadnessVariationEvent(VariationRegistry.INSTANCE));
        super.fireRegistryEvent();
    }

    @SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event)
    {
        final Entity entity = event.getEntity();
        if (entity instanceof EntitySheep)
        {
            CHANNEL.sendToServer(new CheckSheepChiseledRequestMessage(entity));
        }
    }
}
