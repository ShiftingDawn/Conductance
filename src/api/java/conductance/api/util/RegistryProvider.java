package conductance.api.util;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import conductance.api.coil.CoilBlockType;
import conductance.api.machine.MachineType;
import conductance.api.machine.multi.MultiBlockPartCapability;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.MaterialOreBearer;
import conductance.api.material.MaterialTraitKey;
import conductance.api.periodicelement.PeriodicElement;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.recipe.RecipeElementType;
import conductance.api.tier.Tier;

public interface RegistryProvider {

	ResourceKey<Registry<PeriodicElement>> periodicElementRegistry();

	ResourceKey<Registry<MaterialFlag>> materialFlagRegistry();

	ResourceKey<Registry<MaterialTraitKey<?>>> materialTraitRegistry();

	ResourceKey<Registry<MaterialOreBearer>> materialOreBearerRegistry();

	ResourceKey<Registry<Material>> materialRegistry();

	ResourceKey<Registry<MaterialGenerationHandler>> materialGenerationHandlerRegistry();

	ResourceKey<Registry<Tier>> tierRegistry();

	ResourceKey<Registry<RecipeElementType<?>>> recipeElementTypeRegistry();

	ResourceKey<Registry<MachineRecipeType>> recipeTypeRegistry();

	ResourceKey<Registry<MultiBlockPartCapability>> multiBlockPartCapabilityRegistry();

	ResourceKey<Registry<CoilBlockType>> coilBlockTypeRegistry();

	ResourceKey<Registry<MachineType<?>>> machineRegistry();

	Registry<PeriodicElement> periodicElements();

	Registry<MaterialFlag> materialFlags();

	Registry<MaterialTraitKey<?>> materialTraits();

	Registry<MaterialOreBearer> materialOreBearers();

	Registry<Material> materials();

	Registry<MaterialGenerationHandler> materialGenerationHandlers();

	Registry<Tier> tiers();

	Registry<RecipeElementType<?>> recipeElementTypes();

	Registry<MachineRecipeType> recipeTypes();

	Registry<MultiBlockPartCapability> multiBlockPartCapabilities();

	Registry<CoilBlockType> coilBlockTypes();

	Registry<MachineType<?>> machines();
}
