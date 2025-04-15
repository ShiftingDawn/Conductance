package conductance.api.machine;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import conductance.api.capability.CapabilityHelper;
import conductance.api.capability.energy.EnergyHandlerList;
import conductance.api.capability.energy.IEnergyHandler;

public interface IMachineBlock<T extends MachineBlockEntity<T>> extends EntityBlock {

	default Block self() {
		return (Block) this;
	}

	MachineType<T> getMachineType();

	default void setMachine(final BlockGetter level, final BlockPos pos, final Consumer<MachineBlockEntity<?>> setter) {
		if (level.getBlockEntity(pos) instanceof final MachineBlockEntity<?> machine) {
			setter.accept(machine);
		}
	}

	@Contract("_, _, _, !null -> !null; _, _, _, null -> null")
	@Nullable
	default <R> R getMachine(final BlockGetter level, final BlockPos pos, final Function<MachineBlockEntity<?>, R> getter, @Nullable final R fallback) {
		if (level.getBlockEntity(pos) instanceof final MachineBlockEntity<?> machine) {
			final R result = getter.apply(machine);
			if (result != null) {
				return result;
			}
		}
		return fallback;
	}

	@NotNull
	default <R> R getMachine(final BlockGetter level, final BlockPos pos, final Function<MachineBlockEntity<?>, R> getter, @NotNull final Supplier<R> fallback) {
		if (level.getBlockEntity(pos) instanceof final MachineBlockEntity<?> machine) {
			final R result = getter.apply(machine);
			if (result != null) {
				return result;
			}
		}
		return fallback.get();
	}

	default void attachCapabilities(final RegisterCapabilitiesEvent event) {
		event.registerBlock(Capabilities.ItemHandler.BLOCK, (level, blockPos, blockState, blockEntity, direction) -> {
			if (blockEntity instanceof final MachineBlockEntity<?> machine) {
				return machine.getItemTransferCapability(direction, true);
			}
			return null;
		}, this.self());
		event.registerBlock(Capabilities.FluidHandler.BLOCK, (level, blockPos, blockState, blockEntity, direction) -> {
			if (blockEntity instanceof final MachineBlockEntity<?> machine) {
				return machine.getFluidTransferCapability(direction, true);
			}
			return null;
		}, this.self());
		event.registerBlock(CapabilityHelper.ENERGY_HANDLER_BLOCK, (level, blockPos, blockState, blockEntity, direction) -> {
			if (blockEntity instanceof final IEnergyHandler handler) {
				return handler;
			}
			if (blockEntity instanceof final MachineBlockEntity<?> machine) {
				final List<IEnergyHandler> handlers = machine.getCapabilities().stream()
						.filter(cap -> cap instanceof IEnergyHandler && cap.hasCapability(direction))
						.map(IEnergyHandler.class::cast)
						.toList();
				if (!handlers.isEmpty()) {
					return handlers.size() == 1 ? handlers.getFirst() : new EnergyHandlerList(handlers);
				}
			}
			return null;
		}, this.self());
	}
}
