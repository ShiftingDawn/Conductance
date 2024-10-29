package conductance.core.recipe;

import net.minecraft.resources.ResourceLocation;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.NCRecipeSerializer;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.registry.RegistryObject;

public class RecipeTypeImpl extends RegistryObject<ResourceLocation> implements NCRecipeType {

	private final Object2IntMap<IRecipeElementType<?>> maxInputs;
	private final Object2IntMap<IRecipeElementType<?>> maxOutputs;

	public RecipeTypeImpl(
			final ResourceLocation registryKey,
			final Object2IntMap<IRecipeElementType<?>> maxInputs,
			final Object2IntMap<IRecipeElementType<?>> maxOutputs) {
		super(registryKey);
		this.maxInputs = Object2IntMaps.unmodifiable(maxInputs);
		this.maxOutputs = Object2IntMaps.unmodifiable(maxOutputs);
	}

	@Override
	public NCRecipeSerializer getSerializer() {
		return RecipeSerializerImpl.INSTANCE;
	}

	@Override
	public int getMaxInputs(final IRecipeElementType<?> elementType) {
		return this.maxInputs.getOrDefault(elementType, -1);
	}

	@Override
	public int getMaxOutputs(final IRecipeElementType<?> elementType) {
		return this.maxOutputs.getOrDefault(elementType, -1);
	}
}
