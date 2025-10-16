package conductance.init.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.CapabilityHelper;
import conductance.api.machine.MachineTick;
import conductance.api.machine.MachineType;
import conductance.api.machine.RecipePerTickFailureAction;
import conductance.api.tier.Tier;

public class GenericGeneratorMachine extends GenericRecipeMachine {

	private @Nullable MachineTick tick = null;

	public GenericGeneratorMachine(final MachineType<GenericRecipeMachine> type, final Tier tier, final BlockPos pos, final BlockState blockState) {
		super(type, tier, pos, blockState);
		this.getEnergy().setCapabilityValidator(side -> side == this.getFacing());
		this.getEnergy().addChangedListener(this::revalidateTick);
		if (this.getInputItems() != null) {
			this.getInputItems().setCapabilityValidator(side -> side != this.getFacing());
		}
		if (this.getInputFluids() != null) {
			this.getInputFluids().setCapabilityValidator(side -> side != this.getFacing());
		}
		if (this.getOutputItems() != null) {
			this.getOutputItems().setCapabilityValidator(side -> side != this.getFacing());
		}
		if (this.getOutputFluids() != null) {
			this.getOutputFluids().setCapabilityValidator(side -> side != this.getFacing());
		}
	}

	private void revalidateTick() {
		if (!this.isServerSide()) {
			return;
		}
		this.tick = this.addTick(this::tick, this.tick);
	}

	private void tick() {
		assert this.tick != null;
		if (this.getEnergy().getEnergyStored() == 0) {
			this.tick.invalidate();
			return;
		}
		final Direction facing = this.getFacing();
		CapabilityHelper.tryExportEnergy(this.getEnergy(), this.getLevel(), this.getBlockPos().relative(facing), facing.getOpposite());
	}

	@Override
	public void onLoad() {
		super.onLoad();
		this.revalidateTick();
	}

	@Override
	public void onNeighborChanged(final BlockState neighborState, final BlockPos neighborPos, final Direction neighborSide) {
		this.revalidateTick();
	}

	@Override
	protected boolean isEnergyGenerator() {
		return true;
	}

	@Override
	protected long getMaxEnergyAmperage() {
		return 1;
	}

	@Override
	public RecipePerTickFailureAction getPerTickFailureAction() {
		return RecipePerTickFailureAction.NOTHING;
	}
}
