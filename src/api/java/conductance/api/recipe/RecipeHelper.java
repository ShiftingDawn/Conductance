package conductance.api.recipe;

import java.util.List;
import java.util.Map;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.material.Fluid;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.registry.IRegistryObject;
import conductance.api.registry.TaggedSet;
import conductance.api.util.IOMode;

public interface RecipeHelper {

	Codec<IRecipeElementType<?>> ELEMENT_TYPE_CODEC = ResourceLocation.CODEC.xmap(CAPI.regs().recipeElementTypes()::get, IRegistryObject::getRegistryKey);
	StreamCodec<ByteBuf, IRecipeElementType<?>> ELEMENT_TYPE_STREAM_CODEC = ResourceLocation.STREAM_CODEC.map(CAPI.regs().recipeElementTypes()::get, IRegistryObject::getRegistryKey);
	Codec<Map<IRecipeElementType<?>, List<RecipeElement>>> ELEMENT_MAP_CODEC = Codec.dispatchedMap(RecipeHelper.ELEMENT_TYPE_CODEC, key -> key.codec().listOf());

	boolean test(IRecipe recipe, RecipeCapabilityHolder holder);

	boolean testPerTick(IRecipe recipe, RecipeCapabilityHolder holder);

	boolean handle(IRecipe recipe, IOMode ioMode, RecipeCapabilityHolder holder);

	boolean handlePerTick(IRecipe recipe, IOMode ioMode, RecipeCapabilityHolder holder);

	List<IRecipe> findRecipes(NCRecipeType recipeType, RecipeManager recipeManager, RecipeCapabilityHolder holder, @Nullable Map<IRecipeElementType<?>, Integer> recipeOutputLimits);

	<T> AutoRecipeData calculateRecipeData(T obj, TaggedSet<T> inputType, TaggedSet<T> outputType, int baseTime, long baseEnergy);

	@UnknownNullability
	static TagKey<Item> getItemTag(final TaggedMaterialSet tagType, final Material material) {
		return tagType.streamItemTags(material).findFirst().orElse(null);
	}

	@UnknownNullability
	static TagKey<Fluid> getFluidTag(final TaggedMaterialSet tagType, final Material material) {
		return tagType.streamFluidTags(material).findFirst().orElse(null);
	}
}
