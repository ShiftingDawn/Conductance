package conductance.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import conductance.Conductance;

@Mod(value = Conductance.MODID, dist = Dist.CLIENT)
public final class ConductanceClient extends Conductance {

	public ConductanceClient(final IEventBus modEventBus, final ModContainer modContainer) {
		super(Dist.CLIENT, modEventBus, modContainer);
	}
}
