package net.glerkin.testmod;

import com.mojang.logging.LogUtils;
import net.glerkin.testmod.block.ModBlockEntities;
import net.glerkin.testmod.block.ModBlocks;
import net.glerkin.testmod.core.registry.testmodCreativeTabs;
import net.glerkin.testmod.item.ModItems;
import net.glerkin.testmod.recipe.ModRecipes;
import net.glerkin.testmod.screen.CustomFurnaceScreen;
import net.glerkin.testmod.screen.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(TestMod.MOD_ID)
public class TestMod
{
    public static final String MOD_ID = "testmod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public TestMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        testmodCreativeTabs.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // ИНТЕГРАЦИЯ: Регистрируем экран печи на клиенте, чтобы игра не вылетала
            event.enqueueWork(() -> {
                MenuScreens.register(ModMenuTypes.CUSTOM_FURNACE_MENU.get(), CustomFurnaceScreen::new);
            });
        }
    }
}
