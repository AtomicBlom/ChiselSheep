package com.github.atomicblom.shearmadness.proxy;

import com.github.atomicblom.shearmadness.block.InvisibleBookshelfBlock;
import com.github.atomicblom.shearmadness.block.InvisibleGlowstoneBlock;
import com.github.atomicblom.shearmadness.block.InvisibleRedstoneBlock;
import com.github.atomicblom.shearmadness.utility.Localization;
import com.github.atomicblom.shearmadness.utility.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

@SuppressWarnings({"MethodMayBeStatic", "unused"})
public class CommonBlockProxy
{
    public void registerBlocks() {
        registerBlockAndItem(new InvisibleRedstoneBlock(), Reference.Blocks.InvisibleRedstone);
        registerBlockAndItem(new InvisibleGlowstoneBlock(), Reference.Blocks.InvisibleGlowstone);
        registerBlockAndItem(new InvisibleBookshelfBlock(), Reference.Blocks.InvisibleBookshelf);
    }

    private void registerBlockAndItem(Block block, ResourceLocation name) {
        GameRegistry.register(configureBlock(block, name));
        GameRegistry.register(configureItemBlock(new ItemBlock(block)));
    }

    protected Block configureBlock(Block block, ResourceLocation name) {
        return block.setRegistryName(name)
                .setUnlocalizedName(Localization.getUnlocalizedNameFor(block));
    }

    protected Item configureItemBlock(ItemBlock itemBlock)
    {
        return itemBlock
                .setRegistryName(itemBlock.block.getRegistryName())
                .setCreativeTab(Reference.CreativeTab)
                .setUnlocalizedName(itemBlock.block.getUnlocalizedName());
    }

}
