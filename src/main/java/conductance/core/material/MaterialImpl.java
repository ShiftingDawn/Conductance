package conductance.core.material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialProp;
import conductance.api.material.MaterialTrait;
import conductance.api.material.MaterialTraitKey;
import conductance.api.periodicelement.PeriodicElement;
import conductance.api.util.Lazy;
import conductance.api.util.LazyInt;

final class MaterialImpl implements Material {

	private final @Nullable PeriodicElement periodicElement;
	private final Set<MaterialFlag> flags;
	private final Map<MaterialTraitKey<?>, MaterialTrait<?>> traits;
	private final Map<MaterialProp<?>, Object> props;
	private final @Getter ResourceLocation textureSet;
	private final LazyInt color;
	private final Lazy<String> chemicalFormula;
	private final Lazy<String> descriptionId = Lazy.of(() -> Util.makeDescriptionId("material", this.getId()));

	MaterialImpl(
			@Nullable final PeriodicElement periodicElement, final Set<MaterialFlag> flags, final Map<MaterialTraitKey<?>, MaterialTrait<?>> traits, final Map<MaterialProp<?>, Object> props,
			@Nullable final Integer color,
			final ResourceLocation textureSet, @Nullable final String chemicalFormula
	) {
		this.periodicElement = periodicElement;
		this.flags = Collections.unmodifiableSet(flags);
		this.traits = Collections.unmodifiableMap(traits);
		this.props = Collections.unmodifiableMap(props);
		this.textureSet = textureSet;
		this.color = color != null ? LazyInt.of(color) : LazyInt.of(this::calcColor);
		this.chemicalFormula = chemicalFormula != null ? Lazy.of(chemicalFormula) : Lazy.of(this::calcChemicalFormula);
	}

	@Override
	public @Nullable PeriodicElement getPeriodicElement() {
		return this.periodicElement;
	}

	@Override
	public boolean hasFlag(final MaterialFlag flag) {
		return this.flags.contains(flag);
	}

	@Override
	public boolean hasTrait(final MaterialTraitKey<?> trait) {
		return this.traits.containsKey(trait);
	}

	@Override
	public boolean hasProp(final MaterialProp<?> prop) {
		return this.props.containsKey(prop);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends MaterialTrait<T>> @Nullable T getTrait(final MaterialTraitKey<T> traitKey) {
		return (T) this.traits.get(traitKey);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> @Nullable T getProp(final MaterialProp<T> prop) {
		return (T) this.props.get(prop);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getProp(final MaterialProp<T> prop, final T fallback) {
		if (!this.hasProp(prop)) {
			return fallback;
		}
		return (T) this.props.get(prop);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getProp(final MaterialProp<T> prop, final Supplier<T> fallback) {
		if (!this.hasProp(prop)) {
			return fallback.get();
		}
		return (T) this.props.get(prop);
	}

	@Override
	public String getDescriptionId() {
		return this.descriptionId.get();
	}

	@Override
	public int getColor() {
		return this.color.getAsInt();
	}

	@Override
	public String getChemicalFormula() {
		return this.chemicalFormula.get();
	}

	private int calcColor() {
		return -1;
	}

	private String calcChemicalFormula() {
		if (this.periodicElement != null) {
			return this.periodicElement.symbol();
		}
		return "?";
	}

	List<String> validate() {
		final List<String> errors = new ArrayList<>();
		for (final MaterialFlag flag : this.flags) {
			if (flag instanceof final MaterialFlagImpl f) {
				errors.addAll(f.validate(this));
			}
		}
		return errors;
	}
}
