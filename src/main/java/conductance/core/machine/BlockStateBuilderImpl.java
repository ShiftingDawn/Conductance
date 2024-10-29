package conductance.core.machine;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import conductance.api.resource.BlockStateBuilder;
import conductance.api.resource.BlockStateVariantBuilder;
import conductance.api.resource.BlockStateVariantsBuilder;

public final class BlockStateBuilderImpl implements BlockStateBuilder {

	@Nullable
	private BlockStateVariantBuilderImpl variant;
	@Nullable
	private BlockStateVariantsBuilderImpl variants;

	@Override
	public void simple(final Consumer<BlockStateVariantBuilder> callback) {
		this.variant = new BlockStateVariantBuilderImpl(null, null);
		callback.accept(this.variant);
	}

	@Override
	public void variants(final Consumer<BlockStateVariantsBuilder> callback) {
		this.variants = new BlockStateVariantsBuilderImpl();
		callback.accept(this.variants);
	}

	public JsonObject build() {
		if (this.variant != null) {
			return Util.make(new JsonObject(), root -> root.add("variants", Util.make(new JsonObject(), this.variant::populateJson)));
		}
		if (this.variants != null) {
			return Util.make(new JsonObject(), root -> root.add("variants", this.variants.serialize()));
		}
		return new JsonObject();
	}
}
