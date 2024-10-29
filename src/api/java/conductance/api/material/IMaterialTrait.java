package conductance.api.material;

public interface IMaterialTrait<T extends IMaterialTrait<T>> {

	void verify(Material material, MaterialTraitMap traitMap);
}
