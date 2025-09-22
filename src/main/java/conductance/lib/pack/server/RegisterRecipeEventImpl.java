package conductance.lib.pack.server;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import com.google.gson.JsonElement;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.function.TriConsumer;
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

	private final String modid;
	private final BiConsumer<ResourceLocation, JsonElement> output;
	private final TriConsumer<ResourceLocation, MachineRecipeType, Consumer<MachineRecipeBuilder>> output2;

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
		//TODO implement
		this.output2.accept(recipeId, type, builder);
	}

	@Override
	public void shaped(final ResourceLocation recipeId, final ItemStack result, final Consumer<ShapedCraftingRecipeBuilder> builder) {
		this.output.accept(recipeId, Util.make(new ShapedCraftingRecipeBuilderImpl(this.stack(result)), builder).build());
	}

	@Override
	public void shaped(final ResourceLocation recipeId, final ItemLike result, final Consumer<ShapedCraftingRecipeBuilder> builder) {
		this.output.accept(recipeId, Util.make(new ShapedCraftingRecipeBuilderImpl(this.stack(result)), builder).build());
	}

	@Override
	public void shapeless(final ResourceLocation recipeId, final ItemStack result, final Consumer<ShapelessCraftingRecipeBuilder> builder) {
		this.output.accept(recipeId, Util.make(new ShapelessCraftingRecipeBuilderImpl(this.stack(result)), builder).build());
	}

	@Override
	public void shapeless(final ResourceLocation recipeId, final ItemLike result, final Consumer<ShapelessCraftingRecipeBuilder> builder) {
		this.output.accept(recipeId, Util.make(new ShapelessCraftingRecipeBuilderImpl(this.stack(result)), builder).build());
	}

	@Override
	public void transmute(final ResourceLocation recipeId, final ItemStack result, final Consumer<TransmuteCraftingRecipeBuilder> builder) {
		this.output.accept(recipeId, Util.make(new TransmuteCraftingRecipeBuilderImpl(this.stack(result)), builder).build());
	}

	@Override
	public void transmute(final ResourceLocation recipeId, final ItemLike result, final Consumer<TransmuteCraftingRecipeBuilder> builder) {
		this.output.accept(recipeId, Util.make(new TransmuteCraftingRecipeBuilderImpl(this.stack(result)), builder).build());
	}

	@Override
	public void cooking(final ResourceLocation recipeType, final ResourceLocation recipeId, final ItemStack result, final Consumer<CookingRecipeBuilder> builder) {
		this.output.accept(recipeId, Util.make(new CookingRecipeBuilderImpl(recipeType, this.stack(result)), builder).build());
	}

	@Override
	public void cooking(final ResourceLocation recipeType, final ResourceLocation recipeId, final ItemLike result, final Consumer<CookingRecipeBuilder> builder) {
		this.output.accept(recipeId, Util.make(new CookingRecipeBuilderImpl(recipeType, this.stack(result)), builder).build());
	}

	@Override
	public void stonecutting(final ResourceLocation recipeId, final ItemStack result, final Consumer<StonecutterRecipeBuilder> builder) {
		this.output.accept(recipeId, Util.make(new StonecutterRecipeBuilderImpl(this.stack(result)), builder).build());
	}

	@Override
	public void stonecutting(final ResourceLocation recipeId, final ItemLike result, final Consumer<StonecutterRecipeBuilder> builder) {
		this.output.accept(recipeId, Util.make(new StonecutterRecipeBuilderImpl(this.stack(result)), builder).build());
	}

	@Override
	public void smithingTransform(final ResourceLocation recipeId, final ItemStack result, final Consumer<SmithingRecipeBuilder> builder) {
		this.output.accept(recipeId, Util.make(new SmithingRecipeBuilderImpl(this.stack(result)), builder).build());
	}

	@Override
	public void smithingTransform(final ResourceLocation recipeId, final ItemLike result, final Consumer<SmithingRecipeBuilder> builder) {
		this.output.accept(recipeId, Util.make(new SmithingRecipeBuilderImpl(this.stack(result)), builder).build());
	}

	private ItemStack stack(final ItemStack stack) {
		return stack;
	}

	private ItemStack stack(final ItemLike item) {
		return new ItemStack(item);
	}
}
