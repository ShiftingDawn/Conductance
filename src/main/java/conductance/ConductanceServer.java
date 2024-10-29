package conductance;


import conductance.core.CommonProxy;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = Conductance.MODID, dist = Dist.DEDICATED_SERVER)
public final class ConductanceServer extends Conductance {

	public ConductanceServer(final IEventBus modEventBus, final ModContainer modContainer) {
		super(modEventBus, modContainer);
	}

	@Override
	protected CommonProxy createSidedProxy() {
		return new CommonProxy();
	}
}