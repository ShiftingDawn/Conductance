package conductance.core.runtimepack.client;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.world.level.block.state.properties.Property;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import conductance.api.resource.BlockStateModelDefiner;
import conductance.api.resource.BlockStateMultipartBuilder;
import conductance.api.resource.BlockStateMultipartWhenBuilder;

final class BlockStateMultipartBuilderImpl implements BlockStateMultipartBuilder {

	private record Multipart(IdentityHashMap<Property<?>, Comparable<?>> properties, BlockStateModelPropsBuilderImpl model) {
	}

	private final List<Multipart> parts = new ArrayList<>();

	@Override
	public BlockStateMultipartBuilder part(final Consumer<BlockStateMultipartWhenBuilder> whenBuilder, final Consumer<BlockStateModelDefiner> model) {
		final IdentityHashMap<Property<?>, Comparable<?>> when = new IdentityHashMap<>();
		whenBuilder.accept(new BlockStateMultipartWhenBuilder() {
			@Override
			public <T extends Comparable<T>> BlockStateMultipartWhenBuilder when(final Property<T> property, final T value) {
				when.put(property, value);
				return this;
			}
		});
		final BlockStateModelPropsBuilderImpl[] modelHolder = new BlockStateModelPropsBuilderImpl[1];
		model.accept(modelLocation -> Util.make(new BlockStateModelPropsBuilderImpl(modelLocation), m -> modelHolder[0] = m));
		this.parts.add(new Multipart(when, modelHolder[0]));
		return this;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public JsonElement serialize() {
		return Util.make(new JsonArray(), array -> this.parts.forEach(part -> array.add(Util.make(new JsonObject(), json -> {
			if (!part.properties.isEmpty()) {
				json.add("when", Util.make(new JsonObject(), whenJson -> part.properties.forEach((prop, value) -> {
					whenJson.addProperty(prop.getName(), ((Property) prop).getName(value));
				})));
			}
			json.add("apply", part.model.build());
		}))));
	}
}
