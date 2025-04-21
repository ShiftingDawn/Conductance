package conductance.core.machine;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import conductance.api.machine.IMachineBlock;
import conductance.Conductance;
import conductance.block.CableBlock;

@EventBusSubscriber(modid = Conductance.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class MachineEventListeners {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onAttachCapabilities(final RegisterCapabilitiesEvent event) {
		BuiltInRegistries.BLOCK.forEach(block -> {
			if (block instanceof final IMachineBlock<?> machineBlock) {
				machineBlock.attachCapabilities(event);
			} else if (block instanceof final CableBlock cableBlock) {
				cableBlock.attachCapabilities(event);
			}
		});
	}

	private MachineEventListeners() {
	}
}
