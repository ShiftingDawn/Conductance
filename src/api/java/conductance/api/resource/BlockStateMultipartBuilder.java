package conductance.api.resource;

import java.util.function.Consumer;

public interface BlockStateMultipartBuilder {

	BlockStateMultipartBuilder part(Consumer<BlockStateMultipartWhenBuilder> whenBuilder, Consumer<BlockStateModelDefiner> model);
}
