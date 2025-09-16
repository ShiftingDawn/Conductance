package conductance.core.material;

import java.util.Collections;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.util.LazyInt;

final class MaterialImpl implements Material {

	private final Set<MaterialFlag> flags;
	private final @Getter ResourceLocation textureSet;
	private final LazyInt color;

	MaterialImpl(final Set<MaterialFlag> flags, @Nullable final Integer color, final ResourceLocation textureSet) {
		this.flags = Collections.unmodifiableSet(flags);
		this.textureSet = textureSet;
		this.color = color != null ? LazyInt.of(color) : LazyInt.of(this::calcColor);
	}

	@Override
	public boolean hasFlag(final MaterialFlag flag) {
		return this.flags.contains(flag);
	}

	@Override
	public int getColor() {
		return this.color.getAsInt();
	}

	private int calcColor() {
		return -1;
	}
}
