package conductance.lib.pack.client;

import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.floats.Float2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.floats.Float2ObjectMap;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.resource.ItemsModelBuilder;
import conductance.api.resource.ItemsModelRangeDispatchBuilder;

@RequiredArgsConstructor
final class ItemsModelRangeDispatchBuilderImpl extends JsonResourceBuilderImpl<ItemsModelRangeDispatchBuilder> implements ItemsModelRangeDispatchBuilder {

	private final Float2ObjectMap<ItemsModelBuilderImpl> entries = new Float2ObjectLinkedOpenHashMap<>();
	private final ResourceLocation property;
	private @Nullable Float scale;
	private @Nullable ItemsModelBuilderImpl fallback;

	@Override
	public ItemsModelRangeDispatchBuilder scale(final float scale) {
		this.scale = scale;
		return this;
	}

	@Override
	public ItemsModelRangeDispatchBuilder entry(final float threshold, final Consumer<ItemsModelBuilder> builder) {
		final ItemsModelBuilderImpl modelBuilder = Util.make(new ItemsModelBuilderImpl(), builder);
		this.entries.put(threshold, modelBuilder);
		return this;
	}

	@Override
	public ItemsModelRangeDispatchBuilder fallback(final Consumer<ItemsModelBuilder> builder) {
		if (this.fallback == null) {
			this.fallback = new ItemsModelBuilderImpl();
		}
		builder.accept(this.fallback);
		return this;
	}

	@Override
	public void compass(final CompassTarget target, final boolean wobble) {
		this.addProperty("target", switch (target) {
			case SPAWN -> "spawn";
			case LODESTONE -> "lodestone";
			case RECOVERY -> "recovery";
			default -> "none";
		});
		this.addProperty("wobble", wobble);
	}

	@Override
	public void stackSize(final boolean normalize) {
		this.addProperty("normalize", normalize);
	}

	@Override
	public void damage(final boolean normalize) {
		this.addProperty("normalize", normalize);
	}

	@Override
	public void time(final TimeSource source, final boolean wobble) {
		this.addProperty("source", switch (source) {
			case DAYTIME -> "daytime";
			case MOON_PHASE -> "moon_phase";
			case RANDOM -> "random";
		});
		this.addProperty("wobble", wobble);
	}

	@Override
	public void useCycle(final float period) {
		this.addProperty("period", Mth.abs(period));
	}

	@Override
	public void useDuration(final boolean remaining) {
		this.addProperty("remaining", remaining);
	}

	@Override
	public void customModelData(final int index) {
		this.addProperty("index", index);
	}

	@Override
	protected void populateJson(final JsonObject json) {
		json.addProperty("type", ResourceLocation.withDefaultNamespace("range_dispatch").toString());
		json.addProperty("property", this.property.toString());
		if (this.fallback != null) {
			json.add("fallback", this.fallback.build());
		}
		if (this.scale != null) {
			json.addProperty("scale", this.scale);
		}
		json.add("entries", Util.make(new JsonArray(), arr -> this.entries.forEach((threshold, model) -> {
			arr.add(Util.make(new JsonObject(), entryJson -> {
				entryJson.addProperty("threshold", threshold);
				entryJson.add("model", model.build());
			}));
		})));
	}
}
