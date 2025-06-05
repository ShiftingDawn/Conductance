package conductance.api.resource;

import java.util.function.Consumer;

public interface CompositeModelBuilder {

	CompositeModelBuilder child(String name, Consumer<ModelBuilder> childBuilder);

	CompositeModelBuilder itemRenderOrder(String... renderOrder);
}
