package conductance.core.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import conductance.api.CAPI;
import conductance.core.data.datasync.SCSyncPacket;

@EventBusSubscriber(modid = CAPI.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class PacketRegister {

	@SubscribeEvent
	public static void onRegisterPackets(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar("1");
		registrar.playToClient(SCSyncPacket.TYPE, SCSyncPacket.CODEC, SCSyncPacket::handle);
	}

	private PacketRegister() {
	}
}
