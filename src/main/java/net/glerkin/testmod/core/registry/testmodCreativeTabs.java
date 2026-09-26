package net.glerkin.testmod.core.registry;

import net.glerkin.testmod.TestMod;
import net.glerkin.testmod.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.IEventBusEngine;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

//import java.awt.*;

import static com.mojang.serialization.codecs.RecordCodecBuilder.build;

public class testmodCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TestMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab
            .builder()
            .icon(() -> ModItems.ETHER_SHARD.get().getDefaultInstance())
            .title(Component.translatable("creative_tab.testmod.tab"))
            .displayItems((parameters, output) -> {
                //output.accept(ModItems.ETHER_SHARD.get());
                ModItems.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
                    }).build());

    public static void register(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }

}
