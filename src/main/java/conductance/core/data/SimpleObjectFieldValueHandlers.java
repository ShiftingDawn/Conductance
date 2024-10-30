package conductance.core.data;

import java.util.UUID;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.machine.data.ManagedObjectValueHandler;

final class SimpleObjectFieldValueHandlers {

	private SimpleObjectFieldValueHandlers() {
	}

	public static class HandlerString extends ManagedObjectValueHandler<String> {

		HandlerString() {
			super(String.class, true);
		}
	}

	public static class HandlerResourceLocation extends ManagedObjectValueHandler<ResourceLocation> {

		HandlerResourceLocation() {
			super(ResourceLocation.class, true);
		}
	}

	public static class HandlerUUID extends ManagedObjectValueHandler<UUID> {

		HandlerUUID() {
			super(UUID.class, true);
		}
	}

	public static class HandlerBlockState extends ManagedObjectValueHandler<BlockState> {

		HandlerBlockState() {
			super(BlockState.class, false);
		}
	}
}
