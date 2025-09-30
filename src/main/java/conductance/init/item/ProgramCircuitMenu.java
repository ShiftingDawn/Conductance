package conductance.init.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import lombok.Getter;
import conductance.api.NCItems;
import conductance.init.ConductanceMenuTypes;

public final class ProgramCircuitMenu extends AbstractContainerMenu {

	private final @Getter Player thePlayer;
	private final @Getter InteractionHand hand;
	private final @Getter DataSlot dataSlot;

	public ProgramCircuitMenu(final int containerId, final Player player, final InteractionHand hand, final DataSlot dataSlot) {
		super(ConductanceMenuTypes.PROGRAM_CIRCUIT.value(), containerId);
		this.thePlayer = player;
		this.hand = hand;
		this.dataSlot = dataSlot;
		this.addDataSlot(this.dataSlot);
		this.addStandardInventorySlots(player.getInventory(), 8, 84);
	}

	@Override
	public ItemStack quickMoveStack(final Player player, final int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(final Player player) {
		return player.getItemInHand(this.hand).is(NCItems.PROGRAM_CIRCUIT);
	}
}
