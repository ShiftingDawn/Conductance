package conductance.api.machine.gui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import lombok.Getter;
import conductance.api.machine.CapIO;

public class RepositionableSlot extends Slot {

	private final @Getter int initialX;
	private final @Getter int initialY;
	private final @Getter CapIO io;

	public RepositionableSlot(final Container container, final int slot, final int initialX, final int initialY, final CapIO io) {
		super(container, slot, initialX, initialY);
		this.initialX = initialX;
		this.initialY = initialY;
		this.io = io;
	}

	@Override
	public boolean mayPlace(final ItemStack stack) {
		return this.io.isInput() && super.mayPlace(stack);
	}

	@Override
	public boolean mayPickup(final Player player) {
		return this.io.isOutput() && super.mayPickup(player);
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
