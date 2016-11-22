package com.github.atomicblom.shearmadness.utility;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import java.util.Arrays;
import java.util.Random;

@SuppressWarnings("UtilityClass")
public final class ItemStackUtils
{
    private ItemStackUtils() {}

    public static void dropItem(Entity nearEntity, ItemStack itemStack)
    {
        final EntityItem item = nearEntity.entityDropItem(itemStack, 1.0F);
        final Random rand = new Random();
        item.motionY += rand.nextFloat() * 0.05F;
        item.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
        item.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
    }

    public static int getHash(ItemStack itemStack)
    {
        final ByteBuf buffer = new PooledByteBufAllocator(false).heapBuffer();
        final PacketBuffer packetBuffer = new PacketBuffer(buffer);
        packetBuffer.writeItemStack(itemStack);
        return Arrays.hashCode(packetBuffer.array());
    }
}
