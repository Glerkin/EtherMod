package net.glerkin.testmod.item;

import net.glerkin.testmod.TestMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TestMod.MOD_ID);

    // Ваши слитки (Исправлено IGNOT -> INGOT для порядка, свойства по умолчанию стакаются до 64)
    public static final RegistryObject<Item> STEEL_INGOT = ITEMS.register("steel_ingot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> NICKEL_INGOT = ITEMS.register("nickel_ingot",
            () -> new Item(new Item.Properties()));
    //EtherMod
    public static final RegistryObject<Item> ETHER_INGOT = ITEMS.register("ether_ingot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ETHER_SHARD = ITEMS.register("ether_shard",
            () -> new Item(new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
