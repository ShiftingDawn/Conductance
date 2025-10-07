package conductance.api.machine.gui;

import net.minecraft.world.inventory.Slot;

public class SlotWidget extends GuiWidget {

	private final Slot slot;

	public SlotWidget(final RepositionableSlot slot) {
		super(slot.x, slot.y, 18, 18);
		this.slot = slot;
	}

	public SlotWidget(final RepositionableSlotItemHandler slot) {
		super(slot.x, slot.y, 18, 18);
		this.slot = slot;
	}

	@Override
	public void initClient() {
		this.slot.x = this.getX() + 1;
		this.slot.y = this.getY() + 1;
	}
}
