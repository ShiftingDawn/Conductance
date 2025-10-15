package conductance.core.coil;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import conductance.api.CAPI;
import conductance.api.coil.CoilBlockRegistry;
import conductance.api.coil.event.RegisterCoilBlockTypeEvent;
import conductance.Conductance;

public final class CoilCore {

	public static void initialize(final IEventBus modEventBus) {
		Conductance.COILS = CAPI.make(new CoilBlockRegistryImpl(), reg -> Conductance.setApiValue(CoilBlockRegistry.class, reg));

		Conductance.dispatch(RegisterCoilBlockTypeEvent.class, modid -> new RegisterCoilBlockTypeEventImpl(((registryName, color, previousCoil) -> {
			final ResourceLocation registryKey = ResourceLocation.fromNamespaceAndPath(modid, registryName);
			final CoilBlockTypeImpl result = new CoilBlockTypeImpl(color);
			if (previousCoil != null) {
				result.setPrevCoil((CoilBlockTypeImpl) previousCoil);
			} else {
				result.setPrevCoil(Conductance.COILS.getLastCoil());
			}
			Conductance.REGISTRIES.register(CAPI.regs().coilBlockTypes(), registryKey, result);
			Conductance.COILS.insertCoil(result);
			return result;
		})));
	}

	private CoilCore() {
	}
}
