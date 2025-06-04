package conductance.init.machine;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.widget.ProgressWidget;
import com.lowdragmc.lowdraglib.gui.widget.TankWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.lowdragmc.lowdraglib.side.fluid.FluidTransferHelper;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.NCMaterials;
import conductance.api.machine.MachineRunnable;
import conductance.api.machine.MachineType;
import conductance.api.machine.capability.MachineRecipeCapabilityFluids;
import conductance.api.machine.gui.GuiTextures;
import conductance.api.machine.gui.GuiTheme;
import conductance.api.machine.gui.MachineGuiHolder;
import conductance.api.machine.sync.Persisted;
import conductance.api.machine.sync.Synchronized;
import conductance.api.util.IOMode;
import conductance.api.util.world.WorldUtils;
import conductance.client.MachineUIFactory;

public abstract class SteamBoilerMachine<T extends SteamBoilerMachine<T>> extends SteamWorkableMachine<T> implements MachineGuiHolder {

	private static final int TICKRATE_AUTO_OUTPUT = 5;
	private static final int TICKRATE_INCREASE_TEMPERATURE = 12;
	private static final int TICKRATE_MAIN_LOOP = 10;

	@Persisted
	@Synchronized
	protected final MachineRecipeCapabilityFluids waterTank;
	@Persisted
	@Synchronized
	protected int currentTemperature;
	@Persisted
	protected int timeBeforeCoolingDown;
	@Getter
	protected boolean hasNoWater;
	@Nullable
	protected MachineRunnable temperatureSubs, autoOutputSubs;
	@Nullable
	protected ISubscription steamTankListener;

	public SteamBoilerMachine(final MachineType<T> machineType, final BlockPos pos, final BlockState blockState) {
		super(machineType, pos, blockState);
		this.waterTank = new MachineRecipeCapabilityFluids(this, 1, 16 * FluidHelper.getBucket(), IOMode.INPUT);
		this.waterTank.setFilter(fluid -> fluid.getFluid() == Fluids.WATER);
	}

	@Override
	protected MachineRecipeCapabilityFluids createSteamTank() {
		return new MachineRecipeCapabilityFluids(this, 1, 16 * FluidHelper.getBucket(), IOMode.OUTPUT);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		if (this.getLevel() instanceof final ServerLevel serverLevel) {
			serverLevel.getServer().tell(new TickTask(0, this::updateAutoOutputSubscription));
		}
		this.updateSteamSubscription();
		this.steamTankListener = this.steamTank.addChangedListener(this::updateAutoOutputSubscription);
	}

	@Override
	public void onUnload() {
		super.onUnload();
		if (this.steamTankListener != null) {
			this.steamTankListener.unsubscribe();
		}
	}

	@Override
	public void onNeighborChanged(final BlockPos neighborPos, final BlockState neighborState, final Direction neighborSide) {
		super.onNeighborChanged(neighborPos, neighborState, neighborSide);
		this.updateAutoOutputSubscription();
	}

	protected void updateAutoOutputSubscription() {
		for (final Direction d : Direction.values()) {
			if (d != this.getFrontFacing() && d != Direction.DOWN) {
				final var h = FluidTransferHelper.getFluidTransfer(this.getLevel(), this.getBlockPos().relative(d.getOpposite()), d.getOpposite());
				if (h != null) {
					this.autoOutputSubs = this.addTick(this.autoOutputSubs, this::autoOutput);
					return;
				}
			}
		}
		if (this.autoOutputSubs != null) {
			this.autoOutputSubs.invalidate();
			this.autoOutputSubs = null;
		}
	}

	protected void autoOutput() {
		if (this.hasTicksPassed(SteamBoilerMachine.TICKRATE_AUTO_OUTPUT)) {
			this.steamTank.exportToNearby(Direction.stream().filter(d -> d != this.getFrontFacing()).toArray(Direction[]::new));
			this.updateAutoOutputSubscription();
		}
	}

	protected void updateSteamSubscription() {
		if (this.currentTemperature > 0) {
			this.temperatureSubs = this.addTick(this.temperatureSubs, this::updateCurrentTemperature);
		} else if (this.temperatureSubs != null) {
			this.temperatureSubs.invalidate();
			this.temperatureSubs = null;
		}
	}

	protected void updateCurrentTemperature() {
		if (this.getRecipeProcessor().isWorking()) {
			if (this.hasTicksPassed(SteamBoilerMachine.TICKRATE_INCREASE_TEMPERATURE)) {
				if (this.currentTemperature < this.getMaxTemperature()) {
					++this.currentTemperature;
				}
			}
		} else if (this.timeBeforeCoolingDown == 0) {
			if (this.currentTemperature > 0) {
				this.currentTemperature -= this.getCoolDownRate();
				this.timeBeforeCoolingDown = this.getCooldownInterval();
			}
		} else {
			--this.timeBeforeCoolingDown;
		}

		if (this.hasTicksPassed(SteamBoilerMachine.TICKRATE_MAIN_LOOP)) {
			if (this.currentTemperature >= 100) {
				final int fillAmount = (int) (this.getSteamPerSecond() * (this.currentTemperature / (this.getMaxTemperature() * 1.0)) / (20.0 / SteamBoilerMachine.TICKRATE_MAIN_LOOP));
				final boolean hasDrainedWater = !this.waterTank.drainInternal(1, IFluidHandler.FluidAction.EXECUTE).isEmpty();
				var filledSteam = 0L;
				if (hasDrainedWater) {
					filledSteam = this.steamTank.fillInternal(CAPI.materials().getFluid(NCMaterialTaggedSets.GAS, NCMaterials.STEAM, fillAmount), IFluidHandler.FluidAction.EXECUTE);
				}
				if (this.hasNoWater && hasDrainedWater) {
					WorldUtils.explode(this.getLevel(), this.getBlockPos(), 2.0f);
				} else {
					this.hasNoWater = !hasDrainedWater;
				}
				if (filledSteam == 0 && hasDrainedWater && this.getLevel() instanceof final ServerLevel serverLevel) {
					final float x = this.getBlockPos().getX() + 0.5F;
					final float y = this.getBlockPos().getY() + 0.5F;
					final float z = this.getBlockPos().getZ() + 0.5F;

					serverLevel.sendParticles(ParticleTypes.CLOUD,
							x + this.getFrontFacing().getStepX() * 0.6,
							y + this.getFrontFacing().getStepY() * 0.6,
							z + this.getFrontFacing().getStepZ() * 0.6,
							7 + CAPI.RANDOM.nextInt(3),
							this.getFrontFacing().getStepX() / 2.0,
							this.getFrontFacing().getStepY() / 2.0,
							this.getFrontFacing().getStepZ() / 2.0, 0.1);
					// bypass capability check for special case behavior
					this.steamTank.drainInternal(FluidType.BUCKET_VOLUME * 4, IFluidHandler.FluidAction.EXECUTE);
				}
			} else {
				this.hasNoWater = false;
			}
		}
		this.updateSteamSubscription();
	}

	@Override
	public void onWorking() {
		if (this.currentTemperature < this.getMaxTemperature()) {
			this.currentTemperature = Math.max(1, this.currentTemperature);
			this.updateSteamSubscription();
		}
	}

	@Override
	public void afterWorking() {
		this.timeBeforeCoolingDown = this.getCooldownInterval();
	}

	protected int getCooldownInterval() {
		return 45;
	}

	protected int getCoolDownRate() {
		return 1;
	}

	public int getMaxTemperature() {
		return 1000;
	}

	protected double getTemperaturePercent() {
		return this.currentTemperature / (this.getMaxTemperature() * 1.0);
	}

	protected long getSteamPerSecond() {
		return 1280;
	}

	protected double getBurnTimePercentage() {
		return this.getRecipeProcessor().getProgressMax() == 0 ? 0 : 1.0 - this.getRecipeProcessor().getProgressPercentage();
	}

	@Override
	public void onAnimateTick(final RandomSource random) {
		if (this.isWorking()) {
			float x = this.getBlockPos().getX() + 0.5F;
			float z = this.getBlockPos().getZ() + 0.5F;
			final float horizontalOffset = random.nextFloat() * 0.6F - 0.3F;
			final float y = this.getBlockPos().getY() + random.nextFloat() * 0.375F;
			if (this.getFrontFacing().getAxis() == Direction.Axis.X) {
				if (this.getFrontFacing().getAxisDirection() == Direction.AxisDirection.POSITIVE) {
					x += 0.52F;
				} else {
					x -= 0.52F;
				}
				z += horizontalOffset;
			} else if (this.getFrontFacing().getAxis() == Direction.Axis.Z) {
				if (this.getFrontFacing().getAxisDirection() == Direction.AxisDirection.POSITIVE) {
					z += 0.52F;
				} else {
					z -= 0.52F;
				}
				x += horizontalOffset;
			}
			this.onRandomDisplayTick(random, x, y, z);
		}
	}

	protected void onRandomDisplayTick(final RandomSource random, final float x, final float y, final float z) {
		this.getLevel().addParticle(ParticleTypes.SMOKE, x, y, z, 0, 0, 0);
		this.getLevel().addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
	}

	@Override
	public ModularUI createUI(final Player entityPlayer) {
		return MachineUIFactory.createGui(this, this, entityPlayer);
	}

	@Override
	public void populateWidgetPanel(final WidgetGroup panel) {
		panel.addWidget(new ProgressWidget(this::getTemperaturePercent, 96, 15, 10, 54)
				.setProgressTexture(GuiTextures.BOILER_SLOT, GuiTextures.BOILER_TEMPERATURE)
				.setFillDirection(ProgressTexture.FillDirection.DOWN_TO_UP)
				.setDynamicHoverTips(pct -> I18n.get("tooltip.conductance.boiler.temperature", (int) (this.currentTemperature + 274.15), (int) (this.getMaxTemperature() + 274.15)))
		);
		panel.addWidget(new TankWidget(this.waterTank.getFluidTanks()[0], 83, 15, 10, 54, false, true)
				.setShowAmount(false)
				.setFillDirection(ProgressTexture.FillDirection.DOWN_TO_UP)
				.setBackground(GuiTextures.BOILER_SLOT)
		);
		panel.addWidget(new TankWidget(this.steamTank.getFluidTanks()[0], 70, 15, 10, 54, true, false)
				.setShowAmount(false)
				.setFillDirection(ProgressTexture.FillDirection.DOWN_TO_UP)
				.setBackground(GuiTextures.BOILER_SLOT)
		);
	}

	@Override
	public GuiTheme getGuiTheme() {
		return GuiTheme.THEME_BRONZE;
	}
}
