package conductance.core.apiimpl;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;
import conductance.api.capability.cover.CoverType;
import conductance.api.machine.MachineType;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialOreType;
import conductance.api.material.MaterialTextureSet;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.PeriodicElement;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.registry.ConductanceRegistry;
import conductance.api.registry.IRegistryObject;
import conductance.api.registry.RegistryProvider;
import conductance.api.util.tier.Tier;
import conductance.Conductance;
import conductance.core.recipe.RecipeSerializerImpl;

public final class RegistryProviderImpl implements RegistryProvider {

	private final ConductanceRegistryImpl<ResourceLocation, PeriodicElement> periodicElements = RegistryProviderImpl.makeResourceKeyed("periodic_element");

	private final ConductanceRegistryImpl<ResourceLocation, MaterialTextureType> materialTextureTypes = RegistryProviderImpl.makeResourceKeyed("material_texture_type");
	private final ConductanceRegistryImpl<String, MaterialTextureSet> materialTextureSets = RegistryProviderImpl.makeStringKeyed("material_texture_set");
	private final ConductanceRegistryImpl<ResourceLocation, MaterialTraitKey<?>> materialTraits = RegistryProviderImpl.makeResourceKeyed("material_trait");
	private final ConductanceRegistryImpl<ResourceLocation, MaterialFlag> materialFlags = RegistryProviderImpl.makeResourceKeyed("material_flag");
	private final ConductanceRegistryImpl<ResourceLocation, MaterialOreType> materialOreTypes = RegistryProviderImpl.makeResourceKeyed("material_ore_type");
	private final ConductanceRegistryImpl<String, TaggedMaterialSet> materialTaggedSets = RegistryProviderImpl.makeStringKeyed("material_tagged_set");
	private final ConductanceRegistryImpl<ResourceLocation, Material> materials = RegistryProviderImpl.makeResourceKeyed("material");

	private final ConductanceRegistryImpl<ResourceLocation, IRecipeElementType<?>> recipeElementTypes = RegistryProviderImpl.makeResourceKeyed("recipe_element_type");
	private final ConductanceRegistryImpl<ResourceLocation, NCRecipeType> recipeTypes = RegistryProviderImpl.makeResourceKeyed("recipe_type");

	private final ConductanceRegistryImpl<String, Tier> tiers = RegistryProviderImpl.makeStringKeyed("tier");
	private final ConductanceRegistryImpl<String, MachineType<?>> machines = RegistryProviderImpl.makeStringKeyed("machine_type");
	private final ConductanceRegistryImpl<ResourceLocation, CoverType<?>> covers = RegistryProviderImpl.makeResourceKeyed("cover");

	RegistryProviderImpl(final IEventBus modEventBus) {
		modEventBus.addListener(this::onRegisterEvent);
		this.tiers.setRegisterCallback((id, tier) -> TierRegistryImpl.insertTier((TierImpl) tier));
		this.tiers.setUnregisterCallback((id, tier) -> TierRegistryImpl.removeTier((TierImpl) tier));
	}

	private void onRegisterEvent(final RegisterEvent event) {
		if (event.getRegistryKey() == Registries.RECIPE_TYPE) {
			this.recipeTypes.values().forEach(recipeType -> {
				event.register(Registries.RECIPE_TYPE, recipeType.getRegistryKey(), () -> recipeType);
			});
		}
		if (event.getRegistryKey() == Registries.RECIPE_SERIALIZER) {
			this.recipeTypes.values().forEach(recipeType -> {
				event.register(Registries.RECIPE_SERIALIZER, recipeType.getRegistryKey(), RecipeSerializerImpl::new);
			});
		}
	}

	private static <VALUE extends IRegistryObject<String>> ConductanceRegistryImpl<String, VALUE> makeStringKeyed(final String registryName) {
		return new ConductanceRegistryImpl.StringKeyed<>(Conductance.id(registryName));
	}

	private static <VALUE extends IRegistryObject<ResourceLocation>> ConductanceRegistryImpl<ResourceLocation, VALUE> makeResourceKeyed(final String registryName) {
		return new ConductanceRegistryImpl.ResourceKeyed<>(Conductance.id(registryName));
	}

	private static <VALUE extends IRegistryObject<ResourceLocation>> ConductanceDataPackRegistry<VALUE> makeDataPack(final String registryName) {
		return new ConductanceDataPackRegistry<>(Conductance.id(registryName));
	}

	@Override
	public ConductanceRegistry<ResourceLocation, PeriodicElement> periodicElements() {
		return this.periodicElements;
	}

	@Override
	public ConductanceRegistry<ResourceLocation, MaterialTextureType> materialTextureTypes() {
		return this.materialTextureTypes;
	}

	@Override
	public ConductanceRegistry<String, MaterialTextureSet> materialTextureSets() {
		return this.materialTextureSets;
	}

	@Override
	public ConductanceRegistry<ResourceLocation, MaterialTraitKey<?>> materialTraits() {
		return this.materialTraits;
	}

	@Override
	public ConductanceRegistry<ResourceLocation, MaterialFlag> materialFlags() {
		return this.materialFlags;
	}

	@Override
	public ConductanceRegistry<ResourceLocation, MaterialOreType> materialOreTypes() {
		return this.materialOreTypes;
	}

	@Override
	public ConductanceRegistry<String, TaggedMaterialSet> materialTaggedSets() {
		return this.materialTaggedSets;
	}

	@Override
	public ConductanceRegistry<ResourceLocation, Material> materials() {
		return this.materials;
	}

	@Override
	public ConductanceRegistry<ResourceLocation, IRecipeElementType<?>> recipeElementTypes() {
		return this.recipeElementTypes;
	}

	@Override
	public ConductanceRegistry<ResourceLocation, NCRecipeType> recipeTypes() {
		return this.recipeTypes;
	}

	@Override
	public ConductanceRegistry<String, Tier> tiers() {
		return this.tiers;
	}

	@Override
	public ConductanceRegistry<String, MachineType<?>> machines() {
		return this.machines;
	}

	@Override
	public ConductanceRegistry<ResourceLocation, CoverType<?>> covers() {
		return this.covers;
	}
}
