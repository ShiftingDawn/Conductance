package conductance.api.machine.api;

import java.util.function.Consumer;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import conductance.api.util.Internal;

public interface IRequesterBlockEntity extends IFeatureBase {

	default void sendToClient(final int requestId, @Nullable final Consumer<ValueOutput> payloadProvider) {
		this.onClient(level -> Internal.MACHINE_RPC_PACKET_SENDER.send(level, this, payload -> {
			payload.putInt("r", requestId);
			if (payloadProvider != null) {
				payloadProvider.accept(payload.child("d"));
			}
		}));
	}

	default void sendToServer(final int requestId, @Nullable final Consumer<ValueOutput> payloadProvider) {
		this.onServer(level -> Internal.MACHINE_RPC_PACKET_SENDER.send(level, this, payload -> {
			payload.putInt("r", requestId);
			if (payloadProvider != null) {
				payloadProvider.accept(payload.child("d"));
			}
		}));
	}

	default void handleRequestFromServer(final int requestId, final ValueInput payload) {
	}

	default void handleRequestFromClient(final int requestId, final ValueInput payload) {
	}
}
