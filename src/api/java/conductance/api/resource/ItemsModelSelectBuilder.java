package conductance.api.resource;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.Property;

public interface ItemsModelSelectBuilder extends JsonResourceBuilder<ItemsModelSelectBuilder> {

	ItemsModelSelectBuilder addCase(String when, Consumer<ItemsModelBuilder> builder);

	ItemsModelSelectBuilder fallback(Consumer<ItemsModelBuilder> builder);

	void blockState(String property);

	default void blockState(final Property<?> property) {
		this.blockState(property.getName());
	}

	void component(ResourceLocation predicate);

	default void component(final String predicate) {
		this.component(ResourceLocation.parse(predicate));
	}

	void localTime(String pattern, @Nullable String locale, @Nullable String timezone);

	void customModelData(int index);
}
