package conductance.core.sync.serializers;

import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.Serializer;
import conductance.core.recipe.RecipeSerializerImpl;

public class RecipeSerializer extends Serializer<IRecipe> {

	@Override
	@Nullable
	public Tag serialize(final Reference ref) {
		return RecipeSerializerImpl.CODEC.encoder().encodeStart(NbtOps.INSTANCE, this.getData()).getOrThrow();
	}

	@Override
	public void deserialize(final Reference ref, final Tag tag) {
		this.setData(RecipeSerializerImpl.CODEC.decoder().decode(NbtOps.INSTANCE, tag).getOrThrow().getFirst());
	}

	@Override
	public void toNetwork(final Reference ref, final RegistryFriendlyByteBuf buf) {
		RecipeSerializerImpl.toNetwork(buf, this.getData());
	}

	@Override
	public void fromNetwork(final Reference ref, final RegistryFriendlyByteBuf buf) {
		this.setData(RecipeSerializerImpl.fromNetwork(buf));
	}
}
