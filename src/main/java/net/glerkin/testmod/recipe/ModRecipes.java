package net.glerkin.testmod.recipe;

import net.glerkin.testmod.TestMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TestMod.MOD_ID);

    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, TestMod.MOD_ID);

    // Регистрируем сериализатор
    public static final RegistryObject<RecipeSerializer<CustomFurnaceRecipe>> CUSTOM_SMELTING_SERIALIZER =
            SERIALIZERS.register("custom_smelting", () -> CustomFurnaceRecipe.Serializer.INSTANCE);

    // Регистрируем тип рецепта
    public static final RegistryObject<RecipeType<CustomFurnaceRecipe>> CUSTOM_SMELTING_TYPE =
            TYPES.register("custom_smelting", () -> CustomFurnaceRecipe.Type.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
