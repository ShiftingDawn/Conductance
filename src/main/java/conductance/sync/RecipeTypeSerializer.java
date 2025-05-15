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
		return this.serialize(data -> StringTag.valueOf(data.getRegistryKey().toString()));
	}

	@Override
	public void deserialize(final Operation operation, final Reference ref, @Nullable final Tag tag, final HolderLookup.Provider registries) {
		this.deserialize(tag, StringTag.class, t -> CAPI.regs().recipeTypes().get(ResourceLocation.parse(t.getAsString())));
	}

	@Override
	public void toNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		this.write(buf, data -> buf.writeResourceLocation(data.getRegistryKey()));
	}

	@Override
	public void fromNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		this.read(buf, () -> CAPI.regs().recipeTypes().get(buf.readResourceLocation()));
	}
}
