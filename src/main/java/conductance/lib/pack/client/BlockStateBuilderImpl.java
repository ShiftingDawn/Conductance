package conductance.lib.pack.client;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import conductance.api.resource.BlockStateBuilder;
import conductance.api.resource.BlockStateMultipartBuilder;
import conductance.api.resource.BlockStateVariantBuilder;
import conductance.api.resource.BlockStateVariantsBuilder;

final class BlockStateBuilderImpl implements BlockStateBuilder {

	private @Nullable BlockStateVariantBuilderImpl variant;
	private @Nullable BlockStateVariantsBuilderImpl variants;
	private @Nullable BlockStateMultipartBuilderImpl multipart;

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

	@Override
	public void multipart(final Consumer<BlockStateMultipartBuilder> callback) {
		this.multipart = new BlockStateMultipartBuilderImpl();
		callback.accept(this.multipart);
	}

	public JsonObject build() {
		if (this.variant != null) {
			return Util.make(new JsonObject(), root -> root.add("variants", Util.make(new JsonObject(), this.variant::populateJson)));
		}
		if (this.variants != null) {
			return Util.make(new JsonObject(), root -> root.add("variants", this.variants.serialize()));
		}
		if (this.multipart != null) {
			return Util.make(new JsonObject(), root -> root.add("multipart", this.multipart.serialize()));
		}
		return new JsonObject();
	}
}
