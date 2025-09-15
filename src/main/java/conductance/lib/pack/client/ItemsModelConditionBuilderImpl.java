package conductance.lib.pack.client;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import conductance.api.resource.ItemsModelBuilder;
import conductance.api.resource.ItemsModelConditionBuilder;

@RequiredArgsConstructor
final class ItemsModelConditionBuilderImpl extends JsonResourceBuilderImpl<ItemsModelConditionBuilder> implements ItemsModelConditionBuilder {

	private final ResourceLocation property;
	private @Nullable ItemsModelBuilderImpl onTrue;
	private @Nullable ItemsModelBuilderImpl onFalse;

	@Override
	public ItemsModelConditionBuilder onTrue(final Consumer<ItemsModelBuilder> builder) {
		if (this.onTrue == null) {
			this.onTrue = new ItemsModelBuilderImpl();
		}
		builder.accept(this.onTrue);
		return this;
	}

	@Override
	public ItemsModelConditionBuilder onFalse(final Consumer<ItemsModelBuilder> builder) {
		if (this.onFalse == null) {
			this.onFalse = new ItemsModelBuilderImpl();
		}
		builder.accept(this.onFalse);
		return this;
	}

	@Override
	public void component(final ResourceLocation predicate, final String value) {
		this.addProperty("predicate", predicate.toString());
		this.addProperty("value", value);
	}

	@Override
	public void hasComponent(final ResourceLocation component, final boolean ignoreDefault) {
		this.addProperty("component", component.toString());
		this.addProperty("ignore_default", ignoreDefault);
	}

	@Override
	public void keybindDown(final String keybind) {
		this.addProperty("keybind", keybind);
	}

	@Override
	public void customModelData(final int index) {
		this.addProperty("index", index);
	}

	@Override
	protected void populateJson(final JsonObject json) {
		json.addProperty("type", ResourceLocation.withDefaultNamespace("condition").toString());
		json.addProperty("property", this.property.toString());
		if (this.onTrue != null) {
			json.add("on_true", this.onTrue.build());
		}
		if (this.onFalse != null) {
			json.add("on_false", this.onFalse.build());
		}
	}
}
