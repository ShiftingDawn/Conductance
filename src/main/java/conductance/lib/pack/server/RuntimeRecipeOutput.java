package conductance.lib.pack.server;

import java.util.Optional;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import com.mojang.serialization.JsonOps;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;
import com.google.gson.JsonElement;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
final class RuntimeRecipeOutput implements RecipeOutput {

	private final HolderLookup.Provider registries;

	@Override
	public Advancement.Builder advancement() {
		return new Advancement.Builder();
	}

	@Override
	public void includeRootAdvancement() {
	}

	@Override
	public void accept(final ResourceKey<Recipe<?>> resourceKey, final Recipe<?> recipe, @Nullable final AdvancementHolder advancementHolder, final ICondition... iConditions) {
		final RegistryOps<JsonElement> ctx = this.registries.createSerializationContext(JsonOps.INSTANCE);
		final JsonElement json = Recipe.CONDITIONAL_CODEC.encodeStart(ctx, Optional.of(new WithConditions<>(recipe, iConditions))).getOrThrow();
		RuntimeDataPack.addRecipe(resourceKey.location(), json);
	}
}

