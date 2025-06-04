package conductance.api.resource.event;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import conductance.api.plugin.IConductancePluginEvent;
import conductance.api.resource.BlockStateBuilder;

public interface AddBlockStateEvent extends IConductancePluginEvent {

	void add(ResourceLocation location, Consumer<BlockStateBuilder> builder);
}
