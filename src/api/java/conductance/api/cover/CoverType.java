package conductance.api.cover;

import java.util.function.Supplier;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import conductance.api.registry.IRegistryObject;

public interface CoverType<COVER extends CoverEntity<COVER>> extends IRegistryObject<ResourceLocation> {

	Supplier<CoverQuadProvider> getRenderer();

	COVER instantiate(CoverManager manager, Direction side);
}
