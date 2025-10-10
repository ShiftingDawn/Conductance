package conductance.init.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.block.BlockRotationHelper;
import conductance.api.machine.MachineType;
import conductance.api.tier.Tier;

public class GenericGeneratorMachine extends GenericRecipeMachine {

	public GenericGeneratorMachine(final MachineType<GenericRecipeMachine> type, final Tier tier, final BlockPos pos, final BlockState blockState) {
		super(type, tier, pos, blockState);
		this.getEnergy().setCapabilityValidator(side -> side == BlockRotationHelper.getFacing(this.getBlockState()));
		this.getEnergy().setCapabilityValidator(side -> side == this.getFacing());
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

	@Override
	protected boolean isEnergyGenerator() {
		return true;
	}

	@Override
	protected long getMaxEnergyAmperage() {
		return 1;
	}
}
