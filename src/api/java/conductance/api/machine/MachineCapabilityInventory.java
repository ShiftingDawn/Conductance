package conductance.api.machine;

import java.util.function.BiConsumer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import lombok.Getter;
import lombok.Setter;
import conductance.api.CAPI;
import conductance.api.machine.gui.GuiTextures;
import conductance.api.machine.gui.IGuiWidget;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.machine.gui.ManagedBoolean;
import conductance.api.machine.gui.ToggleButtonWidget;
import conductance.api.util.GuiUtils;

public class MachineCapabilityInventory extends MachineCapability implements IBlockCapabilityHandler, IDelegatedItemHandler {

	private final @Getter MachineInventory inventory;
	private @Setter CapIO ioMode = CapIO.BOTH;

	public MachineCapabilityInventory(final String key, final MachineBlockEntity<?> machine, final MachineInventory inventory) {
		super(key, machine);
		this.inventory = inventory;
		this.inventory.setChangeListener(this::onContentsChanged);
	}

	@Override
	public CapIO getCapabilityIoMode() {
		return this.ioMode;
	}

	@Override
	public IItemHandlerModifiable getRealItemHandler() {
		return this.inventory;
	}

	@Override
	public void serialize(final ValueOutput valueOutput) {
		this.inventory.serialize(valueOutput);
	}

	@Override
	public void deserialize(final ValueInput valueInput) {
		this.inventory.deserialize(valueInput);
	}

	public void onContentsChanged() {
		this.setChanged();
	}

	@Override
	public ItemStack insertItem(final int item, final ItemStack stack, final boolean simulate) {
		if (!this.canCapabilityInput()) {
			return stack;
		}
		return this.insertItemInternal(item, stack, simulate);
	}

	public ItemStack insertItemInternal(final int slot, final ItemStack stack, final boolean simulate) {
		return this.inventory.insertItem(slot, stack, simulate);
	}

	@Override
	public ItemStack extractItem(final int slot, final int count, final boolean simulate) {
		if (!this.canCapabilityOutput()) {
			return ItemStack.EMPTY;
		}
		return this.extractItemInternal(slot, count, simulate);
	}

	public ItemStack extractItemInternal(final int slot, final int count, final boolean simulate) {
		return this.inventory.extractItem(slot, count, simulate);
	}

	@Override
	public void addGuiControls(final MachineMenu menu, final BiConsumer<String, IGuiWidget> adder) {
		if (this.canCapabilityInput()) {
			adder.accept("item_allow_overflow", CAPI.make(new ToggleButtonWidget(
				0, 0, 0, 0,
				new ManagedBoolean(this.inventory::setAllowOverflow, this.inventory::isAllowOverflow),
				toggled -> toggled ? GuiTextures.TEXTURE_ITEM_OVERFLOW_ON.get() : GuiTextures.TEXTURE_ITEM_OVERFLOW_OFF.get(),
				null
			), button -> button.addTooltipCallback((widget, tooltip) -> {
				final ToggleButtonWidget btn = (ToggleButtonWidget) widget;
				GuiUtils.tooltipTranslatable(tooltip, btn.isToggled() ? "guiWidget.conductance.allow_overflow.item.disable" : "guiWidget.conductance.allow_overflow.item.enable");
			})));
		}
	}
}
