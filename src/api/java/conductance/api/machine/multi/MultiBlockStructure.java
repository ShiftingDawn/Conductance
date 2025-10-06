package conductance.api.machine.multi;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public record MultiBlockStructure(
	StructurePredicate[][][] expectedStates,
	int xOffset, int yOffset, int zOffset,
	@Nullable StructureCheckCallback[][][] callbacks,
	List<StructureCheckCallback> globalCallbacks
) {
}
