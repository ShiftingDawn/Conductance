package conductance.core.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import conductance.api.CAPI;
import conductance.core.sync.task.S2CSyncPacket;

@EventBusSubscriber(modid = CAPI.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class PacketRegister {

	@SubscribeEvent
	public static void onRegisterPackets(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar("1");
		registrar.playToClient(S2CSyncPacket.TYPE, S2CSyncPacket.CODEC, S2CSyncPacket::handle);
	}

	private PacketRegister() {
	}
}
