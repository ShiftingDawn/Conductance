package conductance.api.material;

import java.util.function.Consumer;

public interface IMaterialTrait<T extends IMaterialTrait<T>> {

	void validate(Material material, Consumer<MaterialTraitKey<?>> assertTrait);
}
