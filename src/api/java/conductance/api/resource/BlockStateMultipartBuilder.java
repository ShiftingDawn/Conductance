package conductance.api.resource;

import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;

public interface BlockStateMultipartBuilder {

	BlockStateMultipartBuilder part(@Nullable Consumer<BlockStateMultipartWhenBuilder> whenBuilder, Consumer<BlockStateModelDefiner> model);
}
