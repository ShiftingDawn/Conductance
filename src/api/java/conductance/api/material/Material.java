package conductance.api.material;

import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;

public interface Material {

	boolean hasFlag(MaterialFlag flag);

	boolean hasTrait(MaterialTraitKey<?> trait);

	boolean hasProp(MaterialProp<?> prop);

	<T extends MaterialTrait<T>> @Nullable T getTrait(MaterialTraitKey<T> traitKey);

	<T> @Nullable T getProp(MaterialProp<T> prop);

	<T> T getProp(MaterialProp<T> prop, T fallback);

	<T> T getProp(MaterialProp<T> prop, Supplier<T> fallback);

	default ResourceLocation getId() {
		return Objects.requireNonNull(CAPI.regs().materials().getKey(this), "Unregistered material");
	}

	default String getName() {
		return this.getId().getPath();
	}

	String getDescriptionId();

	int getColor();

	ResourceLocation getTextureSet();
}
