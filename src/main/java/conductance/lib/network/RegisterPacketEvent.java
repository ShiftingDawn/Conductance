package conductance.lib.network;

import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import conductance.api.plugin.IConductancePluginEvent;

@RequiredArgsConstructor
public final class RegisterPacketEvent implements IConductancePluginEvent {

	private final @Getter PayloadRegistrar registrar;
}
