package conductance.core.apiimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCMaterialTraits;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.Material;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.MaterialTraitMap;
import conductance.api.util.Marker;

public class MaterialTraitMapImpl implements MaterialTraitMap {

	// TODO add base materials : fluid
	private static final Set<MaterialTraitKey<?>> BASE_TRAITS = new HashSet<>(Arrays.asList(NCMaterialTraits.DUST, NCMaterialTraits.INGOT, NCMaterialTraits.GEM));

	private final IdentityHashMap<MaterialTraitKey<?>, IMaterialTrait<?>> traits = new IdentityHashMap<>();
	Material material;

	@Override
	public boolean isEmpty() {
		return this.traits.isEmpty();
	}

	@Override
	public boolean has(MaterialTraitKey<?> trait) {
		return this.traits.get(trait) != null;
	}

	@Nullable
	@Override
	public <T extends IMaterialTrait<T>> T get(MaterialTraitKey<T> key) {
		IMaterialTrait<?> trait = this.traits.get(key);
		return trait != null ? key.getTypeClass().cast(trait) : null;
	}

	@Override
	public <T extends IMaterialTrait<T>> void set(MaterialTraitKey<T> key, T value) {
		Objects.requireNonNull(value);
		if (!this.has(key)) {
			this.traits.put(key, value);
			if (this.material != null) { // Don't verify during the building phase
				this.verify();
			}
		}
	}

	public void verify() {
		if (this.material instanceof Marker) {
			return;
		}
		List<IMaterialTrait<?>> oldList;
		do {
			oldList = new ArrayList<>(this.traits.values());
			oldList.forEach(p -> p.verify(this.material, this));
		} while (oldList.size() != this.traits.size());

		if (this.traits.keySet().stream().noneMatch(BASE_TRAITS::contains)) {
			throw new IllegalArgumentException("Material must have at least one of: " + BASE_TRAITS + " specified!");
		}
	}
}
