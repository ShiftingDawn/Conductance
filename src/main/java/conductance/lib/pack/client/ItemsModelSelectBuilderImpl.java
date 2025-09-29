package conductance.lib.pack.client;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.resource.ItemsModelBuilder;
import conductance.api.resource.ItemsModelSelectBuilder;

@RequiredArgsConstructor
final class ItemsModelSelectBuilderImpl extends JsonResourceBuilderImpl<ItemsModelSelectBuilder> implements ItemsModelSelectBuilder {

	private final Map<JsonPrimitive, ItemsModelBuilderImpl> cases = new LinkedHashMap<>();
	private final ResourceLocation property;
	private @Nullable ItemsModelBuilderImpl fallback;

	@Override
	public ItemsModelSelectBuilder addCase(final String when, final Consumer<ItemsModelBuilder> builder) {
		final ItemsModelBuilderImpl modelBuilder = Util.make(new ItemsModelBuilderImpl(), builder);
		this.cases.put(new JsonPrimitive(when), modelBuilder);
		return this;
	}

	@Override
	public ItemsModelSelectBuilder addCase(final int when, final Consumer<ItemsModelBuilder> builder) {
		final ItemsModelBuilderImpl modelBuilder = Util.make(new ItemsModelBuilderImpl(), builder);
		this.cases.put(new JsonPrimitive(when), modelBuilder);
		return this;
	}

	@Override
	public ItemsModelSelectBuilder addCase(final boolean when, final Consumer<ItemsModelBuilder> builder) {
		final ItemsModelBuilderImpl modelBuilder = Util.make(new ItemsModelBuilderImpl(), builder);
		this.cases.put(new JsonPrimitive(when), modelBuilder);
		return this;
	}

	@Override
	public ItemsModelSelectBuilder fallback(final Consumer<ItemsModelBuilder> builder) {
		if (this.fallback == null) {
			this.fallback = new ItemsModelBuilderImpl();
		}
		builder.accept(this.fallback);
		return this;
	}

	@Override
	public void blockState(final String property) {
		this.addProperty("block_state_property", property);
	}

	@Override
	public void component(final ResourceLocation predicate) {
		this.addProperty("component", predicate.toString());
	}

	@Override
	public void localTime(final String pattern, @Nullable final String locale, @Nullable final String timezone) {
		this.addProperty("pattern", pattern);
		if (locale != null) {
			this.addProperty("locale", locale);
		}
		if (timezone != null) {
			this.addProperty("time_zone", timezone);
		}
	}

	@Override
	public void customModelData(final int index) {
		this.addProperty("index", index);
	}

	@Override
	protected void populateJson(final JsonObject json) {
		json.addProperty("type", ResourceLocation.withDefaultNamespace("select").toString());
		json.addProperty("property", this.property.toString());
		if (this.fallback != null) {
			json.add("fallback", this.fallback.build());
		}
		json.add("cases", Util.make(new JsonArray(), arr -> this.cases.forEach((when, model) -> {
			arr.add(Util.make(new JsonObject(), caseJson -> {
				caseJson.add("when", when);
				caseJson.add("model", model.build());
			}));
		})));
	}
}
