package conductance.api.resource;

import java.util.function.Consumer;

public interface ItemsModelCompositeBuilder {

	ItemsModelCompositeBuilder model(Consumer<ItemsModelBuilder> builder);
}
