package conductance.core.machine;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.Property;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import conductance.api.resource.BlockStateModelDefiner;
import conductance.api.resource.BlockStateModelPropsBuilder;
import conductance.api.resource.BlockStateVariantBuilder;

final class BlockStateVariantBuilderImpl implements BlockStateVariantBuilder {

	private final IdentityHashMap<Property<?>, Comparable<?>> properties = new IdentityHashMap<>();
	private final List<BlockStateModelPropsBuilderImpl> models = new ArrayList<>();

	BlockStateVariantBuilderImpl(@Nullable final Property<?> initialProperty, @Nullable final Comparable<?> initialValue) {
		if (initialProperty != null && initialValue != null) {
			this.properties.put(initialProperty, initialValue);
		}
	}

	@Override
	public <T extends Comparable<T>> BlockStateVariantBuilder when(final Property<T> property, final T value) {
		this.properties.put(property, value);
		return this;
	}

	@Override
	public BlockStateModelPropsBuilder model(final ResourceLocation modelLocation) {
		return Util.make(new BlockStateModelPropsBuilderImpl(modelLocation), this.models::add);
	}

	@Override
	public void models(final Consumer<BlockStateModelDefiner> generatorConsumer) {
		generatorConsumer.accept(this);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private String getJsonKey() {
		if (this.properties.isEmpty()) {
			return "";
		}
		final StringJoiner joiner = new StringJoiner(",");
		this.properties.forEach((prop, value) -> joiner.add("%s=%s".formatted(prop.getName(), ((Property) prop).getName(value))));
		return joiner.toString();
	}

	private JsonElement getJsonValue() {
		if (this.models.size() == 1) {
			return this.models.getFirst().serialize();
		} else {
			return Util.make(new JsonArray(), arr -> this.models.forEach(model -> arr.add(model.serialize())));
		}
	}

	void populateJson(final JsonObject json) {
		json.add(this.getJsonKey(), this.getJsonValue());
	}
}
