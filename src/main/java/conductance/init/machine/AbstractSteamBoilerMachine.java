package conductance.init.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCMaterialGenerationHandlers;
import conductance.api.NCMaterials;
import conductance.api.machine.CapIO;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineCapabilityFluidHandler;
import conductance.api.machine.MachineFluidHandler;
import conductance.api.machine.MachineTick;
import conductance.api.machine.MachineType;

public abstract class AbstractSteamBoilerMachine<T extends AbstractSteamBoilerMachine<T>> extends MachineBlockEntity<T> {

	private static final int MAX_PRODUCTION = 120 / 4; //4 Times per second
	private static final int WATER_TO_STEAM = 10;
	private static final int MAX_WATER = AbstractSteamBoilerMachine.MAX_PRODUCTION / AbstractSteamBoilerMachine.WATER_TO_STEAM;
	private final @Getter MachineCapabilityFluidHandler waterTank;
	private final @Getter MachineCapabilityFluidHandler steamTank;
	private @Nullable MachineTick tick;
	private int fuelTime = 0;

	public AbstractSteamBoilerMachine(final MachineType<T> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
		this.waterTank = CAPI.make(new MachineCapabilityFluidHandler("water", this, new MachineFluidHandler(1, CAPI.BUCKET * 4)), handler -> {
			handler.setIoMode(CapIO.IN);
			handler.getHandler().setFilter((tank, stack) -> stack.is(CAPI.materials().getFluidTag(NCMaterials.WATER, NCMaterialGenerationHandlers.LIQUID)));
		});
		this.waterTank.addChangedListener(this::setChanged);
		this.waterTank.addChangedListener(this::revalidateTick);
		this.steamTank = CAPI.make(new MachineCapabilityFluidHandler("steam", this, new MachineFluidHandler(1, CAPI.BUCKET * 4)), handler -> {
			handler.setIoMode(CapIO.OUT);
			handler.getHandler().setFilter((tank, stack) -> stack.is(CAPI.materials().getFluidTag(NCMaterials.STEAM, NCMaterialGenerationHandlers.GAS)));
		});
		this.steamTank.addChangedListener(this::setChanged);
		this.steamTank.addChangedListener(this::revalidateTick);
	}

	protected abstract int consumeSingularInput();

	protected final void revalidateTick() {
		if (this.isServerSide()) {
			this.tick = this.addTick(this::tick, this.tick);
		}
	}

	private void tick() {
		if (this.isServerSide() && this.haveTicksPassed(5)) {
			this.updateWorkingState();
			if (this.fuelTime == 0) {
				final int fuel = this.consumeSingularInput();
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
			if (water > AbstractSteamBoilerMachine.MAX_WATER) {
				water = AbstractSteamBoilerMachine.MAX_WATER;
			}
			final int space = this.steamTank.getTankCapacity(0) - this.steamTank.getFluidInTank(0).getAmount();
			if (space == 0) {
				this.reset();
				return;
			}
			int maxProduce = this.fuelTime * 12;
			if (maxProduce > AbstractSteamBoilerMachine.MAX_PRODUCTION) {
				maxProduce = AbstractSteamBoilerMachine.MAX_PRODUCTION;
			}
			if (maxProduce > space) {
				maxProduce = space;
			}
			if (maxProduce > water * AbstractSteamBoilerMachine.WATER_TO_STEAM) {
				maxProduce = water * AbstractSteamBoilerMachine.WATER_TO_STEAM;
			}
			final int fuelToConsume = maxProduce / 12;
			maxProduce = fuelToConsume * 12;
			if (maxProduce == 0) {
				this.reset();
				return;
			}
			this.waterTank.drainInternal(maxProduce / AbstractSteamBoilerMachine.WATER_TO_STEAM, IFluidHandler.FluidAction.EXECUTE);
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
		this.setWorkingState(this.fuelTime > 0);
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

	@Override
	public void setChanged() {
		super.setChanged();
		this.syncToClient();
	}

	@Override
	protected void saveAdditional(final ValueOutput output) {
		super.saveAdditional(output);
		output.putInt("fuel", this.fuelTime);
	}

	@Override
	protected void loadAdditional(final ValueInput input) {
		super.loadAdditional(input);
		this.fuelTime = input.getIntOr("fuel", 0);
	}
}
