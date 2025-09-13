package conductance.api.material.event;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import conductance.api.material.Material;
import conductance.api.periodicelement.PeriodicElement;
import conductance.api.plugin.IConductancePluginEvent;

public interface RegisterMaterialEvent extends IConductancePluginEvent {

	Material register(String registryName, @Nullable PeriodicElement element, Consumer<MaterialBuilder> builder);

	default Material register(final String registryName, final Consumer<MaterialBuilder> builder) {
		return this.register(registryName, null, builder);
	}
}
