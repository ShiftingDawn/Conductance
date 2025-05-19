package conductance.api.plugin;

import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeTypeBuilder;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegisterRecipeTypeEvent implements IConductancePluginEvent {

	private final String modid;
	private final Function<ResourceLocation, RecipeTypeBuilder> delegate;

	public NCRecipeType register(final String name, final Consumer<RecipeTypeBuilder> builder) {
		final RecipeTypeBuilder recipeTypeBuilder = this.delegate.apply(ResourceLocation.fromNamespaceAndPath(this.modid, name));
		builder.accept(recipeTypeBuilder);
		return recipeTypeBuilder.build();
	}
}
