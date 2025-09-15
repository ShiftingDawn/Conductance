package conductance.api.resource;

import java.util.function.Consumer;

public interface ItemsModelModelBuilder {

	ItemsModelModelBuilder tints(Consumer<ItemsModelTintsBuilder> builder);
}
