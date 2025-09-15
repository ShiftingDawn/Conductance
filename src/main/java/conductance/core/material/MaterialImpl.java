package conductance.core.material;

import java.util.Collections;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import lombok.Getter;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;

final class MaterialImpl implements Material {

	private final Set<MaterialFlag> flags;
	private final @Getter ResourceLocation textureSet;

	MaterialImpl(final Set<MaterialFlag> flags, final ResourceLocation textureSet) {
		this.flags = Collections.unmodifiableSet(flags);
		this.textureSet = textureSet;
	}

	@Override
	public boolean hasFlag(final MaterialFlag flag) {
		return this.flags.contains(flag);
	}
}
