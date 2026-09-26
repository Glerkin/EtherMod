package net.glerkin.testmod.block;

import net.glerkin.testmod.TestMod;
import net.glerkin.testmod.item.ModItems;
import net.glerkin.testmod.block.custom.CustomFurnaceBlock;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, TestMod.MOD_ID);

    // Ваши блоки (Сталь и Никель)
    public static final RegistryObject<Block> STEEL_BLOCK = registerBlock("steel_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> NICKEL_BLOCK = registerBlock("nickel_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    //EtherMod

    

    public static final RegistryObject<Block> ETHER_ORE = registerBlock("ether_ore",
            () -> new DropExperienceBlock(
                    BlockBehaviour.Properties.copy(Blocks.STONE)
                            .requiresCorrectToolForDrops()
                            .strength(1.5F, 6.0F)
                            .sound(SoundType.STONE),
                    UniformInt.of(2, 5)
            ));

    public static final RegistryObject<Block> DEEPSLATE_ETHER_ORE = registerBlock("deepslate_ether_ore",
            () -> new DropExperienceBlock(
                    BlockBehaviour.Properties.copy(Blocks.DEEPSLATE) // Или Blocks.DEEPSLATE для глубинной руды
                            .requiresCorrectToolForDrops()
                            .strength(4.5F, 4.5F) // Прочность камня (у глубинного сланца 4.5F)
                            .sound(SoundType.STONE),
                    UniformInt.of(2, 5)
            ));

    public static final RegistryObject<Block> ETHER_BLOCK = registerBlock("ether_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> CUSTOM_FURNACE = registerBlock("custom_furnace",
            () -> new CustomFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.FURNACE)));

    // Метод регистрации блока
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn); // Передаем RegistryObject, а не Supplier
        return toReturn;
    }

    // ИСПРАВЛЕНО: Теперь принимает RegistryObject<T>, чтобы избежать краша при запуске
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}