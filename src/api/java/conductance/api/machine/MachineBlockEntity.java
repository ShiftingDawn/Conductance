package conductance.api.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public class MachineBlockEntity<T extends MachineBlockEntity<T>> extends BlockEntity {

	private final @Getter Map<String, MachineCapability> capabilities = new ConcurrentHashMap<>();
	private final @Getter MachineType<T> machineType;

	public MachineBlockEntity(final MachineType<T> type, final BlockPos pos, final BlockState blockState) {
		super(type.getBlockEntityType().get(), pos, blockState);
		this.machineType = type;
	}

	//region Capabilities
	public final void registerCapability(final String key, final MachineCapability capability) {
		if (this.capabilities.containsKey(key) || this.capabilities.containsValue(capability)) {
			return;
		}
		this.capabilities.put(key, capability);
	}

	public final <C extends MachineCapability> Optional<C> getCapability(final Class<C> type, final boolean exact) {
		for (final MachineCapability capability : this.capabilities.values()) {
			if (exact) {
				if (type.equals(capability.getClass())) {
					return Optional.of(type.cast(capability));
				}
			} else if (type.isAssignableFrom(capability.getClass())) {
				return Optional.of(type.cast(capability));
			}
		}
		return Optional.empty();
	}

	public final <C extends MachineCapability> Optional<C> getCapability(final Class<C> type) {
		return this.getCapability(type, false);
	}

	public Optional<IItemHandler> getItemTransferCapability(@Nullable final Direction side) {
		final List<IItemHandlerModifiable> itemHandlers = new ArrayList<>();
		for (final MachineCapability capability : this.capabilities.values()) {
			if (capability instanceof final IItemHandlerModifiable cap2 && capability.isValid(side)) {
				itemHandlers.add(cap2);
			}
		}
		if (itemHandlers.isEmpty()) {
			return Optional.empty();
		}
		final CapIO io = CapIO.BOTH;
		final IOItemHandlerList handlerList = new IOItemHandlerList(itemHandlers, io);
		return Optional.of(handlerList);
	}

	public Optional<IFluidHandler> getFluidTransferCapability(@Nullable final Direction side) {
		final List<IFluidHandler> fluidHandlers = new ArrayList<>();
		for (final MachineCapability capability : this.capabilities.values()) {
			if (capability instanceof final IFluidHandler cap2 && capability.isValid(side)) {
				fluidHandlers.add(cap2);
			}
		}
		if (fluidHandlers.isEmpty()) {
			return Optional.empty();
		}
		final CapIO io = CapIO.BOTH;
		final IOFluidHandlerList handlerList = new IOFluidHandlerList(fluidHandlers, io);
		return Optional.of(handlerList);
	}
	//endregion

	@Override
	protected void saveAdditional(final ValueOutput output) {
		super.saveAdditional(output);
		this.capabilities.forEach((key, cap) -> cap.serialize(output.child(key)));
	}

	@Override
	protected void loadAdditional(final ValueInput input) {
		super.loadAdditional(input);
		this.capabilities.forEach((key, cap) -> input.child(key).ifPresent(cap::deserialize));
	}
}
