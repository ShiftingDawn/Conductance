package conductance.api.resource.event;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import conductance.api.plugin.IConductancePluginEvent;
import conductance.api.resource.ItemModelBuilder;

public interface AddItemModelEvent extends IConductancePluginEvent {

	void add(ResourceLocation location, Consumer<ItemModelBuilder<?>> builder);
}
