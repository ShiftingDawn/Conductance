package conductance.init.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.world.AuxiliaryLightManager;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import lombok.Getter;
import conductance.init.ConductanceBlockEntities;

public final class CreativeTankBlockEntity extends BlockEntity {

	private final @Getter IFluidHandler fluidHandler = new FluidHandler();
	private @Getter FluidStack fluid = FluidStack.EMPTY;

	public CreativeTankBlockEntity(final BlockPos pos, final BlockState blockState) {
		super(ConductanceBlockEntities.CREATIVE_TANK.get(), pos, blockState);
	}

	public void setFluid(final FluidStack fluid) {
		this.fluid = fluid.copy();
		this.setChanged();
	}

	public boolean isLocked() {
		return this.getBlockState().getValue(CreativeTankBlock.LOCKED);
	}

	public void updateLight(final BlockGetter level) {
		if (!this.fluid.isEmpty()) {
			final AuxiliaryLightManager lightManager = level.getAuxLightManager(this.getBlockPos());
			if (lightManager != null) {
				lightManager.setLightAt(this.getBlockPos(), this.fluid.getFluidType().getLightLevel(this.fluid));
			}
		}
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (this.getLevel() == null) {
			return;
		}
		final BlockState state = this.getBlockState();
		this.getLevel().sendBlockUpdated(this.getBlockPos(), state, state, Block.UPDATE_ALL);
	}

	@Override
	public CompoundTag getUpdateTag(final HolderLookup.Provider registries) {
		return super.saveWithoutMetadata(registries);
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	protected void saveAdditional(final ValueOutput output) {
		super.saveAdditional(output);
		output.store("fluid", FluidStack.CODEC, this.fluid);
	}

	@Override
	protected void loadAdditional(final ValueInput input) {
		super.loadAdditional(input);
		this.fluid = input.read("fluid", FluidStack.CODEC).orElse(FluidStack.EMPTY);
	}

	private final class FluidHandler implements IFluidHandler {

		@Override
		public int getTanks() {
			return 1;
		}

		@Override
		public FluidStack getFluidInTank(final int tank) {
			return CreativeTankBlockEntity.this.fluid.copyWithAmount(Integer.MAX_VALUE);
		}

		@Override
		public int getTankCapacity(final int tank) {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isFluidValid(final int tank, final FluidStack stack) {
			return stack.isEmpty() && CreativeTankBlockEntity.this.fluid.isEmpty() || FluidStack.isSameFluidSameComponents(CreativeTankBlockEntity.this.fluid, stack);
		}

		@Override
		public int fill(final FluidStack resource, final FluidAction action) {
			if (!this.isFluidValid(0, resource)) {
				return 0;
			}
			return resource.getAmount();
		}

		@Override
		public FluidStack drain(final FluidStack resource, final FluidAction action) {
			if (CreativeTankBlockEntity.this.fluid.isEmpty() || resource.isEmpty() || !FluidStack.isSameFluidSameComponents(CreativeTankBlockEntity.this.fluid, resource)) {
				return FluidStack.EMPTY;
			}
			return resource.copy();
		}

		@Override
		public FluidStack drain(final int maxDrain, final FluidAction action) {
			if (CreativeTankBlockEntity.this.fluid.isEmpty()) {
				return FluidStack.EMPTY;
			}
			return CreativeTankBlockEntity.this.fluid.copyWithAmount(maxDrain);
		}
	}
}
