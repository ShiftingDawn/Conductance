package conductance.init.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import lombok.Getter;
import conductance.api.CAPI;
import conductance.api.NCMaterialGenerationHandlers;
import conductance.api.NCMaterials;
import conductance.api.machine.CapIO;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineCapabilityFluidHandler;
import conductance.api.machine.MachineFluidHandler;
import conductance.api.machine.MachineType;

public abstract class AbstractSteamBoilerMachine<T extends AbstractSteamBoilerMachine<T>> extends MachineBlockEntity<T> {

	private final @Getter MachineCapabilityFluidHandler waterTank;
	private final @Getter MachineCapabilityFluidHandler steamTank;

	public AbstractSteamBoilerMachine(final MachineType<T> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
		this.waterTank = CAPI.make(new MachineCapabilityFluidHandler("water", this, new MachineFluidHandler(1, CAPI.BUCKET * 4)), handler -> {
			handler.setIoMode(CapIO.IN);
			handler.getHandler().setFilter((tank, stack) -> stack.is(CAPI.materials().getFluidTag(NCMaterials.WATER, NCMaterialGenerationHandlers.LIQUID)));
		});
		this.steamTank = CAPI.make(new MachineCapabilityFluidHandler("steam", this, new MachineFluidHandler(1, CAPI.BUCKET * 4)), handler -> {
			handler.setIoMode(CapIO.OUT);
			handler.getHandler().setFilter((tank, stack) -> stack.is(CAPI.materials().getFluidTag(NCMaterials.STEAM, NCMaterialGenerationHandlers.GAS)));
		});
	}


}
