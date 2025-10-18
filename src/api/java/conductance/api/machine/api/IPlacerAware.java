package conductance.api.machine.api;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public interface IPlacerAware {

	void setPlacer(@Nullable UUID placerUuid);

	@Nullable UUID getPlacer();
}
