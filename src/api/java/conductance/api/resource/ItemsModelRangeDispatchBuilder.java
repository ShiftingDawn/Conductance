package conductance.api.resource;

import java.util.function.Consumer;

public interface ItemsModelRangeDispatchBuilder extends JsonResourceBuilder<ItemsModelRangeDispatchBuilder> {

	enum CompassTarget {
		SPAWN, LODESTONE, RECOVERY, NONE;
	}

	enum TimeSource {
		DAYTIME, MOON_PHASE, RANDOM
	}

	ItemsModelRangeDispatchBuilder scale(float scale);

	ItemsModelRangeDispatchBuilder entry(float threshold, Consumer<ItemsModelBuilder> builder);

	ItemsModelRangeDispatchBuilder fallback(Consumer<ItemsModelBuilder> builder);

	void compass(CompassTarget target, boolean wobble);

	default void compass(final CompassTarget target) {
		this.compass(target, true);
	}

	void stackSize(boolean normalize);

	void damage(boolean normalize);

	void time(TimeSource source, boolean wobble);

	default void time(final TimeSource source) {
		this.time(source, true);
	}

	void useCycle(float period);

	void useDuration(boolean remaining);

	void customModelData(int index);
}
