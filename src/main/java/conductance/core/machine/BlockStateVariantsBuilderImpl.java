package conductance.core.machine;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.world.level.block.state.properties.Property;
import com.google.gson.JsonObject;
import conductance.api.resource.BlockStateVariantBuilder;
import conductance.api.resource.BlockStateVariantsBuilder;

final class BlockStateVariantsBuilderImpl implements BlockStateVariantsBuilder {

	private final List<BlockStateVariantBuilderImpl> variants = new ArrayList<>();

	@Override
	public <T extends Comparable<T>> BlockStateVariantBuilder variant(final Property<T> property, final T value) {
		return Util.make(new BlockStateVariantBuilderImpl(property, value), this.variants::add);
	}

	JsonObject serialize() {
		JsonObject result = new JsonObject();
		this.variants.forEach(variant -> variant.populateJson(result));
		return result;
	}
}
