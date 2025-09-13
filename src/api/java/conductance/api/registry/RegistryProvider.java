package conductance.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import conductance.api.cover.CoverType;
import conductance.api.machine.MachineType;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialOreType;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.periodicelement.PeriodicElement;
import conductance.api.recipe.IRecipeElementType;
import conductance.api.recipe.NCRecipeType;
import conductance.api.tier.Tier;

public interface RegistryProvider {

	//region resourcekeys
	ResourceKey<Registry<PeriodicElement>> periodicElementRegistry();

	ResourceKey<Registry<MaterialTextureType>> materialTextureTypeRegistry();

	ResourceKey<Registry<MaterialTraitKey<?>>> materialTraitRegistry();

	ResourceKey<Registry<MaterialFlag>> materialFlagRegistry();

	ResourceKey<Registry<MaterialOreType>> materialOreTypeRegistry();

	ResourceKey<Registry<TaggedMaterialSet>> materialTaggedSetRegistry();

	ResourceKey<Registry<Material>> materialRegistry();

	ResourceKey<Registry<IRecipeElementType<?>>> recipeElementTypeRegistry();

	ResourceKey<Registry<NCRecipeType>> recipeTypeRegistry();

	ResourceKey<Registry<Tier>> tierRegistry();

	ResourceKey<Registry<MachineType<?>>> machineRegistry();

	ResourceKey<Registry<CoverType<?>>> coverRegistry();
	//endregion

	//region registry
	Registry<PeriodicElement> periodicElements();

	Registry<MaterialTextureType> materialTextureTypes();

	Registry<MaterialTraitKey<?>> materialTraits();

	Registry<MaterialFlag> materialFlags();

	Registry<MaterialOreType> materialOreTypes();

	Registry<TaggedMaterialSet> materialTaggedSets();

	Registry<Material> materials();

	Registry<IRecipeElementType<?>> recipeElementTypes();

	Registry<NCRecipeType> recipeTypes();

	Registry<Tier> tiers();

	Registry<MachineType<?>> machines();

	Registry<CoverType<?>> covers();
	//endregion
}
