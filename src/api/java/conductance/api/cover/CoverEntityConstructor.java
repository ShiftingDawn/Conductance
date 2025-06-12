package conductance.api.cover;

import net.minecraft.core.Direction;

@FunctionalInterface
public interface CoverEntityConstructor<COVER extends CoverEntity<COVER>> {

	COVER instantiate(CoverManager manager, CoverType<COVER> coverType, Direction side);
}
