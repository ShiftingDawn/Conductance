package conductance.api.plugin;

import java.util.function.Consumer;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeBuilder;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegisterRecipeEvent implements IConductancePluginEvent {

	public interface RecipeBuilderFactory {

		RecipeBuilder build(NCRecipeType recipeType, ResourceLocation recipeId);
	}

	private final String modid;
	@Getter
	private final RecipeOutput output;
	private final RecipeBuilderFactory factory;

	public void create(final NCRecipeType recipeType, final String recipePath, final Consumer<RecipeBuilder> callback) {
		final RecipeBuilder builder = this.factory.build(recipeType, this.id(recipePath));
		callback.accept(builder);
		builder.save(this.output);
	}

	public ResourceLocation id(final String recipeType, final String recipePath) {
		return ResourceLocation.fromNamespaceAndPath(this.modid, recipeType + "/" + recipePath);
	}

	public ResourceLocation id(final String recipePath) {
		return ResourceLocation.fromNamespaceAndPath(this.modid, recipePath);
	}
}
