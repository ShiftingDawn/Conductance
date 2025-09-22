package conductance.api.resource;

import java.util.function.Consumer;

public interface CompositeModelBuilder {

	CompositeModelBuilder child(String name, Consumer<ModelBuilder> childBuilder, boolean ignoreWhenEmpty);

	default CompositeModelBuilder child(final String name, final Consumer<ModelBuilder> childBuilder) {
		return this.child(name, childBuilder, false);
	}

	CompositeModelBuilder itemRenderOrder(String... renderOrder);
}
