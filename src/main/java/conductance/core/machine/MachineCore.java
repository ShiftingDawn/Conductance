package conductance.core.machine;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import conductance.api.machine.IMachineBlock;
import conductance.api.machine.event.RegisterMachineEvent;
import conductance.Conductance;
import conductance.init.block.WireBlock;

public final class MachineCore {

	public static void initialize(final IEventBus modEventBus) {
		modEventBus.addListener(MachineCore::onAttachCapabilities);
		Conductance.dispatchAll(RegisterMachineEvent.class, new RegisterMachineEventImpl(MachineBuilderImpl::new));
	}

	private static void onAttachCapabilities(final RegisterCapabilitiesEvent event) {
		BuiltInRegistries.BLOCK.forEach(block -> {
			if (block instanceof final IMachineBlock<?> machineBlock) {
				machineBlock.attachCapabilities(event);
			} else if (block instanceof final WireBlock wireBlock) {
				wireBlock.attachCapabilities(event);
			}
		});
	}

	private MachineCore() {
	}
}
