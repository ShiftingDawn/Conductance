package conductance.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.common.NeoForge;

@OnlyIn(Dist.CLIENT)
public final class ClientProxy {

	public static void init(final IEventBus modEventBus) {
		NeoForge.EVENT_BUS.addListener(RenderHighlightEvent.Block.class, event -> {
			ExtendedInteractionRenderer.renderBlockHighLight(event.getPoseStack(), event.getCamera(), event.getTarget(), event.getMultiBufferSource(), event.getDeltaTracker().getGameTimeDeltaTicks());
		});
	}

	private ClientProxy() {
	}
}
