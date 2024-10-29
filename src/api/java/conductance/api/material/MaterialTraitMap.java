package conductance.api.material;

import javax.annotation.Nullable;

public interface MaterialTraitMap {

	boolean isEmpty();

	boolean has(MaterialTraitKey<?> key);

	@Nullable
	<T extends IMaterialTrait<T>> T get(MaterialTraitKey<T> key);

	<T extends IMaterialTrait<T>> void set(MaterialTraitKey<T> key, T value, boolean verify);

	default <T extends IMaterialTrait<T>> void set(MaterialTraitKey<T> key, T value) {
		set(key, value, true);
	}
}
