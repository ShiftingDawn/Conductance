package conductance.api.material.event;

import java.util.function.Consumer;
import conductance.api.material.Material;
import conductance.api.plugin.IConductancePluginEvent;
import conductance.api.material.MaterialBuilder;

public interface RegisterMaterialEvent extends IConductancePluginEvent {

	Material register(String name, Consumer<MaterialBuilder> builder);
}
