package net.glerkin.testmod.block.custom;

import net.glerkin.testmod.block.ModBlockEntities;
import net.glerkin.testmod.screen.CustomFurnaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CustomFurnaceBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> itemHandler);

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200;
    private int burnTime = 0;

    public CustomFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CUSTOM_FURNACE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> CustomFurnaceBlockEntity.this.progress;
                    case 1 -> CustomFurnaceBlockEntity.this.maxProgress;
                    case 2 -> CustomFurnaceBlockEntity.this.burnTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> CustomFurnaceBlockEntity.this.progress = value;
                    case 1 -> CustomFurnaceBlockEntity.this.maxProgress = value;
                    case 2 -> CustomFurnaceBlockEntity.this.burnTime = value;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Nonnull
    @Override
    public Component getDisplayName() {
        return Component.literal("Кастомная Печь");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inventory, @Nonnull Player player) {
        return new CustomFurnaceMenu(id, inventory, this, this.data);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CustomFurnaceBlockEntity entity) {
        if (level.isClientSide()) return;

        boolean wasLit = state.getValue(CustomFurnaceBlock.LIT);
        boolean isLitNow = entity.burnTime > 0;

        if (entity.burnTime > 0) {
            entity.burnTime--;
        }

        if (entity.burnTime <=  0 && hasRecipe(entity)) {
            entity.progress++;
            if (entity.progress >= entity.maxProgress) {
                entity.craftItem();
                entity.progress = 0;
            }
        } else {
            entity.progress = 0;
        }

        if (wasLit != isLitNow) {
            level.setBlock(pos, state.setValue(CustomFurnaceBlock.LIT, isLitNow), 3);
            setChanged(level, pos, state);
        }
    }

    private static boolean hasFuel(CustomFurnaceBlockEntity entity) {
        return entity.itemHandler.getStackInSlot(1).is(Items.COAL) ||
                entity.itemHandler.getStackInSlot(1).is(Items.CHARCOAL);
    }

    private static boolean hasRecipe(CustomFurnaceBlockEntity entity) {
        ItemStack inputSlot = entity.itemHandler.getStackInSlot(0);
        boolean hasIngredient = inputSlot.is(Items.RAW_IRON);

        ItemStack resultSlot = entity.itemHandler.getStackInSlot(2);
        return hasIngredient && (resultSlot.isEmpty() || (resultSlot.is(Items.GOLD_INGOT) && resultSlot.getCount() < 64));
    }

    private void craftItem() {
        this.itemHandler.extractItem(0, 1, false);
        this.itemHandler.setStackInSlot(2, new ItemStack(Items.GOLD_INGOT,
                this.itemHandler.getStackInSlot(2).getCount() + 1));
    }

    public void drops() {
        if (this.level == null) return;
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("progress", progress);
        tag.putInt("burnTime", burnTime);
        super.saveAdditional(tag);
    }

    @Override
    public void load(@Nonnull CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("progress");
        burnTime = tag.getInt("burnTime");
    }
}
