package conductance.core.material;

import java.util.Optional;
import net.minecraft.resources.ResourceLocation;

public record MaterialTextureSet(ResourceLocation parent, Optional<ResourceLocation> overlay) {

}
