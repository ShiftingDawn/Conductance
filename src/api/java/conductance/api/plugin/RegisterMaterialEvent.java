package conductance.api.plugin;

import java.util.function.Consumer;
import conductance.api.material.Material;

public interface RegisterMaterialEvent extends IConductancePluginEvent {

	Material register(String name, Consumer<MaterialBuilder> builder);
}
