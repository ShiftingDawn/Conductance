package conductance;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import conductance.client.ClientProxy;
import conductance.core.CommonProxy;

@Mod(value = Conductance.MODID, dist = Dist.CLIENT)
public final class ConductanceClient extends Conductance {

	public ConductanceClient(final IEventBus modEventBus, final ModContainer modContainer) {
		super(modEventBus, modContainer);
	}

	@Override
	protected CommonProxy createSidedProxy() {
		return new ClientProxy();
	}
}
