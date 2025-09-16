package conductance.api.material;

import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import conductance.api.CAPI;

public interface MaterialFlag {

	default ResourceLocation getId() {
		return Objects.requireNonNull(CAPI.regs().materialFlags().getKey(this), "unregistered material flag");
	}
}
