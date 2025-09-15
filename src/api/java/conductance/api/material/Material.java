package conductance.api.material;

import java.util.Objects;
import conductance.api.CAPI;

public interface Material {

	boolean hasFlag(MaterialFlag flag);

	default String getName() {
		return Objects.requireNonNull(CAPI.regs().materials().getKey(this), "Unregistered material").getPath();
	}
}
