package conductance.core;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import conductance.Conductance;

@Mod(value = Conductance.MODID, dist = Dist.DEDICATED_SERVER)
public final class ConductanceServer extends Conductance {

	public ConductanceServer(final IEventBus modEventBus, final ModContainer modContainer) {
		super(Dist.DEDICATED_SERVER, modEventBus, modContainer);
	}
}
