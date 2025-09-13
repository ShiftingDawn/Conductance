package conductance.core.register;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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
import conductance.api.registry.IRegistryObject;
import conductance.api.registry.RegistryProvider;
import conductance.api.tier.Tier;
import conductance.Conductance;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class RegistryProviderImpl implements RegistryProvider {

	private static final Map<ResourceLocation, Registry<?>> LOAD_ORDER = new LinkedHashMap<>();

	private final ResourceKey<Registry<PeriodicElement>> periodicElementRegistry = RegistryProviderImpl.makeKey("periodic_element");
	private final ResourceKey<Registry<MaterialTextureType>> materialTextureTypeRegistry = RegistryProviderImpl.makeKey("material_texture_type");
	private final ResourceKey<Registry<MaterialTraitKey<?>>> materialTraitRegistry = RegistryProviderImpl.makeKey("material_trait");
	private final ResourceKey<Registry<MaterialFlag>> materialFlagRegistry = RegistryProviderImpl.makeKey("material_flag");
	private final ResourceKey<Registry<MaterialOreType>> materialOreTypeRegistry = RegistryProviderImpl.makeKey("material_ore_type");
	private final ResourceKey<Registry<TaggedMaterialSet>> materialTaggedSetRegistry = RegistryProviderImpl.makeKey("material_tagged_set");
	private final ResourceKey<Registry<Material>> materialRegistry = RegistryProviderImpl.makeKey("material");
	private final ResourceKey<Registry<IRecipeElementType<?>>> recipeElementTypeRegistry = RegistryProviderImpl.makeKey("recipe_element_type");
	private final ResourceKey<Registry<NCRecipeType>> recipeTypeRegistry = RegistryProviderImpl.makeKey("recipe_type");
	private final ResourceKey<Registry<Tier>> tierRegistry = RegistryProviderImpl.makeKey("tier");
	private final ResourceKey<Registry<MachineType<?>>> machineRegistry = RegistryProviderImpl.makeKey("machine_type");
	private final ResourceKey<Registry<CoverType<?>>> coverRegistry = RegistryProviderImpl.makeKey("cover");

	private final Registry<PeriodicElement> periodicElements = RegistryProviderImpl.makeRegistry(this.periodicElementRegistry);
	private final Registry<MaterialTextureType> materialTextureTypes = RegistryProviderImpl.makeRegistry(this.materialTextureTypeRegistry);
	private final Registry<MaterialTraitKey<?>> materialTraits = RegistryProviderImpl.makeRegistry(this.materialTraitRegistry);
	private final Registry<MaterialFlag> materialFlags = RegistryProviderImpl.makeRegistry(this.materialFlagRegistry);
	private final Registry<MaterialOreType> materialOreTypes = RegistryProviderImpl.makeRegistry(this.materialOreTypeRegistry);
	private final Registry<TaggedMaterialSet> materialTaggedSets = RegistryProviderImpl.makeRegistry(this.materialTaggedSetRegistry);
	private final Registry<Material> materials = RegistryProviderImpl.makeRegistry(this.materialRegistry);
	private final Registry<IRecipeElementType<?>> recipeElementTypes = RegistryProviderImpl.makeRegistry(this.recipeElementTypeRegistry);
	private final Registry<NCRecipeType> recipeTypes = RegistryProviderImpl.makeRegistry(this.recipeTypeRegistry);
	private final Registry<Tier> tiers = RegistryProviderImpl.makeRegistry(this.tierRegistry);
	private final Registry<MachineType<?>> machines = RegistryProviderImpl.makeRegistry(this.machineRegistry);
	private final Registry<CoverType<?>> covers = RegistryProviderImpl.makeRegistry(this.coverRegistry);

	private static <VALUE extends IRegistryObject<String>> ConductanceRegistryImpl<String, VALUE> makeStringKeyed(final String registryName) {
		return new ConductanceRegistryImpl.StringKeyed<>(Conductance.id(registryName));
	}

	private static <VALUE extends IRegistryObject<ResourceLocation>> ConductanceRegistryImpl<ResourceLocation, VALUE> makeResourceKeyed(final String registryName) {
		return new ConductanceRegistryImpl.ResourceKeyed<>(Conductance.id(registryName));
	}

	private static <VALUE extends IRegistryObject<ResourceLocation>> ConductanceDataPackRegistry<VALUE> makeDataPack(final String registryName) {
		return new ConductanceDataPackRegistry<>(Conductance.id(registryName));
	}

	private static <T> ResourceKey<Registry<T>> makeKey(final String id) {
		return ResourceKey.createRegistryKey(Conductance.id(id));
	}

	private static <T> Registry<T> makeRegistry(final ResourceKey<Registry<T>> key, final boolean doSync) {
		return Util.make(new RegistryBuilder<>(key).sync(doSync).create(), registry -> RegistryProviderImpl.LOAD_ORDER.put(key.location(), registry));
	}

	private static <T> Registry<T> makeRegistry(final ResourceKey<Registry<T>> key) {
		return RegistryProviderImpl.makeRegistry(key, true);
	}

	@Override
	public ResourceKey<Registry<PeriodicElement>> periodicElementRegistry() {
		return this.periodicElementRegistry;
	}

	@Override
	public ResourceKey<Registry<MaterialTextureType>> materialTextureTypeRegistry() {
		return this.materialTextureTypeRegistry;
	}

	@Override
	public ResourceKey<Registry<MaterialTraitKey<?>>> materialTraitRegistry() {
		return this.materialTraitRegistry;
	}

	@Override
	public ResourceKey<Registry<MaterialFlag>> materialFlagRegistry() {
		return this.materialFlagRegistry;
	}

	@Override
	public ResourceKey<Registry<MaterialOreType>> materialOreTypeRegistry() {
		return this.materialOreTypeRegistry;
	}

	@Override
	public ResourceKey<Registry<TaggedMaterialSet>> materialTaggedSetRegistry() {
		return this.materialTaggedSetRegistry;
	}

	@Override
	public ResourceKey<Registry<Material>> materialRegistry() {
		return this.materialRegistry;
	}

	@Override
	public ResourceKey<Registry<IRecipeElementType<?>>> recipeElementTypeRegistry() {
		return this.recipeElementTypeRegistry;
	}

	@Override
	public ResourceKey<Registry<NCRecipeType>> recipeTypeRegistry() {
		return this.recipeTypeRegistry;
	}

	@Override
	public ResourceKey<Registry<Tier>> tierRegistry() {
		return this.tierRegistry;
	}

	@Override
	public ResourceKey<Registry<MachineType<?>>> machineRegistry() {
		return this.machineRegistry;
	}

	@Override
	public ResourceKey<Registry<CoverType<?>>> coverRegistry() {
		return this.coverRegistry;
	}

	@Override
	public Registry<PeriodicElement> periodicElements() {
		return this.periodicElements;
	}

	@Override
	public Registry<MaterialTextureType> materialTextureTypes() {
		return this.materialTextureTypes;
	}

	@Override
	public Registry<MaterialTraitKey<?>> materialTraits() {
		return this.materialTraits;
	}

	@Override
	public Registry<MaterialFlag> materialFlags() {
		return this.materialFlags;
	}

	@Override
	public Registry<MaterialOreType> materialOreTypes() {
		return this.materialOreTypes;
	}

	@Override
	public Registry<TaggedMaterialSet> materialTaggedSets() {
		return this.materialTaggedSets;
	}

	@Override
	public Registry<Material> materials() {
		return this.materials;
	}

	@Override
	public Registry<IRecipeElementType<?>> recipeElementTypes() {
		return this.recipeElementTypes;
	}

	@Override
	public Registry<NCRecipeType> recipeTypes() {
		return this.recipeTypes;
	}

	@Override
	public Registry<Tier> tiers() {
		return this.tiers;
	}

	@Override
	public Registry<MachineType<?>> machines() {
		return this.machines;
	}

	@Override
	public Registry<CoverType<?>> covers() {
		return this.covers;
	}
}
