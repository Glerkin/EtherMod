package net.glerkin.testmod.block;

import net.glerkin.testmod.TestMod;
import net.glerkin.testmod.block.custom.CustomFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TestMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<CustomFurnaceBlockEntity>> CUSTOM_FURNACE =
            BLOCK_ENTITIES.register("custom_furnace", () ->
                    BlockEntityType.Builder.of(CustomFurnaceBlockEntity::new,
                            ModBlocks.CUSTOM_FURNACE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
