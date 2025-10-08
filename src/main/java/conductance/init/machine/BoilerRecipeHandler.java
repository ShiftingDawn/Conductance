package conductance.init.machine;

import java.util.function.IntSupplier;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCMaterialGenerationHandlers;
import conductance.api.NCMaterials;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineCapability;
import conductance.api.machine.MachineCapabilityFluidHandler;
import conductance.api.machine.MachineTick;

final class BoilerRecipeHandler extends MachineCapability {

	private static final int MAX_PRODUCTION = 120 / 4; //4 Times per second
	private static final int WATER_TO_STEAM = 10;
	private static final int MAX_WATER = BoilerRecipeHandler.MAX_PRODUCTION / BoilerRecipeHandler.WATER_TO_STEAM;
	private final @Getter MachineCapabilityFluidHandler waterTank;
	private final @Getter MachineCapabilityFluidHandler steamTank;
	private final IntSupplier consumerFunction;
	private @Nullable MachineTick tick;
	private int fuelTime = 0;

	BoilerRecipeHandler(final MachineBlockEntity<?> machine, final MachineCapabilityFluidHandler waterTank, final MachineCapabilityFluidHandler steamTank, final IntSupplier consumerFunction) {
		super("boiler", machine);
		this.addChangedListener(machine::syncToClient);
		this.waterTank = waterTank;
		this.waterTank.addChangedListener(this::setChanged);
		this.waterTank.addChangedListener(this::revalidateTick);
		this.steamTank = steamTank;
		this.steamTank.addChangedListener(this::setChanged);
		this.steamTank.addChangedListener(this::revalidateTick);
		this.consumerFunction = consumerFunction;
	}


	@Override
	public void serialize(final ValueOutput output) {
		output.putInt("fuel", this.fuelTime);
	}

	@Override
	public void deserialize(final ValueInput input) {
		this.fuelTime = input.getIntOr("fuel", 0);
	}

	public void revalidateTick() {
		if (this.getMachine().isServerSide()) {
			this.tick = this.getMachine().addTick(this::tick, this.tick);
		}
	}

	private void tick() {
		if (this.getMachine().haveTicksPassed(5)) {
			this.updateWorkingState();
			if (this.fuelTime == 0) {
				final int fuel = this.consumerFunction.getAsInt();
				if (fuel == 0) {
					this.reset();
					return;
				}
				this.fuelTime = fuel;
				this.setChanged();
			}
			int water = this.waterTank.getFluidInTank(0).getAmount();
			if (water == 0) {
				this.reset();
				return;
			}
			if (water > BoilerRecipeHandler.MAX_WATER) {
				water = BoilerRecipeHandler.MAX_WATER;
			}
			final int space = this.steamTank.getTankCapacity(0) - this.steamTank.getFluidInTank(0).getAmount();
			if (space == 0) {
				this.reset();
				return;
			}
			int maxProduce = this.fuelTime * 12;
			if (maxProduce > BoilerRecipeHandler.MAX_PRODUCTION) {
				maxProduce = BoilerRecipeHandler.MAX_PRODUCTION;
			}
			if (maxProduce > space) {
				maxProduce = space;
			}
			if (maxProduce > water * BoilerRecipeHandler.WATER_TO_STEAM) {
				maxProduce = water * BoilerRecipeHandler.WATER_TO_STEAM;
			}
			final int fuelToConsume = maxProduce / 12;
			maxProduce = fuelToConsume * 12;
			if (maxProduce == 0) {
				this.reset();
				return;
			}
			this.waterTank.drainInternal(maxProduce / BoilerRecipeHandler.WATER_TO_STEAM, IFluidHandler.FluidAction.EXECUTE);
			this.steamTank.fillInternal(CAPI.materials().getFluid(NCMaterials.STEAM, NCMaterialGenerationHandlers.GAS, maxProduce), IFluidHandler.FluidAction.EXECUTE);
			this.fuelTime -= fuelToConsume;
			if (this.fuelTime == 0) {
				this.reset();
			} else {
				this.setChanged();
			}
		}
	}

	private void updateWorkingState() {
		this.getMachine().setWorkingState(this.fuelTime > 0);
	}

	private void reset() {
		if (this.tick != null) {
			this.tick.invalidate();
		}
		this.updateWorkingState();
		this.setChanged();
	}

	@Override
	public void onLoad() {
		super.onLoad();
		this.revalidateTick();
	}
}
