package conductance.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import conductance.api.NCSoundEvents;
import conductance.api.plugin.ConductancePluginListener;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
public final class ConductanceSoundEvents {

	private static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Conductance.MODID);

	public static void initialize(final IEventBus modEventBus) {
		ConductanceSoundEvents.REGISTRY.register(modEventBus);
		NCSoundEvents.TOOL_WRENCH = ConductanceSoundEvents.REGISTRY.register("tool_wrench", SoundEvent::createVariableRangeEvent);
		NCSoundEvents.TOOL_WIRE_CUTTERS = ConductanceSoundEvents.REGISTRY.register("tool_wire_cutters", SoundEvent::createVariableRangeEvent);
		NCSoundEvents.TOOL_HAMMER = ConductanceSoundEvents.REGISTRY.register("tool_hammer", SoundEvent::createVariableRangeEvent);
		NCSoundEvents.TOOL_CROWBAR = ConductanceSoundEvents.REGISTRY.register("tool_crowbar", SoundEvent::createVariableRangeEvent);
	}

	private ConductanceSoundEvents() {
	}
}
