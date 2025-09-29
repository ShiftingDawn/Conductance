package conductance.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import conductance.api.NCDataComponents;
import conductance.Conductance;

public final class ConductanceDataComponents {

	private static final DeferredRegister.DataComponents REGISTRY = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Conductance.MODID);

	public static void initialize(final IEventBus modEventBus) {
		ConductanceDataComponents.REGISTRY.register(modEventBus);
		NCDataComponents.PROGRAM_CIRCUIT = ConductanceDataComponents.REGISTRY.registerComponentType("program_circuit",
			b -> b.persistent(ExtraCodecs.intRange(0, 24)).networkSynchronized(ByteBufCodecs.VAR_INT));
	}

	private ConductanceDataComponents() {
	}
}
