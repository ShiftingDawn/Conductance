package conductance.core.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import lombok.Getter;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineType;
import conductance.api.tier.Tier;

public final class MachineHullMachine extends MachineBlockEntity<MachineHullMachine> {

	private final @Getter Tier tier;

	public MachineHullMachine(final MachineType<MachineHullMachine> type, final BlockPos pos, final BlockState blockState, final Tier tier) {
		super(type, pos, blockState);
		this.tier = tier;
	}
}
