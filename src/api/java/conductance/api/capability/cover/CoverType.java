package conductance.api.capability.cover;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import conductance.api.registry.IRegistryObject;

public interface CoverType<COVER extends CoverEntity<COVER>> extends IRegistryObject<ResourceLocation> {

	CoverRenderer getRenderer();

	COVER instantiate(CoverManager manager, Direction side);
}
