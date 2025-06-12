package conductance.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;

@OnlyIn(Dist.CLIENT)
public final class ClientProxy {

	public static void init(final IEventBus modEventBus) {
	}

	private ClientProxy() {
	}
}
