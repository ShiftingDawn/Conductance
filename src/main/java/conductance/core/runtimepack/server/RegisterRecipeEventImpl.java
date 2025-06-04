package conductance.core.runtimepack.server;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeBuilder;
import conductance.api.plugin.RegisterRecipeEvent;

@AllArgsConstructor
final class RegisterRecipeEventImpl implements RegisterRecipeEvent {

	private final String modid;
	@Getter
	private final RecipeOutput output;
	private final BiFunction<NCRecipeType, ResourceLocation, RecipeBuilder> factory;

	@Override
	public void create(final NCRecipeType recipeType, final String recipePath, final Consumer<RecipeBuilder> callback) {
		final RecipeBuilder builder = this.factory.apply(recipeType, this.id(recipePath));
		callback.accept(builder);
		builder.save(this.output);
	}

	@Override
	public ResourceLocation id(final String recipeType, final String recipePath) {
		return ResourceLocation.fromNamespaceAndPath(this.modid, recipeType + "/" + recipePath);
	}

	@Override
	public ResourceLocation id(final String recipePath) {
		return ResourceLocation.fromNamespaceAndPath(this.modid, recipePath);
	}
}
