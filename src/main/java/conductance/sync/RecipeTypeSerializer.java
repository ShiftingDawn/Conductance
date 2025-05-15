package conductance.sync;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.Serializer;

public class RecipeTypeSerializer extends Serializer<NCRecipeType> {

	@Override
	@Nullable
	public Tag serialize(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		return StringTag.valueOf(this.getData().getRegistryKey().toString());
	}

	@Override
	public void deserialize(final Operation operation, final Reference ref, final Tag tag, final HolderLookup.Provider registries) {
		final StringTag stringTag = this.testTag(tag, StringTag.class);
		this.setData(CAPI.regs().recipeTypes().get(ResourceLocation.parse(stringTag.getAsString())));
	}

	@Override
	public void toNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		buf.writeResourceLocation(this.getData().getRegistryKey());
	}

	@Override
	public void fromNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		this.setData(CAPI.regs().recipeTypes().get(buf.readResourceLocation()));
	}
}
