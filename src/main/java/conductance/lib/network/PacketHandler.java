package conductance.lib.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public final class PacketHandler {

	public static void initialize(final RegisterPayloadHandlersEvent event) {
		final var registrar = event.registrar("1");
	}

	private PacketHandler() {
	}
}
