package conductance.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import conductance.api.CAPI;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.NCMaterials;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineType;
import conductance.api.machine.capability.MachineRecipeCapabilityFluids;

public abstract class SteamMachine<T extends SteamMachine<T>> extends MachineBlockEntity<T> {

	protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(SteamMachine.class, MachineBlockEntity.MANAGED_FIELD_HOLDER);
	@Persisted
	public final MachineRecipeCapabilityFluids steamTank;

	public SteamMachine(final MachineType<T> machineType, final BlockPos pos, final BlockState blockState) {
		super(machineType, pos, blockState);
		this.steamTank = this.createSteamTank();
		this.steamTank.setFilter(fluid -> fluid.getFluid() == CAPI.materials().getFluidUnsafe(NCMaterialTaggedSets.GAS, NCMaterials.STEAM));
	}

	protected abstract MachineRecipeCapabilityFluids createSteamTank();

	@Override
	public ManagedFieldHolder getFieldHolder() {
		return SteamMachine.MANAGED_FIELD_HOLDER;
	}
}
