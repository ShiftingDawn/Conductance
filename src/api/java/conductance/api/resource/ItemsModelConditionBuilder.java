package conductance.api.resource;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;

public interface ItemsModelConditionBuilder extends JsonResourceBuilder<ItemsModelConditionBuilder> {

	ItemsModelConditionBuilder onTrue(Consumer<ItemsModelBuilder> builder);

	ItemsModelConditionBuilder onFalse(Consumer<ItemsModelBuilder> builder);

	void component(ResourceLocation predicate, String value);

	default void component(final String predicate, final String value) {
		this.component(ResourceLocation.parse(predicate), value);
	}

	void hasComponent(ResourceLocation component, boolean ignoreDefault);

	default void hasComponent(final ResourceLocation component) {
		this.hasComponent(component, false);
	}

	default void hasComponent(final String component, final boolean ignoreDefault) {
		this.hasComponent(ResourceLocation.parse(component), ignoreDefault);
	}

	default void hasComponent(final String component) {
		this.hasComponent(component, false);
	}

	void keybindDown(String keybind);

	void customModelData(int index);
}
