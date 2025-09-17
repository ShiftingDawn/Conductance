package conductance.core.material;

import net.minecraft.resources.ResourceLocation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.UnknownNullability;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialProp;
import conductance.api.material.MaterialTrait;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.event.ModifyMaterialEvent;

@RequiredArgsConstructor
final class ModifyMaterialEventImpl implements ModifyMaterialEvent {

	private final @Getter ResourceLocation materialId;
	private final MaterialBuilderImpl builder;

	@Override
	public boolean hasFlag(final MaterialFlag flag) {
		return this.builder.getFlags().contains(flag);
	}

	@Override
	public void addFlag(final MaterialFlag flag) {
		this.builder.flag(flag);
	}

	@Override
	public void removeFlag(final MaterialFlag flag) {
		this.builder.getFlags().remove(flag);
	}

	@Override
	public boolean hasTrait(final MaterialTraitKey<?> trait) {
		return this.builder.getTraits().containsKey(trait);
	}

	@Override
	public <T extends MaterialTrait<T>> void addTrait(final MaterialTraitKey<T> trait, final T instance) {
		this.builder.trait(trait, instance);
	}

	@SuppressWarnings("unchecked")
	@Override
	@UnknownNullability
	public <T extends MaterialTrait<T>> T getTrait(final MaterialTraitKey<T> trait) {
		return (T) this.builder.getTraits().get(trait);
	}

	@Override
	public void removeTrait(final MaterialTraitKey<?> trait) {
		this.builder.getTraits().remove(trait);
	}

	@Override
	public boolean hasProp(final MaterialProp<?> prop) {
		return this.builder.getProps().containsKey(prop);
	}

	@Override
	public <T> void addProp(final MaterialProp<T> prop, final T value) {
		this.builder.prop(prop, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	@UnknownNullability
	public <T> T getProp(final MaterialProp<T> prop) {
		return (T) this.builder.getProps().get(prop);
	}

	@Override
	public void removeProp(final MaterialProp<?> prop) {
		this.builder.getProps().remove(prop);
	}
}
