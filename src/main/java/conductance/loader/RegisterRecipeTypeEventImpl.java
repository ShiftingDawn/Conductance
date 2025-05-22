package conductance.loader;

import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import lombok.AllArgsConstructor;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeTypeBuilder;
import conductance.api.plugin.RegisterRecipeTypeEvent;

@AllArgsConstructor
final class RegisterRecipeTypeEventImpl implements RegisterRecipeTypeEvent {

	private final String modid;
	private final Function<ResourceLocation, RecipeTypeBuilder> delegate;

	@Override
	public NCRecipeType register(final String name, final Consumer<RecipeTypeBuilder> builder) {
		final RecipeTypeBuilder recipeTypeBuilder = this.delegate.apply(ResourceLocation.fromNamespaceAndPath(this.modid, name));
		builder.accept(recipeTypeBuilder);
		return recipeTypeBuilder.build();
	}
}
