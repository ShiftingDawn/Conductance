package conductance.init.sync;

import javax.annotation.Nullable;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
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

	@Override
	public void toNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		this.write(buf, data -> RecipeSerializerImpl.toNetwork(buf, data));
	}

	@Override
	public void fromNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		this.read(buf, () -> RecipeSerializerImpl.fromNetwork(buf));
	}
}
