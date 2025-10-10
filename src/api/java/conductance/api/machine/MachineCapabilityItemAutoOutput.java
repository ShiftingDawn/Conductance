package conductance.api.machine;

import net.minecraft.core.Direction;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class MachineCapabilityItemAutoOutput extends MachineCapability implements IItemAutoOutput {

	private final MachineInventory handler;
	private boolean enabled = false;
	private Direction side = Direction.NORTH;
	private @Nullable MachineTick tick = null;

	public MachineCapabilityItemAutoOutput(final String key, final MachineBlockEntity<?> machine, final MachineInventory inventory) {
		super(key, machine);
		this.handler = inventory;
		this.handler.addChangeListener(this::revalidateTick);
		this.addChangedListener(this::revalidateTick);
	}

	protected void revalidateTick() {
		if (!this.getMachine().isServerSide()) {
			return;
		}
		this.tick = this.getMachine().addTick(this::tick, this.tick);
	}

	private void tick() {
		assert this.tick != null;
		if (this.handler.isEmpty() || !this.isItemAutoOutputEnabled()) {
			this.tick.invalidate();
			return;
		}
		if (this.getMachine().haveTicksPassed(10)) {
			CapabilityHelper.tryTransferInventory(this.handler, this.getMachine().getLevel(), this.getMachine().getBlockPos().relative(this.side), this.side.getOpposite());
		}
	}

	@Override
	public void serialize(final ValueOutput output) {
		output.putBoolean("enabled", this.enabled);
		output.store("side", Direction.CODEC, this.side);
	}

	@Override
	public void deserialize(final ValueInput input) {
		this.enabled = input.getBooleanOr("enabled", false);
		this.side = input.read("side", Direction.CODEC).orElse(Direction.NORTH);
	}

	@Override
	public void setItemAutoOutputEnabled(final boolean enable) {
		this.enabled = enable;
		this.setChanged();
	}

	@Override
	public boolean isItemAutoOutputEnabled() {
		return this.enabled;
	}

	@Override
	public void setItemAutoOutputSide(final Direction face) {
		this.side = face;
		this.setChanged();
	}

	@Override
	public Direction getItemAutoOutputSide() {
		return this.side;
	}
}
