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

	// TODO add base materials : dust, ingot, fluid, gem
	private static final Set<MaterialTraitKey<?>> BASE_TYPES = new HashSet<>(Arrays.asList(NCMaterialTraits.DUST));

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
	public <T extends IMaterialTrait<T>> void set(MaterialTraitKey<T> key, T value, boolean verify) {
		Objects.requireNonNull(value);
		if (this.has(key)) {
			throw new IllegalStateException("Material type %s is already added to this material!".formatted(key));
		}
		this.traits.put(key, value);
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

		if (this.traits.keySet().stream().noneMatch(BASE_TYPES::contains)) {
			throw new IllegalArgumentException("Material must have at least one of: " + BASE_TYPES + " specified!");
		}
	}
}
