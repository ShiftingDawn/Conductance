package conductance.core.sync.serializers;

import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.Serializer;

public class RecipeTypeSerializer extends Serializer<NCRecipeType> {

	@Override
	@Nullable
	public Tag serialize(final Reference ref) {
		return StringTag.valueOf(this.getData().getRegistryKey().toString());
	}

	@Override
	public void deserialize(final Reference ref, final Tag tag) {
		final StringTag stringTag = this.testTag(tag, StringTag.class);
		this.setData(CAPI.regs().recipeTypes().get(ResourceLocation.parse(stringTag.getAsString())));
	}

	@Override
	public void toNetwork(final Reference ref, final RegistryFriendlyByteBuf buf) {
		buf.writeResourceLocation(this.getData().getRegistryKey());
	}

	@Override
	public void fromNetwork(final Reference ref, final RegistryFriendlyByteBuf buf) {
		this.setData(CAPI.regs().recipeTypes().get(buf.readResourceLocation()));
	}
}
