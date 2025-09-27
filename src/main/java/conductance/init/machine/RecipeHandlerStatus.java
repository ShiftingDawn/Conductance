package conductance.init.machine;

import java.util.Locale;
import com.mojang.serialization.Codec;

public enum RecipeHandlerStatus {

	IDLE,
	PAUSED,
	PROCESSING;

	public static final Codec<RecipeHandlerStatus> CODEC = Codec.STRING.xmap(RecipeHandlerStatus::getByName, RecipeHandlerStatus::toString);
	private final String name = super.toString().toLowerCase(Locale.ROOT);

	public static RecipeHandlerStatus getByName(final String name) {
		for (final RecipeHandlerStatus status : RecipeHandlerStatus.values()) {
			if (status.name.equalsIgnoreCase(name)) {
				return status;
			}
		}
		return RecipeHandlerStatus.IDLE;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
