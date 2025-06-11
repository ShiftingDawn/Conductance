package conductance.init.sync;

import javax.annotation.Nullable;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.Serializer;
import conductance.core.recipe.RecipeSerializerImpl;

public class RecipeSerializer extends Serializer<IRecipe> {

	@Override
	@Nullable
	public Tag serialize(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		return this.serialize(data -> RecipeSerializerImpl.CODEC.encoder().encodeStart(NbtOps.INSTANCE, data).getOrThrow());
	}

	@Override
	public void deserialize(final Operation operation, final Reference ref, @Nullable final Tag tag, final HolderLookup.Provider registries) {
		this.deserialize(tag, Tag.class, t -> RecipeSerializerImpl.CODEC.decoder().decode(NbtOps.INSTANCE, t).getOrThrow().getFirst());
	}
}
