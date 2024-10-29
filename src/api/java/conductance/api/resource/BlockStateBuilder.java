package conductance.api.resource;

import java.util.function.Consumer;

public interface BlockStateBuilder {

	void simple(Consumer<BlockStateVariantBuilder> callback);

	void variants(Consumer<BlockStateVariantsBuilder> callback);
}
