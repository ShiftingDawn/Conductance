package conductance.api.material;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import conductance.api.CAPI;
import conductance.api.periodicelement.PeriodicElement;

public interface Material {

	@Nullable
	PeriodicElement getPeriodicElement();

	long getProtons();

	long getNeutrons();

	long getMass();

	boolean hasFlag(MaterialFlag flag);

	boolean hasTrait(MaterialTraitKey<?> trait);

	boolean hasProp(MaterialProp<?> prop);

	<T extends MaterialTrait<T>> @UnknownNullability T getTrait(MaterialTraitKey<T> traitKey);

	<T> @UnknownNullability T getProp(MaterialProp<T> prop);

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

	List<MaterialStack> getComponents();

	String getChemicalFormula();
}
