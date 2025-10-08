package conductance.api.machine.multi;

import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.Nullable;

public record MultiBlockStructure(
	StructurePredicate[][][] expectedStates,
	int xOffset, int yOffset, int zOffset,
	@Nullable StructureCheckCallback[][][] callbacks,
	List<StructureCheckCallback> globalCallbacks,
	Set<StructurePredicate> predicates
) {
}
