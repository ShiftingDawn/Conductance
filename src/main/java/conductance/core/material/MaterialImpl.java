package conductance.core.material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.function.ToLongFunction;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.MaterialColor;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialProp;
import conductance.api.material.MaterialStack;
import conductance.api.material.MaterialTrait;
import conductance.api.material.MaterialTraitKey;
import conductance.api.periodicelement.PeriodicElement;
import conductance.api.util.Lazy;
import conductance.api.util.LazyLong;
import conductance.api.util.TextHelper;

final class MaterialImpl implements Material {

	private final @Nullable PeriodicElement periodicElement;
	private final Set<MaterialFlag> flags;
	private final Map<MaterialTraitKey<?>, MaterialTrait<?>> traits;
	private final Map<MaterialProp<?>, Object> props;
	private final @Getter ResourceLocation textureSet;
	private final Lazy<MaterialColor> color;
	private final List<MaterialStack> components;
	private final Lazy<String> chemicalFormula;
	private final LazyLong protons;
	private final LazyLong neutrons;
	private final LazyLong mass;
	private final Lazy<String> descriptionId = Lazy.of(() -> Util.makeDescriptionId("material", this.getId()));

	MaterialImpl(
		@Nullable final PeriodicElement periodicElement,
		final Set<MaterialFlag> flags, final Map<MaterialTraitKey<?>, MaterialTrait<?>> traits, final Map<MaterialProp<?>, Object> props,
		@Nullable final MaterialColor color, final ResourceLocation textureSet,
		final List<MaterialStack> components, @Nullable final String chemicalFormula, @Nullable final Long protons, @Nullable final Long neutrons, @Nullable final Long mass
	) {
		this.periodicElement = periodicElement;
		this.flags = Collections.unmodifiableSet(flags);
		this.traits = Collections.unmodifiableMap(traits);
		this.props = Collections.unmodifiableMap(props);
		this.textureSet = textureSet;
		this.color = color != null ? Lazy.of(color) : Lazy.of(this::calcColor);
		this.components = Collections.unmodifiableList(components);
		this.chemicalFormula = chemicalFormula != null ? Lazy.of(chemicalFormula) : Lazy.of(this::calcChemicalFormula);
		this.protons = protons != null ? LazyLong.of(protons) : LazyLong.of(() -> this.calc(PeriodicElement::protons, Material::getProtons, 43));
		this.neutrons = neutrons != null ? LazyLong.of(neutrons) : LazyLong.of(() -> this.calc(PeriodicElement::neutrons, Material::getNeutrons, 55));
		this.mass = mass != null ? LazyLong.of(mass) : LazyLong.of(() -> this.calc(PeriodicElement::mass, Material::getMass, 43));
	}

	@Override
	public @Nullable PeriodicElement getPeriodicElement() {
		return this.periodicElement;
	}

	@Override
	public long getProtons() {
		return this.protons.getAsLong();
	}

	@Override
	public long getNeutrons() {
		return this.neutrons.getAsLong();
	}

	@Override
	public long getMass() {
		return this.mass.getAsLong();
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
	public MaterialColor getColor() {
		return this.color.get();
	}

	@Override
	public List<MaterialStack> getComponents() {
		return this.components;
	}

	@Override
	public String getChemicalFormula() {
		return this.chemicalFormula.get();
	}

	private MaterialColor calcColor() {
		return new MaterialColor(new int[] {-1}, 0);
	}

	private long calc(final ToLongFunction<PeriodicElement> rootProvider, final ToLongFunction<Material> provider, final long fallback) {
		if (this.periodicElement != null) {
			return rootProvider.applyAsLong(this.periodicElement);
		} else if (this.components.isEmpty()) {
			return fallback; // Technetium
		} else {
			long total = 0;
			long amount = 0;
			for (final MaterialStack entry : this.components) {
				total += entry.count() * provider.applyAsLong(entry.material());
				amount += entry.count();
			}
			return total / amount;
		}
	}

	private String calcChemicalFormula() {
		if (this.periodicElement != null) {
			return this.periodicElement.symbol();
		}
		if (this.getComponents().isEmpty()) {
			return "?";
		}
		final StringBuilder builder = new StringBuilder();
		for (final MaterialStack stack : this.components) {
			if (stack.material().getComponents().isEmpty()) {
				builder.append(stack.material().getChemicalFormula());
				if (stack.count() > 1) {
					builder.append(TextHelper.getNumberAsSubscript(stack.count()));
				}
			} else {
				if (this.getComponents().size() > 1) {
					builder.append('(');
				}
				builder.append(stack.material().getChemicalFormula());
				if (this.getComponents().size() > 1) {
					builder.append(')');
				}
				if (stack.count() > 1) {
					builder.append(TextHelper.getNumberAsSubscript(stack.count()));
				}
			}
		}
		return builder.toString();
	}

	List<String> validate() {
		final List<String> errors = new ArrayList<>();
		for (final MaterialFlag flag : this.flags) {
			if (flag instanceof final MaterialFlagImpl f) {
				errors.addAll(f.validate(this));
			}
		}
		for (final Map.Entry<MaterialTraitKey<?>, MaterialTrait<?>> traitEntry : this.traits.entrySet()) {
			for (final String error : traitEntry.getValue().validate(this)) {
				errors.add("Trait %s: %s".formatted(traitEntry.getKey().getId(), error));
			}
		}
		return errors;
	}
}
