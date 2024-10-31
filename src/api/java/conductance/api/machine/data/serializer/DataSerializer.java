package conductance.api.machine.data.serializer;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

public interface DataSerializer<T> {

	@Nullable
	Tag serialize(HolderLookup.Provider registries);

	void deserialize(Tag tag, HolderLookup.Provider registries);

	void toNetwork(RegistryFriendlyByteBuf buf);

	void fromNetwork(RegistryFriendlyByteBuf buf);

	T getData();
}
