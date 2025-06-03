package conductance.core.material;

import lombok.AllArgsConstructor;
import lombok.Getter;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.event.ModifyMaterialEvent;

@AllArgsConstructor
final class ModifyMaterialEventImpl implements ModifyMaterialEvent {

	@Getter
	private final MaterialImpl material;

	@Override
	public void addFlag(final MaterialFlag flag) {
		this.material.getFlags().add(flag);
	}

	@Override
	public <T extends IMaterialTrait<T>> void addTrait(final MaterialTraitKey<T> trait, final T value) {
		this.material.getTraits().put(trait, value);
	}

	@Override
	public void removeFlag(final MaterialFlag flag) {
		this.material.getFlags().remove(flag);
	}

	@Override
	public void removeTrait(final MaterialTraitKey<?> trait) {
		this.material.getTraits().remove(trait);
	}
}
