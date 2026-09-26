package net.glerkin.testmod.screen;

import net.glerkin.testmod.block.ModBlocks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class CustomFurnaceMenu extends AbstractContainerMenu {
    private final BlockEntity blockEntity;
    private final ContainerData data;

    // Конструктор для клиента (вызывается автоматически при открытии GUI на клиенте)
    public CustomFurnaceMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(3));
    }

    // Конструктор для сервера
    public CustomFurnaceMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.CUSTOM_FURNACE_MENU.get(), id);
        checkContainerDataCount(data, 3);
        this.blockEntity = entity;
        this.data = data;

        // Добавляем слоты кастомной печки
        if (entity != null) {
            entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
                this.addSlot(new SlotItemHandler(handler, 0, 56, 17));  // Слот сырья
                this.addSlot(new SlotItemHandler(handler, 1, 56, 53));  // Слот угля
                this.addSlot(new SlotItemHandler(handler, 2, 116, 35)); // Слот результата
            });
        }

        // Добавляем стандартный инвентарь игрока
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        // Добавляем хотбар игрока (быстрый доступ)
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inv, k, 8 + k * 18, 142));
        }

        addDataSlots(data);
    }

    public boolean isLit() {
        return data.get(2) > 0; // burnTime > 0
    }

    public int getScaledProgress() {
        int progress = data.get(0);
        int maxProgress = data.get(1);
        return maxProgress != 0 && progress != 0 ? progress * 24 / maxProgress : 0;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), player, ModBlocks.CUSTOM_FURNACE.get());
    }

    // Логика Shift+Клика (перемещение предметов между инвентарем и печкой)
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            // Если кликнули по слоту внутри печи (слоты 0, 1, 2)
            if (index < 3) {
                // Перемещаем в инвентарь игрока (слоты 3 - 39)
                if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            }
            // Если кликнули по инвентарю игрока
            else {
                // Если предмет является углем или древесным углем, сначала пробуем в слот топлива (слот 1)
                if (itemstack1.is(Items.COAL) || itemstack1.is(Items.CHARCOAL)) {
                    if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                        // Если слот топлива занят или полон, пробуем в слот сырья (слот 0)
                        if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
                // Все остальные предметы отправляем строго в слот сырья (слот 0)
                else {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
        }
    }
