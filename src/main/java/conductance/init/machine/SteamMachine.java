package conductance.init.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.CAPI;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.NCMaterials;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineType;
import conductance.api.machine.capability.MachineRecipeCapabilityFluids;
import conductance.api.machine.sync.Persisted;

public abstract class SteamMachine<T extends SteamMachine<T>> extends MachineBlockEntity<T> {

	@Persisted
	public final MachineRecipeCapabilityFluids steamTank;

	public SteamMachine(final MachineType<T> machineType, final BlockPos pos, final BlockState blockState) {
		super(machineType, pos, blockState);
		this.steamTank = this.createSteamTank();
		this.steamTank.setFilter(fluid -> fluid.getFluid() == CAPI.materials().getFluidUnsafe(NCMaterialTaggedSets.GAS, NCMaterials.STEAM));
	}

	protected abstract MachineRecipeCapabilityFluids createSteamTank();
}
