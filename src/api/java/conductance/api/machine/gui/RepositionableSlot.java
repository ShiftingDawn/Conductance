package conductance.api.machine.gui;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import lombok.Getter;

public class RepositionableSlot extends Slot {

	private final @Getter int initialX;
	private final @Getter int initialY;

	public RepositionableSlot(final Container container, final int slot, final int initialX, final int initialY) {
		super(container, slot, initialX, initialY);
		this.initialX = initialX;
		this.initialY = initialY;
	}

	public final void setX(final int x) {
		this.x = x;
	}

	public final void setY(final int y) {
		this.y = y;
	}

	public final void setRelativeX(final int x) {
		this.x = this.initialX + x;
	}

	public final void setRelativeY(final int y) {
		this.y = this.initialY + y;
	}
}
