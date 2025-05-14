package conductance.core.sync.serializers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.Serializer;
import conductance.core.recipe.RecipeSerializerImpl;

public class RecipeSerializer extends Serializer<IRecipe> {

	@Override
	@Nullable
	public Tag serialize(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		return RecipeSerializerImpl.CODEC.encoder().encodeStart(NbtOps.INSTANCE, this.getData()).getOrThrow();
	}

	@Override
	public void deserialize(final Operation operation, final Reference ref, final Tag tag, final HolderLookup.Provider registries) {
		this.setData(RecipeSerializerImpl.CODEC.decoder().decode(NbtOps.INSTANCE, tag).getOrThrow().getFirst());
	}

	@Override
	public void toNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		RecipeSerializerImpl.toNetwork(buf, this.getData());
	}

	@Override
	public void fromNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		this.setData(RecipeSerializerImpl.fromNetwork(buf));
	}
}
