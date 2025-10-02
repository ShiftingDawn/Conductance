package conductance.lib.pack.server;

import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.ItemLike;
import lombok.RequiredArgsConstructor;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.recipe.event.CookingRecipeBuilder;
import conductance.api.recipe.event.MachineRecipeBuilder;
import conductance.api.recipe.event.RegisterRecipeEvent;
import conductance.api.recipe.event.ShapedCraftingRecipeBuilder;
import conductance.api.recipe.event.ShapelessCraftingRecipeBuilder;
import conductance.api.recipe.event.SmithingRecipeBuilder;
import conductance.api.recipe.event.StonecutterRecipeBuilder;
import conductance.api.recipe.event.TransmuteCraftingRecipeBuilder;

@RequiredArgsConstructor
final class RegisterRecipeEventImpl implements RegisterRecipeEvent {

	interface Delegate {
		void accept(ResourceLocation recipeId, MachineRecipeType recipeType, Consumer<MachineRecipeBuilder> builder, RegisterRecipeEvent self);
	}

	private final String modid;
	private final HolderLookup.Provider registries;
	private final RecipeOutput recipeOutput;
	private final Delegate machineOutput;

	@Override
	public ResourceLocation id(final String recipeType, final String recipePath) {
		return ResourceLocation.fromNamespaceAndPath(this.modid, recipeType + "/" + recipePath);
	}

	@Override
	public ResourceLocation id(final String recipePath) {
		return ResourceLocation.fromNamespaceAndPath(this.modid, recipePath);
	}

	@Override
	public void create(final ResourceLocation recipeId, final MachineRecipeType type, final Consumer<MachineRecipeBuilder> builder) {
		this.machineOutput.accept(recipeId, type, builder, this);
	}

	@Override
	public void shaped(final ResourceLocation recipeId, final ItemStack result, final Consumer<ShapedCraftingRecipeBuilder> builder) {
		Util.make(new ShapedCraftingRecipeBuilderImpl(this.registries, this.stack(result)), builder).build(this.key(recipeId), this.recipeOutput);
	}

	@Override
	public void shaped(final ResourceLocation recipeId, final ItemLike result, final Consumer<ShapedCraftingRecipeBuilder> builder) {
		this.shaped(recipeId, this.stack(result), builder);
	}

	@Override
	public void shapeless(final ResourceLocation recipeId, final ItemStack result, final Consumer<ShapelessCraftingRecipeBuilder> builder) {
		Util.make(new ShapelessCraftingRecipeBuilderImpl(this.registries, this.stack(result)), builder).build(this.key(recipeId), this.recipeOutput);
	}

	@Override
	public void shapeless(final ResourceLocation recipeId, final ItemLike result, final Consumer<ShapelessCraftingRecipeBuilder> builder) {
		this.shapeless(recipeId, this.stack(result), builder);
	}

	@Override
	public void transmute(final ResourceLocation recipeId, final ItemStack result, final Consumer<TransmuteCraftingRecipeBuilder> builder) {
		Util.make(new TransmuteCraftingRecipeBuilderImpl(this.registries, this.stack(result)), builder).build(this.key(recipeId), this.recipeOutput);
	}

	@Override
	public void transmute(final ResourceLocation recipeId, final ItemLike result, final Consumer<TransmuteCraftingRecipeBuilder> builder) {
		this.transmute(recipeId, this.stack(result), builder);
	}

	@Override
	public void smelting(final ResourceLocation recipeId, final ItemStack result, final Consumer<CookingRecipeBuilder> builder) {
		Util.make(new CookingRecipeBuilderImpl<>(this.registries, result, SmeltingRecipe::new, RecipeSerializer.SMELTING_RECIPE), builder).build(this.key(recipeId), this.recipeOutput);
	}

	@Override
	public void smelting(final ResourceLocation recipeId, final ItemLike result, final Consumer<CookingRecipeBuilder> builder) {
		this.smelting(recipeId, this.stack(result), builder);
	}

	@Override
	public void blasting(final ResourceLocation recipeId, final ItemStack result, final Consumer<CookingRecipeBuilder> builder) {
		Util.make(new CookingRecipeBuilderImpl<>(this.registries, result, BlastingRecipe::new, RecipeSerializer.BLASTING_RECIPE), builder).build(this.key(recipeId), this.recipeOutput);
	}

	@Override
	public void blasting(final ResourceLocation recipeId, final ItemLike result, final Consumer<CookingRecipeBuilder> builder) {
		this.blasting(recipeId, this.stack(result), builder);
	}

	@Override
	public void smoking(final ResourceLocation recipeId, final ItemStack result, final Consumer<CookingRecipeBuilder> builder) {
		Util.make(new CookingRecipeBuilderImpl<>(this.registries, result, SmokingRecipe::new, RecipeSerializer.SMOKING_RECIPE), builder).build(this.key(recipeId), this.recipeOutput);
	}

	@Override
	public void smoking(final ResourceLocation recipeId, final ItemLike result, final Consumer<CookingRecipeBuilder> builder) {
		this.smoking(recipeId, this.stack(result), builder);
	}

	@Override
	public void campfire(final ResourceLocation recipeId, final ItemStack result, final Consumer<CookingRecipeBuilder> builder) {
		Util.make(new CookingRecipeBuilderImpl<>(this.registries, result, CampfireCookingRecipe::new, RecipeSerializer.CAMPFIRE_COOKING_RECIPE), builder).build(this.key(recipeId), this.recipeOutput);
	}

	@Override
	public void campfire(final ResourceLocation recipeId, final ItemLike result, final Consumer<CookingRecipeBuilder> builder) {
		this.campfire(recipeId, this.stack(result), builder);
	}

	@Override
	public void stonecutting(final ResourceLocation recipeId, final ItemStack result, final Consumer<StonecutterRecipeBuilder> builder) {
		Util.make(new StonecutterRecipeBuilderImpl(this.registries, this.stack(result)), builder).build(this.key(recipeId), this.recipeOutput);
	}

	@Override
	public void stonecutting(final ResourceLocation recipeId, final ItemLike result, final Consumer<StonecutterRecipeBuilder> builder) {
		this.stonecutting(recipeId, this.stack(result), builder);
	}

	@Override
	public void smithingTransform(final ResourceLocation recipeId, final ItemStack result, final Consumer<SmithingRecipeBuilder> builder) {
		Util.make(new SmithingRecipeBuilderImpl(this.registries, this.stack(result)), builder).build(this.key(recipeId), this.recipeOutput);
	}

	@Override
	public void smithingTransform(final ResourceLocation recipeId, final ItemLike result, final Consumer<SmithingRecipeBuilder> builder) {
		this.smithingTransform(recipeId, this.stack(result), builder);
	}

	private ItemStack stack(final ItemStack stack) {
		return stack;
	}

	private ItemStack stack(final ItemLike item) {
		return new ItemStack(item);
	}

	private ResourceKey<Recipe<?>> key(final ResourceLocation recipeId) {
		return ResourceKey.create(Registries.RECIPE, recipeId);
	}
}
